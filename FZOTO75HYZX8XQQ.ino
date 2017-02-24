// Basic Bluetooth sketch HC-05_AT_MODE_01
// Communicate with a HC-05 using the serial monitor
//
// The HC-05 defaults to communication mode when first powered on you will need to manually enter AT mode
// The default baud rate for AT mode is 38400
// See www.martyncurrey.com for details
//

#include <AFMotor.h>
#include <SoftwareSerial.h>
#include <AccelStepper.h>
#include <dht11.h>

dht11 DHT;


//Bluetooth senso in A4 - A5
SoftwareSerial BTserial(A4, A5); 

//pResistor fotoresistenza
int pResistor = A1;

//char received from Android App
char inputChar = ' ';

//const long distance = 100;

//Brightness received from the sensor 
int brightness = 0;

//Temperature received from the sensor 
double temperature = 0;

//uscita LED
int ledPin = A2;

//array of chars for the bt communication
char charBuf[50];

//Temporary string to manage the received datas
String tString = "";

// two stepper motors one on each port
AF_Stepper motor1(1, 1);
AF_Stepper motor2(1, 2);

// you can change these to DOUBLE or INTERLEAVE or MICROSTEP!
// wrappers for the first motor!
void forwardstep1() {
  motor1.onestep(FORWARD, INTERLEAVE);
}

void forwardstep2() {
  motor2.onestep(FORWARD,  INTERLEAVE);
}

// you can change these to DOUBLE or INTERLEAVE or MICROSTEP!
// wrappers for the first motor!
void back1() {
  motor1.step(-100, RELEASE, SINGLE);
}

void back2() {
  motor2.step(-100, RELEASE, SINGLE);
}

// Motor shield has two motor ports, now we'll wrap them in an AccelStepper object
AccelStepper stepper1(forwardstep1, forwardstep1);
// Motor shield has two motor ports, now we'll wrap them in an AccelStepper object
AccelStepper stepper2(forwardstep2, forwardstep2);
bool flag = true;


void setup()
{
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
  Serial.println("Arduino is ready");

  // HC-05 default serial speed for AT mode is 38400
  BTserial.begin(9600);

  stepper1.setMaxSpeed(4.0);
  stepper1.setAcceleration(4.0);
  stepper1.moveTo(24);
  stepper2.setMaxSpeed(4.0);
  stepper2.setAcceleration(4.0);
  stepper2.moveTo(24);

}

