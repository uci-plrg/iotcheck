import os

#Create directory for files to append in the main file

#Extract information from preferences and subscribe method to create required objects
os.system("python Extractor/ExtractorScript.py")

#Files for both Apps
Out = open("main.groovy", "w+")
GlobalVariablesBothApps = open("GlobalVariables/"+"GlobalVariablesBothApps.groovy", "r")
eventSimulator = open("eventSimulator/"+"eventSimulator.groovy", "r")

#For App1
GlobalVariablesEachApp = open("GlobalVariables/"+"GlobalVariablesEachApp.groovy", "r")
setLocationMode = open("Methods/"+"setLocationMode.groovy", "r")
subscribe = open("Methods/"+"subscribe.groovy", "r")
runIn = open("Methods/"+"runIn.groovy", "r")
runDaily = open("Methods/"+"runDaily.groovy", "r")
unschedule = open("Methods/"+"unschedule.groovy", "r")
sendNotificationToContacts = open("Methods/"+"sendNotificationToContacts.groovy", "r")
sendSms = open("Methods/"+"sendSms.groovy", "r")
sendPush = open("Methods/"+"sendPush.groovy", "r")
eventHandler = open("Methods/"+"eventHandler.groovy", "r")
schedule = open("Methods/"+"schedule.groovy", "r")
httpPostJson = open("Methods/"+"httpPostJson.groovy", "r")
now = open("Methods/"+"now.groovy", "r")
getTemperatureScale = open("Methods/"+"getTemperatureScale.groovy", "r")
getSunriseAndSunset = open("Methods/"+"getSunriseAndSunset.groovy", "r")
runEvery15Minutes = open("Methods/"+"runEvery15Minutes.groovy", "r")
timeToday = open("Methods/"+"timeToday.groovy", "r")
sendNotification = open("Methods/"+"sendNotification.groovy", "r")
canSchedule = open("Methods/"+"canSchedule.groovy", "r")
createAccessToken = open("Methods/"+"createAccessToken.groovy", "r")
runOnce = open("Methods/"+"runOnce.groovy", "r")
parseJson = open("Methods/"+"parseJson.groovy", "r")
unsubscribe = open("Methods/"+"unsubscribe.groovy", "r")
App1 = open("Extractor/"+"App1/App1.groovy", "r")
extractedObjectsApp1 = open("Extractor/"+"App1/extractedObjectsApp1.groovy", "r")
extractedObjectsConstructorApp1 = open("Extractor/"+"App1/extractedObjectsConstructorApp1.groovy", "r")
extractedFunctionsApp1 = open("Extractor/"+"App1/extractedFunctionsApp1.groovy", "r")




