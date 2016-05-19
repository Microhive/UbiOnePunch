#include <SoftwareSerial.h>
 
// Initializing communication ports
SoftwareSerial mySerial(11, 10); // TX/RX pins
 
void setup()  
{
  Serial.begin(9600);
  mySerial.begin(9600);
}
 
String getMessage(){
  String msg = "";
  char a;
  
  while(mySerial.available()) {
      a = mySerial.read();
      msg+=String(a);
  }
  return msg;
}
 
void loop()
{
    // Check if a message has been received
    String msg = getMessage();
    if(msg!=""){
      Serial.println(msg);
    }
 
    // Send the text you entered in the input field of the Serial Monitor to the HC-06
    if(Serial.available()){
      mySerial.write(Serial.read());
    }
 
    delay(10);
}
