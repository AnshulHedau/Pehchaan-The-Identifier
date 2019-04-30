# Pehchaan-The-Identifier 

<p align = "center"><img src="../Pictures/App Logo.png" width = 10%"></img></p>

_Pehchaan_ is the Hindi term for *"recognise"*. 

In this project, we devise a novel method for **detection of the material of an object**. This proof-of-concept consists of an ultrasonic sensor being kept at an established distance from the material.

## API-Interface-Flask

### Endpoints Details
1. **[Landing Page](https://improved-pancakes.herokuapp.com/)**: This is the basic landing page to tell the working status of the API.

2. **[Prediction (/predict)](https://improved-pancakes.herokuapp.com/predict?myvar="Values")**: This is the main API endpoint for the prediction module. This takes the signal values from the acquisition module and apply the machine learning model based on the pkl model file. This returns the confidence probability of which material the signal value can correspond. 
Endpoint Format - /predict?myvar="Values" where Values are the comma seperated signal values.

3. **[Result Page (/view)](https://improved-pancakes.herokuapp.com/view)**: The Website is the consumer facing Web application which displays the confidence values for the different materials.