Out.write("//Infrastructure for SmartThings Application\n")
Out.write("//Importing Libraries\n")
Out.write("import groovy.transform.Field\n")
Out.write("import groovy.json.JsonSlurper\n")
Out.write("\n")
Out.write("//Importing Classes\n")
Out.write("import ContactSensor.ContactSensor\n")
Out.write("import ContactSensor.ContactSensors\n")
Out.write("import DoorControl.DoorControl\n")
Out.write("import DoorControl.DoorControls\n")
Out.write("import Lock.Lock\n")
Out.write("import Lock.Locks\n")
Out.write("import Thermostat.Thermostat\n")
Out.write("import Thermostat.Thermostats\n")
Out.write("import Switch.Switch\n")
Out.write("import Switch.Switches\n")
Out.write("import PresenceSensor.PresenceSensor\n")
Out.write("import PresenceSensor.PresenceSensors\n")
Out.write("import Logger.Logger\n")
Out.write("import Location.LocationVar\n")
Out.write("import Location.Phrase\n")
Out.write("import appTouch.Touched\n")
Out.write("import NfcTouch.NfcTouch\n")
Out.write("import AeonKeyFob.AeonKeyFob\n")
Out.write("import AeonKeyFob.AeonKeyFobs\n")
Out.write("import MusicPlayer.MusicPlayer\n")
Out.write("import MusicPlayer.MusicPlayers\n")
Out.write("import MotionSensor.MotionSensor\n")
Out.write("import MotionSensor.MotionSensors\n")
Out.write("import ImageCapture.ImageCapture\n")
Out.write("import ImageCapture.ImageCaptures\n")
Out.write("import SmokeDetector.SmokeDetector\n")
Out.write("import SmokeDetector.SmokeDetectors\n")
Out.write("import Alarm.Alarm\n")
Out.write("import Alarm.Alarms\n")
Out.write("import SpeechSynthesis.SpeechSynthesis\n")
Out.write("import SpeechSynthesis.SpeechSynthesises\n")
Out.write("import AccelerationSensor.AccelerationSensor\n")
Out.write("import AccelerationSensor.AccelerationSensors\n")
Out.write("import Battery.Battery\n")
Out.write("import Battery.Batteries\n")
Out.write("import BeaconSensor.BeaconSensor\n")
Out.write("import BeaconSensor.BeaconSensors\n")
Out.write("import CarbonMonoxideDetector.CarbonMonoxideDetector\n")
Out.write("import CarbonMonoxideDetector.CarbonMonoxideDetectors\n")
Out.write("import ColorControl.ColorControl\n")
Out.write("import ColorControl.ColorControls\n")
Out.write("import EnergyMeter.EnergyMeter\n")
Out.write("import EnergyMeter.EnergyMeters\n")
Out.write("import IlluminanceMeasurement.IlluminanceMeasurement\n")
Out.write("import IlluminanceMeasurement.IlluminanceMeasurements\n")
Out.write("import PowerMeter.PowerMeter\n")
Out.write("import PowerMeter.PowerMeters\n")
Out.write("import RelativeHumidityMeasurement.RelativeHumidityMeasurement\n")
Out.write("import RelativeHumidityMeasurement.RelativeHumidityMeasurements\n")
Out.write("import RelaySwitch.RelaySwitch\n")
Out.write("import RelaySwitch.RelaySwitches\n")
Out.write("import SleepSensor.SleepSensor\n")
Out.write("import SleepSensor.SleepSensors\n")
Out.write("import StepSensor.StepSensor\n")
Out.write("import StepSensor.StepSensors\n")
Out.write("import SwitchLevel.SwitchLevel\n")
Out.write("import SwitchLevel.SwitchLevels\n")
Out.write("import TemperatureMeasurement.TemperatureMeasurement\n")
Out.write("import TemperatureMeasurement.TemperatureMeasurements\n")
Out.write("import WaterSensor.WaterSensor\n")
Out.write("import WaterSensor.WaterSensors\n")
Out.write("import Valve.Valve\n")
Out.write("import Valve.Valves\n")
Out.write("import MobilePresence.MobilePresence\n")
Out.write("import MobilePresence.MobilePresences\n")
Out.write("import ColorTemperature.ColorTemperature\n")
Out.write("import ColorTemperature.ColorTemperatures\n")
Out.write("import Button.Button\n")
Out.write("import Button.Buttons\n")
Out.write("import ThreeAxis.ThreeAxis\n")
Out.write("import ThreeAxis.ThreeAxises\n")
Out.write("import Momentary.Momentary\n")
Out.write("import Momentary.Momentaries\n")
Out.write("import RemainingDevices.RemainingDevices\n")
Out.write("import Event.Event\n")
Out.write("import Timer.SimulatedTimer\n")
Out.write("\n")
Out.write("//JPF's Verify API\n")
Out.write("import gov.nasa.jpf.vm.Verify\n")
Out.write("\n")
Out.write("//Global eventHandler\n")
for line in eventHandler:
	Out.write(line)
Out.write("\n")
Out.write("//GlobalVariables for both Apps\n")
Out.write("@Field def init = Verify.getBoolean()\n\n")
for line in GlobalVariablesBothApps:
	Out.write(line)
Out.write("@Field def previousValue = false\n")
Out.write("\n")
Out.write("//clear init flag now we are done with it\n")
Out.write("init = false\n")
Out.write("//Application #1\n")
Out.write("class App1 {\n")
Out.write("\tdef reference\n")
Out.write("\tdef location\n")
Out.write("\t// A local variable added for conflict detection tool\n")
Out.write("\tdef location_mode\n")
Out.write("\tdef app\n")
Out.write("\n")
Out.write("\t//Extracted objects for App1\n")
for line in extractedObjectsApp1:
	Out.write("\t"+line)
