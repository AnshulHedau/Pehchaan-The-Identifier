# Importing the required libraries
import serial
import serial.tools.list_ports
import sys

# Viewing all the available ports
ports = serial.tools.list_ports.comports()

print('\n\nThe available ports are:')
for port, desc, hwid in sorted(ports):
	print("{}: {} [{}]".format(port, desc, hwid))

# Initialising the COM and BAUD rate
COM = '/dev/cu.usbmodem14201'
BAUD = 9600

# Declaring a Serial object
arduino_serial = serial.Serial(COM, BAUD, timeout = .1)

while True:
	val = str(arduino_serial.readline().decode().strip('\r\n'))
	
