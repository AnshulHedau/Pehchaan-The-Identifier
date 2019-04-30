// Declaring the required Arduino pins
const int trigPin = 3;
const int analogPin = A0;

// Declaring a global variable
long analog_value;

void setup() {
  // Sets the trigPin as an output pin and analogPin as input pin
  pinMode(trigPin, OUTPUT); 
  pinMode(analogPin, INPUT);

   // Starts the serial communication
  Serial.begin(9600);
}

void loop() {
  // Clears the trigPin
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);

  // Sets the trigPin on HIGH state for 10 microseconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  // Reads the analogPin and returns an integer analog value
  analog_value = pulseIn(analogPin, HIGH);
  
  // Printing the analog value
  Serial.println(analog_value);
}
