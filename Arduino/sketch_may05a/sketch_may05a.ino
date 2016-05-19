#include <SoftwareSerial.h>
SoftwareSerial BT(10, 11); 
SoftwareSerial GS(8, 9); 
// creates a "virtual" serial port/UART
// connect BT module TX to D10
// connect BT module RX to D11
// connect BT Vcc to 5V, GND to GND

#include <FreeSixIMU.h>
#include <FIMU_ADXL345.h>
#include <FIMU_ITG3200.h>

#include <Wire.h>

float angles[3]; // yaw pitch roll

// Set the FreeSixIMU object
FreeSixIMU sixDOF = FreeSixIMU();

void setup()  
{
  // set digital pin to control as an output
  pinMode(13, OUTPUT);
  // set the data rate for the SoftwareSerial port
  BT.begin(9600);
  // Send test message to other device
  BT.println("Hello from Arduino");

  GS.begin(9600);
  Wire.begin();
  
  delay(5);
  sixDOF.init(); //begin the IMU
  delay(5);
}
char a; // stores incoming character from other device
void loop() 
{

  sixDOF.getEuler(angles);
  
  if (BT.available())
  // if text arrived in from BT serial...
  {
      BT.print(angles[0]);
      BT.print(" | ");  
      BT.print(angles[1]);
      BT.print(" | ");
      BT.println(angles[2]);
  }

  delay(100); 
}