Out.write("\n")
Out.write("\t//Extracted objects for functions for App1\n")
for line in extractedFunctionsApp1:
	Out.write("\t"+line)
Out.write("\n")
Out.write("\tApp1(Object obj) {\n")
Out.write("\t\treference = obj\n")
Out.write("\t\tlocation = obj.locationObject\n")
Out.write("\t\tapp = obj.appObject\n")
for line in extractedObjectsConstructorApp1:
	Out.write("\t\t"+line)
Out.write("\t}\n")
Out.write("\t//Global variables for each app\n")
for line in GlobalVariablesEachApp:
	Out.write("\t"+line)
Out.write("\n")
Out.write("\t//Methods\n")
for line in setLocationMode:
	Out.write("\t"+line)
for line in subscribe:
	Out.write("\t"+line)
for line in runIn:
	Out.write("\t"+line)
for line in runDaily:
	Out.write("\t"+line)
for line in unschedule:
	Out.write("\t"+line)
for line in sendNotificationToContacts:
	Out.write("\t"+line)
for line in sendSms:
	Out.write("\t"+line)
for line in sendPush:
	Out.write("\t"+line)
for line in schedule:
	Out.write("\t"+line)
for line in now:
	Out.write("\t"+line)
for line in getTemperatureScale:
	Out.write("\t"+line)
for line in getSunriseAndSunset:
	Out.write("\t"+line)
for line in httpPostJson:
	Out.write("\t"+line)
for line in runEvery15Minutes:
	Out.write("\t"+line)
for line in timeToday:
	Out.write("\t"+line)
for line in sendNotification:
	Out.write("\t"+line)
for line in canSchedule:
	Out.write("\t"+line)
for line in createAccessToken:
	Out.write("\t"+line)
for line in runOnce:
	Out.write("\t"+line)
for line in parseJson:
	Out.write("\t"+line)
for line in unsubscribe:
	Out.write("\t"+line)
Out.write("\n")
Start = 0
for line in App1:
	if ("def " in line):
		Start = 1
	if (Start):
		Out.write("\t"+line)
Out.write("}\n")
Out.write("\n")
Out.write("\n")
GlobalVariablesEachApp = open("GlobalVariables/"+"GlobalVariablesEachApp.groovy", "r")

#For App2
GlobalVariablesEachApp = open("GlobalVariables/"+"GlobalVariablesEachApp.groovy", "r")
setLocationMode = open("Methods/"+"setLocationMode.groovy", "r")
subscribe = open("Methods/"+"subscribe.groovy", "r")
runIn = open("Methods/"+"runIn.groovy", "r")
runDaily = open("Methods/"+"runDaily.groovy", "r")
unschedule = open("Methods/"+"unschedule.groovy", "r")
sendNotificationToContacts = open("Methods/"+"sendNotificationToContacts.groovy", "r")
sendSms = open("Methods/"+"sendSms.groovy", "r")
sendPush = open("Methods/"+"sendPush.groovy", "r")
eventHandler = open("Methods/"+"eventHandler.groovy", "r")
schedule = open("Methods/"+"schedule.groovy", "r")
now = open("Methods/"+"now.groovy", "r")
getTemperatureScale = open("Methods/"+"getTemperatureScale.groovy", "r")
getSunriseAndSunset = open("Methods/"+"getSunriseAndSunset.groovy", "r")
httpPostJson = open("Methods/"+"httpPostJson.groovy", "r")
runEvery15Minutes = open("Methods/"+"runEvery15Minutes.groovy", "r")
timeToday = open("Methods/"+"timeToday.groovy", "r")
sendNotification = open("Methods/"+"sendNotification.groovy", "r")
canSchedule = open("Methods/"+"canSchedule.groovy", "r")
createAccessToken = open("Methods/"+"createAccessToken.groovy", "r")
runOnce = open("Methods/"+"runOnce.groovy", "r")
parseJson = open("Methods/"+"parseJson.groovy", "r")
unsubscribe = open("Methods/"+"unsubscribe.groovy", "r")
App2 = open("Extractor/"+"App2/App2.groovy", "r")
extractedObjectsApp2 = open("Extractor/"+"App2/extractedObjectsApp2.groovy", "r")
extractedObjectsConstructorApp2 = open("Extractor/"+"App2/extractedObjectsConstructorApp2.groovy", "r")
extractedFunctionsApp2 = open("Extractor/"+"App2/extractedFunctionsApp2.groovy", "r")

