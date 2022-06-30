import time
from umqttsimple import MQTTClient
import ubinascii
import machine
import micropython
import network
import esp
from machine import Pin
import dht
esp.osdebug(None)
import gc
gc.collect()

ssid = 'Hakam'
password = 'Hakam1999'
mqtt_server = 'broker.mqttdashboard.com'
#EXAMPLE IP ADDRESS or DOMAIN NAME
#mqtt_server = '192.168.1.106'

client_id = ubinascii.hexlify(machine.unique_id())

topic_pub_temp = b'esp/dht/temp'
topic_pub_value = b'esp/value'
topicsub = b'on/off'

last_message = 0
message_interval = 30

station = network.WLAN(network.STA_IF)

station.active(True)
station.connect(ssid, password)

while station.isconnected() == False:
  pass

print('Connection successful', station.ifconfig())

#sensor = dht.DHT22(Pin(14))
sensor = dht.DHT11(Pin(09))
pot =machine.ADC(0)
led =  machine.Pin(12, machine.Pin.OUT)

def subscribecallback(topic,msg):
  print (msg)
  if msg == b'off':
    led.value(0)
  if msg == b'on':
    led.value(1)

def connect_mqtt():
  global client_id, mqtt_server
  client = MQTTClient(client_id, mqtt_server)
  #client = MQTTClient(client_id, mqtt_server, user=your_username, password=your_password)
  client.set_callback(subscribecallback)
  client.connect()
  client.subscribe(topicsub)
  print('Connected to %s MQTT broker' % (mqtt_server))
  return client

def restart_and_reconnect():
  print('Failed to connect to MQTT broker. Reconnecting...')
  time.sleep(10)
  #machine.reset()

def read_sensor():
  try:
    sensor.measure()
    temp = sensor.temperature()
    # uncomment for Fahrenheit
    #temp = temp * (9/5) + 32.0
    hum = sensor.humidity()
    if (isinstance(temp, float) and isinstance(hum, float)) or (isinstance(temp, int) and isinstance(hum, int)):
      temp = (b'{0:3.1f},'.format(temp))
      hum =  (b'{0:3.1f},'.format(hum))
      return temp
    else:
      return('Invalid sensor readings.')
  except OSError as e:
    return('Failed to read sensor.')




  
try:
  led.value(0)
  time.sleep(1)
  led.value(1)
  client = connect_mqtt()
except OSError as e:
  print("error 1")
  restart_and_reconnect()

while True:
  
  try:
    if (time.time() - last_message) > message_interval:
      temp = read_sensor()
      #v = str(pot.read())
      
      #print(v)
      print(temp)       
      client.publish(topic_pub_temp, temp)
      #client.publish(topic_pub_value, v)
      client.check_msg()
      last_message = time.time()
      
  except OSError as e:
    print('error 2')
    restart_and_reconnect()















