#include <FreeSixIMU.h>
#include <FIMU_ADXL345.h>
#include <FIMU_ITG3200.h>

#define DEBUG
#ifdef DEBUG
#include "DebugUtils.h"
#endif

#include "CommunicationUtils.h"
#include "FreeSixIMU.h"
#include <Wire.h>

int q[6]; //hold q values

// Set the FreeIMU object
FreeSixIMU my3IMU = FreeSixIMU();

void setup() {
Serial.begin(9600);
Wire.begin();

delay(5);
my3IMU.init();
delay(5);
}

void loop() { 
  
my3IMU.getRawValues(q);

Serial.print("h"); // send a header character
Serial.print(",");
Serial.print(q[0]);
Serial.print(",");
Serial.print(q[1]);
Serial.print(",");  
Serial.print(q[2]);
Serial.print(",");
Serial.print(q[3]);
Serial.print(",");
Serial.print(q[4]);
Serial.print(",");  
Serial.print(q[5]); 
Serial.print(",\n");  

delay(62);
}