void loop()
{
  uint8_t i;

  // Keep reading from HC-05 and send to Arduino Serial Monitor
  if (BTserial.available())
  {
   int val = 0;
     int i = 0;
   inputChar = BTserial.read();
   Serial.write(inputChar);
   switch (inputChar) {
    //The temperature in Chelsius
    case 't':    
      val = analogRead(A0);
      temperature = Thermister(val);
      DHT.read(A3);  
      tString = String(DHT.temperature) + "/";
      tString.toCharArray(charBuf, 10);
      BTserial.write(charBuf);
      break;
    //The brightness in Lemon
    case 'b':
      brightness = (analogRead(pResistor));
      tString = String(brightness) + "/";
      tString.toCharArray(charBuf, 10);
      BTserial.write(charBuf);
      break;
    //Move the robot up direction
    case 'u':
      motor1.setCurrentStep(1);
      motor2.setCurrentStep(1);
      stepper1.runSpeed();
      stepper2.runSpeed();
      break;
     //Move the robot up direction
    case 'd':
      motor1.setCurrentStep(20);
      motor2.setCurrentStep(20);
      stepper1.runSpeed();
      stepper2.runSpeed();
      break;
     //Move the robot left direction
    case 'l':
      motor1.setCurrentStep(1);
      motor2.setCurrentStep(1);
      stepper2.runSpeed();
      break;
     //Move the robot right direction
    case 'r':
      motor1.setCurrentStep(1);
      motor2.setCurrentStep(1);
      stepper1.runSpeed();
      break;
    case 's':
      motor1.release();
      motor2.release();
      break;
    case 'h':
      int chk1;
      chk1 = DHT.read(A3);    // READ DATA
      tString = String(DHT.humidity) + "/";
      tString.toCharArray(charBuf, 10);
      BTserial.write(charBuf);
      break;
    //Turn on LED
    case 'x':
        //BTserial.write("LED ON/");
       digitalWrite(ledPin,HIGH);
       break;
    //Turn off LED
    case 'y':
       //BTserial.write("LED OFF/");
       digitalWrite(ledPin,LOW);
       break;

         //Turn on LED
    case 'z':
        BTserial.write("LED ON/");
       digitalWrite(ledPin,HIGH);
       break;
    //Turn off LED
    case 'k':
       BTserial.write("LED OFF/");
       digitalWrite(ledPin,LOW);
       break;
   
   
   
    

     
  }


    /*
 

    if (inputChar == 't') {
      int val = analogRead(A0);
      double temperature = Thermister(val);
      String tempStr = String(temperature) + "/";
      char charBuf[10];
      tempStr.toCharArray(charBuf, 10);
      BTserial.write(charBuf);

    }
    else if (inputChar == 'u') {
      // two stepper motors one on each port
      motor1.setCurrentStep(1);
      motor2.setCurrentStep(1);
      stepper1.run();
      stepper2.run();
    }


    else if (inputChar == 'd')
    {
      // two stepper motors one on each port
      motor1.setCurrentStep(20);
      motor2.setCurrentStep(20);
      stepper1.run();
      stepper2.run();

    }


    else if (inputChar == 'b')
    {
      brightness = (analogRead(pResistor));
      String s = "The brightness is: " + String(brightness) + "\n";
      char charBuf[50];
      s.toCharArray(charBuf, 50);
      Serial.println(charBuf);
      BTserial.write(charBuf);
    }

    /* else if (c == 'l') {
      // two stepper motors one on each port
      motor1.setCurrentStep(1);
      motor2.setCurrentStep(1);
      stepper2.runSpeed();
      }


      else if (c == 'r')
      {
      // two stepper motors one on each port
      motor1.setCurrentStep(1);
      motor2.setCurrentStep(1);
      stepper1.runSpeed();

      }*/

/*

    else if (inputChar == 's')
    {
      /*       stepper1.setMaxSpeed(0.0);
        stepper1.setSpeed(0);
        stepper2.setMaxSpeed(0.0);
        stepper2.setSpeed(0);
        /*
        stepper1.setMaxSpeed(0);
        stepper1.setSpeed(0);
        stepper1.setAcceleration(0);
        stepper2.setMaxSpeed(0);
        stepper2.setSpeed(0);
        stepper2.setAcceleration(0);


        /* stepper1.moveTo(stepper1.currentPosition());
        stepper2.moveTo(stepper2.currentPosition());
        stepper1.runSpeedToPosition();

        stepper2.runSpeedToPosition();*/
     /* motor1.release();
      motor2.release();



      /*

        // Motor shield has two motor ports, now we'll wrap them in an AccelStepper object
        AccelStepper stepper1(forwardstep1,back1);
        // Motor shield has two motor ports, now we'll wrap them in an AccelStepper object
        AccelStepper stepper2(forwardstep2,back2);

           stepper1.setMaxSpeed(200.0);
          stepper1.setAcceleration(100.0);
          stepper1.moveTo(10);

          stepper2.setMaxSpeed(200.0);
          stepper2.setAcceleration(100.0);
          stepper2.moveTo(10);

      */
    }







    //}

    /*
       else{
         Serial.write ("test");}
      }
    */
    // Keep reading from Arduino Serial Monitor and send to HC-05
    if (Serial.available())
    {
      inputChar =  Serial.read();
      BTserial.write(inputChar);
    }
  
}

double Thermister(int RawADC)
{ //Function to perform the fancy math of the Steinhart-Hart equation
  double Temp;
  Temp = log(((10240000 / RawADC) - 10000));
  Temp = 1 / (0.001129148 + (0.000234125 + (0.0000000876741 * Temp * Temp )) * Temp );
  Temp = Temp - 273.15;              // Convert Kelvin to Celsius
  return Temp;
}
