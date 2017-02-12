const int KNOCK_SENSOR = A0;
const int THRESHOLD = 20;
const int TIMEOUT_DELAY = 5000;
const String SERVER_ACK = "SRV";

String serverId = "";

void setup(){
  Serial.begin(9600);
}

void loop() {
  if (Serial.available()) {
    // catch data emitted while BT is not connected
    String serverIdReceived = Serial.readString();
    if (serverIdReceived.startsWith(SERVER_ACK)){
      serverId = serverIdReceived.substring(SERVER_ACK.length());
    }
  }
  
  int knockValue = analogRead(KNOCK_SENSOR);
  if (knockValue >= THRESHOLD) {
    Serial.println(serverId + "KNK" + (String)knockValue);
    delay(TIMEOUT_DELAY);
  }
}
