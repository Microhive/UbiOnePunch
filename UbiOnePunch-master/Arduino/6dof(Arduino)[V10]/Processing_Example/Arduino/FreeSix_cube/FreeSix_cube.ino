////////////////////////////////////////////////////////////
// Arduino firmware for use with FreeSixCube processing example
////////////////////////////////////////////////////////////

#include <FreeSixIMU.h>
#include <FIMU_ADXL345.h>
#include <FIMU_ITG3200.h>
#include <SoftwareSerial.h>

#define DEBUG
#ifdef DEBUG
#include "DebugUtils.h"
#endif

#include "CommunicationUtils.h"
#include "FreeSixIMU.h"
#include <Wire.h>


float q[4]; //hold q values

//BLUETOOTH
const int RX_PIN = 10;
const int TX_PIN = 9;
const int BLUETOOTH_BAUD_RATE = 9600;

SoftwareSerial bluetooth(RX_PIN, TX_PIN);

// Set the FreeIMU object
FreeSixIMU my3IMU = FreeSixIMU();

void setup() {
  Serial.begin(9600);
  bluetooth.begin(BLUETOOTH_BAUD_RATE);
   
  //Serial.begin(115200);
  Wire.begin();

  delay(5);
  my3IMU.init();
  delay(5);
}


void loop() { 
  if (bluetooth.available()) {
    Serial.write(bluetooth.read());
  }
  if (Serial.available()) {
    bluetooth.write(Serial.read());
  }
  
  my3IMU.getQ(q);
  serialPrintFloatArr(q, 4);
  Serial.println(""); //line break
 
  delay(60);
}
