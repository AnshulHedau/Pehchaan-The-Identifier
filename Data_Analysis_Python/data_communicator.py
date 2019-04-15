# Importing the required libraries
import numpy as np
import scipy.signal
import serial
import serial.tools.list_ports
import sys
import urllib.request

# Viewing all the available ports
ports = serial.tools.list_ports.comports()

print('\n\nThe available ports are:')
for port, desc, hwid in sorted(ports):
	print("{}: {} [{}]".format(port, desc, hwid))

# Initialising the COM and BAUD rate
COM = '/dev/cu.usbmodem14101'
BAUD = 9600

while True:
	input_value = input('\n\nPress return to start recording data: ')
	
	# Declaring a Serial object
	arduino_serial = serial.Serial(COM, BAUD, timeout = .1)
	input_values = []
	
	# Getting the data from serial port and adding it an array
	while len(input_values) < 50:
		value = str(arduino_serial.readline().decode().strip('\r\n'))
		
		try:
			if len(value) > 0 and int(value):
				input_values.append(int(value))
		except:
			continue
	
	print('\nThe recorded values are: ', input_values)
	
	# Filtering of the input values
	filtered_values = []
	
	mean_value = np.mean(input_values)
	value_25 = np.percentile(input_values, 25)
	value_75 = np.percentile(input_values, 75)
	
	for value in input_values:
		filtered_values.append(value_75 if value > mean_value else value_25)
	print('\nThe filtered values are: ', filtered_values)
	
	# Extraction of the signal parameters
	extracted_parameters = [np.amin(filtered_values), np.amax(filtered_values),
							np.ptp(filtered_values), np.percentile(filtered_values, 75),
							np.percentile(filtered_values, 25), np.median(filtered_values),
							np.mean(filtered_values), round(np.std(filtered_values),2),
							round(np.var(filtered_values),2), round(scipy.stats.kurtosis(filtered_values),2),
							round(scipy.stats.skew(filtered_values),2)]
							
	print('\nThe parameters of this signal are: ', extracted_parameters)
	
	# Calling the web API
	web_api_url = "https://pacific-harbor-19774.herokuapp.com/predict?myvar=" + str(extracted_parameters[0]) + "," + str(extracted_parameters[1]) + "," + str(extracted_parameters[2]) + "," + str(extracted_parameters[3]) + "," + str(extracted_parameters[4]) + "," + str(extracted_parameters[5]) + "," + str(extracted_parameters[6]) + "," + str(extracted_parameters[7]) + "," + str(extracted_parameters[8]) + "," + str(extracted_parameters[9]) + "," + str(extracted_parameters[10])
	
	print('\nThe web API URL is: ', web_api_url)
	urllib.request.urlopen(web_api_url)
	
	# Closing the Serial connection
	arduino_serial.close()
