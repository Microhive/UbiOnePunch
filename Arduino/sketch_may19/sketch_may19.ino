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

// Averaging values
int const queue_max_size = 5;
int queue[queue_max_size][6];
int queue_count = 0;
int is_full = 0;
int q_avg[6]; //hold q values

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

  // Add new element to the queue
  for (int n = 0; n < 6; n++)
  {
    queue[queue_count][n] = q[n];
    q_avg[n] = 0;
  }
  
  // Average the values, summing first
  for (int i = 0; i < queue_max_size; i++)
  {
    for (int n = 0; n < 6; n++)
    {
      q_avg[n] += queue[i][n];
    }
  }

  // Average the values, dividing now
  for (int n = 0; n < 6; n++)
  {
    if (is_full == 1)
    {
      q_avg[n] = q_avg[n] / queue_max_size;  
    }
    else
    {
      q_avg[n] = q_avg[n] / queue_count + 1;  
    }
    queue[queue_count][n] = q_avg[n];
  }
  
  Serial.print("h"); // send a header character
  Serial.print(",");
  Serial.print(queue[queue_count][0]);
  Serial.print(",");
  Serial.print(queue[queue_count][1]);
  Serial.print(",");  
  Serial.print(queue[queue_count][2]);
  Serial.print(",");
  Serial.print(queue[queue_count][3]);
  Serial.print(",");
  Serial.print(queue[queue_count][4]);
  Serial.print(",");  
  Serial.print(queue[queue_count][5]); 
  Serial.print(",\n"); 

  queue_count++;

  if (queue_count >= queue_max_size)
  {
    is_full = 1;
    queue_count = 0;
  }
  
  delay(63);
}

