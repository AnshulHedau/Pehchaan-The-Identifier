# Initial setup
import json
import numpy as np, scipy.stats
import pandas as pd
import pyrebase
import requests
import time

from datetime import datetime, timedelta
from flask import Flask, jsonify, request, redirect, url_for
from sklearn.externals import joblib


# Flask object creation
app = Flask(__name__)


# Pyrebase object creation
config = {
	"apiKey": "AIzaSyBJqYncSdJGtQ3DHAcpm6LfwGfvps6WW-c",
    "authDomain": "pehchaan-eb1a1.firebaseapp.com",
    "databaseURL": "https://pehchaan-eb1a1.firebaseio.com",
    "storageBucket": "pehchaan-eb1a1.appspot.com"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()


# Index page
@app.route("/")
def index():
    return_value = {"message": "Welcome to the Pehchaan API!"}
    json_string = json.dumps(return_value)
    return json_string


# Prediction page
@app.route('/predict', methods=['GET'])
def predict():
     json_ = request.json
     clf = joblib.load('model.pkl')
     
     myvar = request.args["myvar"]
     parameter_list = myvar.split(',')
     
     myvar = parameter_list[0]
     upper_value = int(parameter_list[1])
     lower_value = int(parameter_list[2])
     
     len_array = len(myvar)
     list_filtered_values = []
     
     for i in range(0,len_array):
         if (myvar[i] == '0'):
             list_filtered_values.append(lower_value)
         else:
             list_filtered_values.append(upper_value)
     
     inputarray = []
     inputarray.append([np.amin(list_filtered_values), np.amax(list_filtered_values), np.ptp(list_filtered_values),
                           np.percentile(list_filtered_values,75), np.percentile(list_filtered_values,25),
                           np.median(list_filtered_values),np.mean(list_filtered_values),np.std(list_filtered_values),
                           np.var(list_filtered_values),scipy.stats.kurtosis(list_filtered_values),
                           scipy.stats.skew(list_filtered_values)])
                           
     prediction = clf.predict(inputarray)
     percentage_predictions = clf.predict_proba(inputarray)
     
     percentage_predictions = np.array(percentage_predictions.tolist())
     db.child("confidence_values").set({"material_1":percentage_predictions[0][0], "material_2":123, "material_3":13, "material_4":1})
     
     return jsonify({'prediction': list(prediction),'ceramics':percentage_predictions[0][0],'plastic':percentage_predictions[0][1],'wood':0,'metal':0})


# Error page
@app.errorhandler(404)
def page_not_found(e):
    return json.dumps("error"), 404


if __name__ == "__main__":
    app.debug = True
    clf = joblib.load('model.pkl')
    app.run()