Out.write("//Application #2\n")
Out.write("class App2 {\n")
Out.write("\tdef reference\n")
Out.write("\tdef location\n")
Out.write("\t// A local variable added for conflict detection tool\n")
Out.write("\tdef location_mode\n")
Out.write("\tdef app\n")
Out.write("\n")
Out.write("\t//Extracted objects for App2\n")
for line in extractedObjectsApp2:
	Out.write("\t"+line)
Out.write("\n")
Out.write("\t//Extracted objects for functions for App2\n")
for line in extractedFunctionsApp2:
	Out.write("\t"+line)
Out.write("\n")
Out.write("\tApp2(Object obj) {\n")
Out.write("\t\treference = obj\n")
Out.write("\t\tlocation = obj.locationObject\n")
Out.write("\t\tapp = obj.appObject\n")
for line in extractedObjectsConstructorApp2:
	Out.write("\t\t"+line)
Out.write("\t}\n")
Out.write("\t//Global variables for each app\n")
for line in GlobalVariablesEachApp:
	Out.write("\t"+line)
Out.write("\n")
Out.write("\t//Methods\n")
for line in setLocationMode:
	Out.write("\t"+line)
for line in subscribe:
	Out.write("\t"+line)
for line in runIn:
	Out.write("\t"+line)
for line in runDaily:
	Out.write("\t"+line)
for line in unschedule:
	Out.write("\t"+line)
for line in sendNotificationToContacts:
	Out.write("\t"+line)
for line in sendSms:
	Out.write("\t"+line)
for line in sendPush:
	Out.write("\t"+line)
for line in schedule:
	Out.write("\t"+line)
for line in now:
	Out.write("\t"+line)
for line in getTemperatureScale:
	Out.write("\t"+line)
for line in getSunriseAndSunset:
	Out.write("\t"+line)
for line in httpPostJson:
	Out.write("\t"+line)
for line in runEvery15Minutes:
	Out.write("\t"+line)
for line in timeToday:
	Out.write("\t"+line)
for line in sendNotification:
	Out.write("\t"+line)
for line in canSchedule:
	Out.write("\t"+line)
for line in createAccessToken:
	Out.write("\t"+line)
for line in runOnce:
	Out.write("\t"+line)
for line in parseJson:
	Out.write("\t"+line)
for line in unsubscribe:
	Out.write("\t"+line)
Out.write("\n")
Start = 0
for line in App2:
	if ("def " in line):
		Start = 1
	if (Start):
		Out.write("\t"+line)
Out.write("}\n")
Out.write("\n")
Out.write("@Field def app1\n")
Out.write("@Field def app2\n")
Out.write("//def initOrder = Verify.getBoolean()\n")
Out.write("//if (initOrder) {\n")
Out.write("\tapp1 = new App1(this)\n")
Out.write("\tapp2 = new App2(this)\n")
Out.write("//} else {\n")
Out.write("\t//app2 = new App2(this)\n")
Out.write("\t//app1 = new App1(this)\n")
Out.write("//}\n\n")
Out.write("//def installOrder = Verify.getBoolean()\n")
Out.write("//if (installOrder) {\n")
Out.write("\tapp1.installed()\n")
Out.write("\tapp2.installed()\n")
Out.write("//} else {\n")
Out.write("\t//app2.installed()\n")
Out.write("\t//app1.installed()\n")
Out.write("//}\n\n")
for line in eventSimulator:
	Out.write(line)
Out.close()




