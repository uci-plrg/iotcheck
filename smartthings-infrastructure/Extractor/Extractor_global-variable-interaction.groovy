////////////////////////////////////////
//import libraries
import groovy.transform.Field


//import classes
//Importing Classes
import ContactSensor.ContactSensor
import ContactSensor.ContactSensors
import DoorControl.DoorControl
import DoorControl.DoorControls
import Lock.Lock
import Lock.Locks
import Thermostat.Thermostat
import Thermostat.Thermostats
import Switch.Switch
import Switch.Switches
import PresenceSensor.PresenceSensor
import PresenceSensor.PresenceSensors
import Logger.Logger
import Location.LocationVar
import Location.Phrase
import appTouch.Touched
import NfcTouch.NfcTouch
import AeonKeyFob.AeonKeyFob
import AeonKeyFob.AeonKeyFobs
import MusicPlayer.MusicPlayer
import MusicPlayer.MusicPlayers
import MotionSensor.MotionSensor
import MotionSensor.MotionSensors
import ImageCapture.ImageCapture
import ImageCapture.ImageCaptures
import SmokeDetector.SmokeDetector
import SmokeDetector.SmokeDetectors
import Alarm.Alarm
import Alarm.Alarms
import SpeechSynthesis.SpeechSynthesis
import SpeechSynthesis.SpeechSynthesises
import AccelerationSensor.AccelerationSensor
import AccelerationSensor.AccelerationSensors
import Battery.Battery
import Battery.Batteries
import BeaconSensor.BeaconSensor
import BeaconSensor.BeaconSensors
import CarbonMonoxideDetector.CarbonMonoxideDetector
import CarbonMonoxideDetector.CarbonMonoxideDetectors
import ColorControl.ColorControl
import ColorControl.ColorControls
import EnergyMeter.EnergyMeter
import EnergyMeter.EnergyMeters
import IlluminanceMeasurement.IlluminanceMeasurement
import IlluminanceMeasurement.IlluminanceMeasurements
import PowerMeter.PowerMeter
import PowerMeter.PowerMeters
import RelativeHumidityMeasurement.RelativeHumidityMeasurement
import RelativeHumidityMeasurement.RelativeHumidityMeasurements
import RelaySwitch.RelaySwitch
import RelaySwitch.RelaySwitches
import SleepSensor.SleepSensor
import SleepSensor.SleepSensors
import StepSensor.StepSensor
import StepSensor.StepSensors
import SwitchLevel.SwitchLevel
import SwitchLevel.SwitchLevels
import TemperatureMeasurement.TemperatureMeasurement
import TemperatureMeasurement.TemperatureMeasurements
import WaterSensor.WaterSensor
import WaterSensor.WaterSensors
import Valve.Valve
import Valve.Valves
import MobilePresence.MobilePresence
import MobilePresence.MobilePresences
import ColorTemperature.ColorTemperature
import ColorTemperature.ColorTemperatures
import Button.Button
import Button.Buttons
import ThreeAxis.ThreeAxis
import ThreeAxis.ThreeAxises
import Momentary.Momentary
import Momentary.Momentaries
import RemainingDevices.RemainingDevices
import Timer.SimulatedTimer

//GlobalVariables
@Field def location = new LocationVar({}, true)
//Settings variable defined to settings on purpose
@Field def settings = [app: "app"]
//Global variable for state[mode]
@Field def state = [home:[],away:[],night:[]]
//Create a global logger object for methods
@Field def log = new Logger()
//Create a global variable for optional property
@Field def optional = false //by default for now
//Global Object for class AtomicState!
@Field def atomicState = [history: "15:00"]


//Global variables for files
@Field File globalObjects = new File("Extractor/globalObjects.groovy")
@Field File extractedObjectsApp1 = new File("Extractor/App1/extractedObjectsApp1.groovy")
@Field File extractedObjectsApp2 = new File("Extractor/App2/extractedObjectsApp2.groovy")
@Field File extractedObjectsConstructorApp1 = new File("Extractor/App1/extractedObjectsConstructorApp1.groovy")
@Field File extractedObjectsConstructorApp2 = new File("Extractor/App2/extractedObjectsConstructorApp2.groovy")

//Set this to true for global-state variable conflict
@Field assignDifferentModes = true
@Field chooseMode = 0
//Empty the files
if (App == "App1") {
	globalObjects.write("")
	extractedObjectsApp1.write("")
	extractedObjectsConstructorApp1.write("")
	if (assignDifferentModes)
		chooseMode = 0
} else if (App == "App2") {
	extractedObjectsApp2.write("")
	extractedObjectsConstructorApp2.write("")
	if (assignDifferentModes)
		chooseMode = 2
}



//Some of methods-May be needed even in install
/////////////////////////////////////////////////////////////////////
def timeToday(String time, Object timeZone) {
	def timeOfDay = new Date()
	def _inputTime = time.split(':')
	def inputTime = Integer.parseInt(_inputTime[0])*3600+Integer.parseInt(_inputTime[1])*60+1564191100415
	timeOfDay.time = inputTime
	return timeOfDay
}



//Global objects
//Global Object for class Touch Sensor!
@Field touchSensorObjects = 0
@Field def touchSensorObject0
@Field def touchSensorObject1
@Field def touchSensorObject2
//Global Object for class switch!
@Field switchObjects = 0
@Field def switchObject0
@Field def switchObject1
@Field def switchObject2
//Global Object for class lock!
@Field lockObjects = 0
@Field def lockObject0
@Field def lockObject1
@Field def lockObject2
//Global Object for class door control!
@Field doorControlObjects = 0
@Field def doorControlObject0
@Field def doorControlObject1
@Field def doorControlObject2
//Global Object for class contact sensor!
@Field contactObjects = 0
@Field def contactObject0
@Field def contactObject1
@Field def contactObject2
//Global Object for class presence sensor!
@Field presenceSensorObjects = 0
@Field def presenceSensorObject0
@Field def presenceSensorObject1
@Field def presenceSensorObject2
//Global Object for class thermostat!
@Field thermostatObjects = 0
@Field def thermostatObject0
@Field def thermostatObject1
@Field def thermostatObject2
//Global Object for class music player!
@Field musicPlayerObjects = 0
@Field def musicPlayerObject0
@Field def musicPlayerObject1
@Field def musicPlayerObject2
//Global Object for class aeon key fob!
@Field aeonKeyFobObjects = 0
@Field def aeonKeyFobObject0
@Field def aeonKeyFobObject1
@Field def aeonKeyFobObject2
//Global Object for class motion sensor!
@Field motionSensorObjects = 0
@Field def motionSensorObject0
@Field def motionSensorObject1
@Field def motionSensorObject2
//Global Object for class image capture!
@Field imageCaptureObjects = 0
@Field def imageCaptureObject0
@Field def imageCaptureObject1
@Field def imageCaptureObject2
//Global Object for class smoke detector!
@Field smokeDetectorObjects = 0
@Field def smokeDetectorObject0
@Field def smokeDetectorObject1
@Field def smokeDetectorObject2
//Global Object for class alarm!
@Field alarmObjects = 0
@Field def alarmObject0
@Field def alarmObject1
@Field def alarmObject2
//Global Object for class speech synthesis!
@Field speechSynthesisObjects = 0
@Field def speechSynthesisObject0
@Field def speechSynthesisObject1
@Field def speechSynthesisObject2
//Global Object for class acceleration sensor!
@Field accelerationSensorObjects = 0
@Field def accelerationSensorObject0
@Field def accelerationSensorObject1
@Field def accelerationSensorObject2
//Global Object for class battery!
@Field batteryObjects = 0
@Field def batteryObject0
@Field def batteryObject1
@Field def batteryObject2
//Global Object for class beacon sensor!
@Field beaconSensorObjects = 0
@Field def beaconSensorObject0
@Field def beaconSensorObject1
@Field def beaconSensorObject2
//Global Object for class carbon monoxide detector!
@Field carbonMonoxideDetectorObjects = 0
@Field def carbonMonoxideDetectorObject0
@Field def carbonMonoxideDetectorObject1
@Field def carbonMonoxideDetectorObject2
//Global Object for class color control!
@Field colorControlObjects = 0
@Field def colorControlObject0
@Field def colorControlObject1
@Field def colorControlObject2
//Global Object for class energy meter!
@Field energyMeterObjects = 0
@Field def energyMeterObject0
@Field def energyMeterObject1
@Field def energyMeterObject2
//Global Object for class energy meter!
@Field illuminanceMeasurementObjects = 0
@Field def illuminanceMeasurementObject0
@Field def illuminanceMeasurementObject1
@Field def illuminanceMeasurementObject2
//Global Object for class power meter!
@Field powerMeterObjects = 0
@Field def powerMeterObject0
@Field def powerMeterObject1
@Field def powerMeterObject2
//Global Object for class power meter!
@Field humidityMeasurementObjects = 0
@Field def humidityMeasurementObject0
@Field def humidityMeasurementObject1
@Field def humidityMeasurementObject2
//Global Object for class relay switch!
@Field relaySwitchObjects = 0
@Field def relaySwitchObject0
@Field def relaySwitchObject1
@Field def relaySwitchObject2
//Global Object for class sleep sensor!
@Field sleepSensorObjects = 0
@Field def sleepSensorObject0
@Field def sleepSensorObject1
@Field def sleepSensorObject2
//Global Object for class step sensor!
@Field stepSensorObjects = 0
@Field def stepSensorObject0
@Field def stepSensorObject1
@Field def stepSensorObject2
//Global Object for class switch level!
@Field switchLevelObjects = 0
@Field def switchLevelObject0
@Field def switchLevelObject1
@Field def switchLevelObject2
//Global Object for class temperature measurement!
@Field temperatureMeasurementObjects = 0
@Field def temperatureMeasurementObject0
@Field def temperatureMeasurementObject1
@Field def temperatureMeasurementObject2
//Global Object for class water sensor!
@Field waterSensorObjects = 0
@Field def waterSensorObject0
@Field def waterSensorObject1
@Field def waterSensorObject2
//Global Object for class valve!
@Field valveObjects = 0
@Field def valveObject0
@Field def valveObject1
@Field def valveObject2
//Global Object for class mobile presence!
@Field mobilePresenceObjects = 0
@Field def mobilePresenceObject0
@Field def mobilePresenceObject1
@Field def mobilePresenceObject2
//Global Object for class color temperature!
@Field colorTemperatureObjects = 0
@Field def colorTemperatureObject0
@Field def colorTemperatureObject1
@Field def colorTemperatureObject2
//Global Object for class button!
@Field buttonObjects = 0
@Field def buttonObject0
@Field def buttonObject1
@Field def buttonObject2
//Global Object for class three axis!
@Field threeAxisObjects = 0
@Field def threeAxisObject0
@Field def threeAxisObject1
@Field def threeAxisObject2
//Global Object for class momentary switch device!
@Field momentaryObjects = 0
@Field def momentaryObject0
@Field def momentaryObject1
@Field def momentaryObject2


//Global variables
//For mode
@Field modeVariables = 0
@Field mode0
@Field mode1
@Field mode2
@Field mode3
@Field mode4
@Field mode5
//For number
@Field numberVariables = 0
@Field number0
@Field number1
@Field number2
@Field number3
@Field number4
@Field number5
//For decimal
@Field decimalVariables = 0
@Field decimal0
@Field decimal1
@Field decimal2
@Field decimal3
@Field decimal4
@Field decimal5
//For time
@Field timeVariables = 0
@Field time0
@Field time1
@Field time2
@Field time3
@Field time4
@Field time5
//For enum
@Field enumVariables = 0
@Field enum0
@Field enum1
@Field enum2
@Field enum3
@Field enum4
@Field enum5
//For phone
@Field phoneVariables = 0
@Field phone0
@Field phone1
@Field phone2
@Field phone3
@Field phone4
@Field phone5
//For contact
@Field contactVariables = 0
@Field contact0
@Field contact1
@Field contact2
@Field contact3
@Field contact4
@Field contact5
//For text
@Field textVariables = 0
@Field textVariable0
@Field textVariable1
@Field textVariable2
@Field textVariable3
@Field textVariable4
@Field textVariable5
//For boolean
@Field boolVariables = 0
@Field boolVariable0
@Field boolVariable1
@Field boolVariable2
@Field boolVariable3
@Field boolVariable4
@Field boolVariable5

/////Input Methods/////
//input "",""
def input(String name, String type) {
	LinkedHashMap metaData = []
	metaData.put('name',name)
	metaData.put('type',type)
	input(metaData)
}

//input "","",linkedHashMap
def input(LinkedHashMap metaData, String name, String type) {
	metaData.put('name',name)
	metaData.put('type',type)
	input(metaData)
}

//input "", "", linkedHashMap, Closure
def input(LinkedHashMap metaData, String name, String type, Closure Input) {
	metaData.put('name',name)
	metaData.put('type',type)
	input(metaData)
	find(Input)
}

//input linkedHashMap
def input(LinkedHashMap metaData) {
	if (metaData.containsKey('title')) {
		println metaData['title']
	}
	if (metaData.containsKey('options')) {
		println "Options: "+metaData['options']
	}
	def contains = 0
	def List = ["capability.carbonDioxideMeasurement", "capability.consumable", "capability.pHMeasurement", "capability.shockSensor", 
		    "capability.signalStrength", "capability.soundSensor", "capability.soundPressureLevel", "capability.tamperAlert",
		    "capability.ultravioletIndex", "capability.voltageMeasurement", "capability.windowShade", "capability.sensor"]
	String thisCapability = ""	
	if (metaData['type'] in List) {
		int dot = metaData['type'].indexOf('.')
		thisCapability = metaData['type'].substring(dot + 1)
		metaData['type'] = "capability.remainingDevices"
	}
	switch(metaData['type']) {
		case "capability.lock":
			globalObjects.eachLine { line ->
				if(line.contains("lockObject")){
					contains = 1
			    }
			}

			if (contains == 0)
				globalObjects.append("@Field def lockObject = new Locks(sendEvent, 1, init)\n")

			if (lockObjects == 0) {
				lockObject0 = metaData['name']
				this[lockObject0] = new Locks({}, 1, true)
			} else if (lockObjects == 1) {
				lockObject1 = metaData['name']
				this[lockObject1] = new Locks({}, 1, true)
			} else if (lockObjects == 2) {
				lockObject2 = metaData['name']
				this[lockObject2] = new Locks({}, 1, true)
			}

			lockObjects=lockObjects+1

			settings.put(metaData['name'], new Locks({}, 1, true)) 

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class lock!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.lockObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class lock!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.lockObject\n")
			}
			break
		case "capability.alarm":
			globalObjects.eachLine { line ->
				if(line.contains("alarmObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def alarmObject = new Alarms(sendEvent, 1, init)\n")

			if (alarmObjects == 0) {
				alarmObject0 = metaData['name']
				this[alarmObject0] = new Alarms({}, 1, true)
			} else if (alarmObjects == 1) {
				alarmObject1 = metaData['name']
				this[alarmObject1] = new Alarms({}, 1, true)
			} else if (alarmObjects == 2) {
				alarmObject2 = metaData['name']
				this[alarmObject2] = new Alarms({}, 1, true)
			}

			alarmObjects=alarmObjects+1

			settings.put(metaData['name'], new Alarms({}, 1, true)) 

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class alarm!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.alarmObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class alarm!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.alarmObject\n")
			}
			break
		case "capability.button":
			globalObjects.eachLine { line ->
				if(line.contains("buttonObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def buttonObject = new Buttons(sendEvent, 1, init)\n")

			if (buttonObjects == 0) {
				buttonObject0 = metaData['name']
				this[buttonObject0] = new Buttons({}, 1, true)
			} else if (buttonObjects == 1) {
				buttonObject1 = metaData['name']
				this[buttonObject1] = new Buttons({}, 1, true)
			} else if (buttonObjects == 2) {
				buttonObject2 = metaData['name']
				this[buttonObject2] = new Buttons({}, 1, true)
			}

			buttonObjects=buttonObjects+1

			settings.put(metaData['name'], new Buttons({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class Button!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.buttonObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class Button!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.buttonObject\n")
			}
			break
		case "capability.battery":
			globalObjects.eachLine { line ->
				if(line.contains("batteryObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def batteryObject = new Batteries(sendEvent, 1, init)\n")

			if (batteryObjects == 0) {
				batteryObject0 = metaData['name']
				this[batteryObject0] = new Batteries({}, 1, true)
			} else if (batteryObjects == 1) {
				batteryObject1 = metaData['name']
				this[batteryObject1] = new Batteries({}, 1, true)
			} else if (batteryObjects == 2) {
				batteryObject2 = metaData['name']
				this[batteryObject2] = new Batteries({}, 1, true)
			}

			batteryObjects=batteryObjects+1

			settings.put(metaData['name'], new Batteries({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class Battery!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.batteryObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class Battery!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.batteryObject\n")
			}
			break
		case "capability.beacon":
			globalObjects.eachLine { line ->
				if(line.contains("beaconSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def beaconSensorObject = new BeaconSensors(sendEvent, 1, init)\n")

			if (beaconSensorObjects == 0) {
				beaconSensorObject0 = metaData['name']
				this[beaconSensorObject0] = new BeaconSensors({}, 1, true)
			} else if (beaconSensorObjects == 1) {
				beaconSensorObject1 = metaData['name']
				this[beaconSensorObject1] = new BeaconSensors({}, 1, true)
			} else if (beaconSensorObjects == 2) {
				beaconSensorObject2 = metaData['name']
				this[beaconSensorObject2] = new BeaconSensors({}, 1, true)
			}

			beaconSensorObjects=beaconSensorObjects+1

			settings.put(metaData['name'], new BeaconSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class beacon sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.beaconSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class beacon sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.beaconSensorObject\n")
			}
			break
		case "capability.carbonMonoxideDetector":
			globalObjects.eachLine { line ->
				if(line.contains("carbonMonoxideDetectorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def carbonMonoxideDetectorObject = new CarbonMonoxideDetectors(sendEvent, 1, init)\n")

			if (carbonMonoxideDetectorObjects == 0) {
				carbonMonoxideDetectorObject0 = metaData['name']
				this[carbonMonoxideDetectorObject0] = new CarbonMonoxideDetectors({}, 1, true)
			} else if (carbonMonoxideDetectorObjects == 1) {
				carbonMonoxideDetectorObject1 = metaData['name']
				this[carbonMonoxideDetectorObject1] = new CarbonMonoxideDetectors({}, 1, true)
			} else if (carbonMonoxideDetectorObjects == 2) {
				carbonMonoxideDetectorObject2 = metaData['name']
				this[carbonMonoxideDetectorObject2] = new CarbonMonoxideDetectors({}, 1, true)
			}

			carbonMonoxideDetectorObjects=carbonMonoxideDetectorObjects+1
			
			settings.put(metaData['name'], new CarbonMonoxideDetectors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class carbon monoxide detector!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.carbonMonoxideDetectorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class carbon monoxide detector!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.carbonMonoxideDetectorObject\n")
			}
			break
		case "capability.colorTemperature":
			globalObjects.eachLine { line ->
				if(line.contains("colorTemperatureObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def colorTemperatureObject = new ColorTemperatures(sendEvent, 1, init)\n")

			if (colorTemperatureObjects == 0) {
				colorTemperatureObject0 = metaData['name']
				this[colorTemperatureObject0] = new ColorTemperatures({}, 1, true)
			} else if (colorTemperatureObjects == 1) {
				colorTemperatureObject1 = metaData['name']
				this[colorTemperatureObject1] = new ColorTemperatures({}, 1, true)
			} else if (colorTemperatureObjects == 2) {
				colorTemperatureObject2 = metaData['name']
				this[colorTemperatureObject2] = new ColorTemperatures({}, 1, true)
			}

			colorTemperatureObjects=colorTemperatureObjects+1

			settings.put(metaData['name'], new ColorTemperatures({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class color temperature!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.colorTemperatureObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class color control!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.colorTemperatureObject\n")
			}
			break
		case "capability.colorControl":
			globalObjects.eachLine { line ->
				if(line.contains("colorControlObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def colorControlObject = new ColorControls(sendEvent, 1, init)\n")

			if (colorControlObjects == 0) {
				colorControlObject0 = metaData['name']
				this[colorControlObject0] = new ColorControls({}, 1, true)
			} else if (colorControlObjects == 1) {
				colorControlObject1 = metaData['name']
				this[colorControlObject1] = new ColorControls({}, 1, true)
			} else if (colorControlObjects == 2) {
				colorControlObject2 = metaData['name']
				this[colorControlObject2] = new ColorControls({}, 1, true)
			}

			colorControlObjects=colorControlObjects+1

			settings.put(metaData['name'], new ColorControls({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class color control!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.colorControlObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class color control!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.colorControlObject\n")
			}
			break
		case "capability.contactSensor":
			globalObjects.eachLine { line ->
				if(line.contains("contactObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def contactObject = new ContactSensors(sendEvent, 1, init)\n")

			if (contactObjects == 0) {
				contactObject0 = metaData['name']
				this[contactObject0] = new ContactSensors({}, 1, true)
			} else if (contactObjects == 1) {
				contactObject1 = metaData['name']
				this[contactObject1] = new ContactSensors({}, 1, true)
			} else if (contactObjects == 2) {
				contactObject2 = metaData['name']
				this[contactObject2] = new ContactSensors({}, 1, true)
			}

			contactObjects=contactObjects+1

			settings.put(metaData['name'], new ContactSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class contactSensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.contactObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class contactSensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.contactObject\n")
			}
			break
		case "capability.threeAxis":
			globalObjects.eachLine { line ->
				if(line.contains("threeAxisObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def threeAxisObject = new ThreeAxises(sendEvent, 1, init)\n")

			if (threeAxisObjects == 0) {
				threeAxisObject0 = metaData['name']
				this[threeAxisObject0] = new ThreeAxises({}, 1, true)
			} else if (threeAxisObjects == 1) {
				threeAxisObject1 = metaData['name']
				this[threeAxisObject1] = new ThreeAxises({}, 1, true)
			} else if (threeAxisObjects == 2) {
				threeAxisObject2 = metaData['name']
				this[threeAxisObject2] = new ThreeAxises({}, 1, true)
			}

			threeAxisObjects=threeAxisObjects+1

			settings.put(metaData['name'], new ThreeAxises({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class three axis!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.threeAxisObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class three axis!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.threeAxisObject\n")
			}
			break
		case "capability.doorControl":
			globalObjects.eachLine { line ->
				if(line.contains("doorControlObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def doorControlObject = new DoorControls(sendEvent, 1, init)\n")

			if (doorControlObjects == 0) {
				doorControlObject0 = metaData['name']
				this[doorControlObject0] = new DoorControls({}, 1, true)
			} else if (doorControlObjects == 1) {
				doorControlObject1 = metaData['name']
				this[doorControlObject1] = new DoorControls({}, 1, true)
			} else if (doorControlObjects == 2) {
				doorControlObject2 = metaData['name']
				this[doorControlObject2] = new DoorControls({}, 1, true)
			}

			doorControlObjects=doorControlObjects+1

			settings.put(metaData['name'], new DoorControls({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class door control!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.doorControlObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class door control!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.doorControlObject\n")
			}
			break
		case "capability.energyMeter":
			globalObjects.eachLine { line ->
				if(line.contains("energyMeterObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def energyMeterObject = new EnergyMeters(sendEvent, 1, init)\n")

			if (energyMeterObjects == 0) {
				energyMeterObject0 = metaData['name']
				this[energyMeterObject0] = new EnergyMeters({}, 1, true)
			} else if (energyMeterObjects == 1) {
				energyMeterObject1 = metaData['name']
				this[energyMeterObject1] = new EnergyMeters({}, 1, true)
			} else if (energyMeterObjects == 2) {
				energyMeterObject2 = metaData['name']
				this[energyMeterObject2] = new EnergyMeters({}, 1, true)
			}

			energyMeterObjects=energyMeterObjects+1

			settings.put(metaData['name'], new EnergyMeters({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class energy meter!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.energyMeterObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class energy meter!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.energyMeterObject\n")
			}
			break
		case "capability.illuminanceMeasurement":
			globalObjects.eachLine { line ->
				if(line.contains("illuminanceMeasurementObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def illuminanceMeasurementObject = new IlluminanceMeasurements(sendEvent, 1, init)\n")

			if (illuminanceMeasurementObjects == 0) {
				illuminanceMeasurementObject0 = metaData['name']
				this[illuminanceMeasurementObject0] = new IlluminanceMeasurements({}, 1, true)
			} else if (illuminanceMeasurementObjects == 1) {
				illuminanceMeasurementObject1 = metaData['name']
				this[illuminanceMeasurementObject1] = new IlluminanceMeasurements({}, 1, true)
			} else if (illuminanceMeasurementObjects == 2) {
				illuminanceMeasurementObject2 = metaData['name']
				this[illuminanceMeasurementObject2] = new IlluminanceMeasurements({}, 1, true)
			}

			illuminanceMeasurementObjects=illuminanceMeasurementObjects+1

			settings.put(metaData['name'], new IlluminanceMeasurements({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class illuminance measurement!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.illuminanceMeasurementObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class illuminance measurement!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.illuminanceMeasurementObject\n")
			}
			break
		case "capability.accelerationSensor":
			globalObjects.eachLine { line ->
				if(line.contains("accelerationSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def accelerationSensorObject = new AccelerationSensors(sendEvent, 1, init)\n")

			if (accelerationSensorObjects == 0) {
				accelerationSensorObject0 = metaData['name']
				this[accelerationSensorObject0] = new AccelerationSensors({}, 1, true)
			} else if (accelerationSensorObjects == 1) {
				accelerationSensorObject1 = metaData['name']
				this[accelerationSensorObject1] = new AccelerationSensors({}, 1, true)
			} else if (accelerationSensorObjects == 2) {
				accelerationSensorObject2 = metaData['name']
				this[accelerationSensorObject2] = new AccelerationSensors({}, 1, true)
			}

			accelerationSensorObjects=accelerationSensorObjects+1

			settings.put(metaData['name'], new AccelerationSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class Acceleration Sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.accelerationSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class Acceleration Sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.accelerationSensorObject\n")
			}
			break
		case "capability.momentary":
			globalObjects.eachLine { line ->
				if(line.contains("momentoryObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def momentaryObject = new Momentaries(sendEvent, 1)\n")

			if (momentaryObjects == 0) {
				momentaryObject0 = metaData['name']
				this[momentaryObject0] = new Momentaries({}, 1)
			} else if (momentaryObjects == 1) {
				momentaryObject1 = metaData['name']
				this[momentaryObject1] = new Momentaries({}, 1)
			} else if (momentaryObjects == 2) {
				momentaryObject2 = metaData['name']
				this[momentaryObject2] = new Momentaries({}, 1)
			}

			momentaryObjects=momentaryObjects+1

			settings.put(metaData['name'], new Momentaries({}, 1))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class momentory switch class!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.momentaryObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class momentory Sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.momentaryObject\n")
			}			
			break
		case "capability.motionSensor":
			globalObjects.eachLine { line ->
				if(line.contains("motionSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def motionSensorObject = new MotionSensors(sendEvent, 1, init)\n")

			if (motionSensorObjects == 0) {
				motionSensorObject0 = metaData['name']
				this[motionSensorObject0] = new MotionSensors({}, 1, true)
			} else if (motionSensorObjects == 1) {
				motionSensorObject1 = metaData['name']
				this[motionSensorObject1] = new MotionSensors({}, 1, true)
			} else if (motionSensorObjects == 2) {
				motionSensorObject2 = metaData['name']
				this[motionSensorObject2] = new MotionSensors({}, 1, true)
			}

			motionSensorObjects=motionSensorObjects+1

			settings.put(metaData['name'], new MotionSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class Motion Sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.motionSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class Motion Sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.motionSensorObject\n")
			}
			break
		case "capability.musicPlayer":
			globalObjects.eachLine { line ->
				if(line.contains("musicPlayerObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def musicPlayerObject = new MusicPlayers(sendEvent, 1, init)\n")

			if (musicPlayerObjects == 0) {
				musicPlayerObject0 = metaData['name']
				this[musicPlayerObject0] = new MusicPlayers({}, 1, true)
			} else if (musicPlayerObjects == 1) {
				musicPlayerObject1 = metaData['name']
				this[musicPlayerObject1] = new MusicPlayers({}, 1, true)
			} else if (musicPlayerObjects == 2) {
				musicPlayerObject2 = metaData['name']
				this[musicPlayerObject2] = new MusicPlayers({}, 1, true)
			}

			musicPlayerObjects=musicPlayerObjects+1

			settings.put(metaData['name'], new MusicPlayers({}, 1, true)) 

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class music player!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.musicPlayerObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class music player!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.musicPlayerObject\n")
			}
			break
		case "capability.powerMeter":
			globalObjects.eachLine { line ->
				if(line.contains("powerMeterObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def powerMeterObject = new PowerMeters(sendEvent, 1, init)\n")

			if (powerMeterObjects == 0) {
				powerMeterObject0 = metaData['name']
				this[powerMeterObject0] = new PowerMeters({}, 1, true)
			} else if (powerMeterObjects == 1) {
				powerMeterObject1 = metaData['name']
				this[powerMeterObject1] = new PowerMeters({}, 1, true)
			} else if (powerMeterObjects == 2) {
				powerMeterObject2 = metaData['name']
				this[powerMeterObject2] = new PowerMeters({}, 1, true)
			}

			powerMeterObjects=powerMeterObjects+1

			settings.put(metaData['name'], new PowerMeters({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class power meter!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.powerMeterObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class power meter!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.powerMeterObject\n")
			}
			break
		case "capability.presenceSensor":
			globalObjects.eachLine { line ->
				if(line.contains("presenceSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def presenceSensorObject = new PresenceSensors(sendEvent, 1, init)\n")

			if (presenceSensorObjects == 0) {
				presenceSensorObject0 = metaData['name']
				this[presenceSensorObject0] = new PresenceSensors({}, 1, true)
			} else if (presenceSensorObjects == 1) {
				presenceSensorObject1 = metaData['name']
				this[presenceSensorObject1] = new PresenceSensors({}, 1, true)
			} else if (presenceSensorObjects == 2) {
				presenceSensorObject2 = metaData['name']
				this[presenceSensorObject2] = new PresenceSensors({}, 1, true)
			}

			presenceSensorObjects=presenceSensorObjects+1

			settings.put(metaData['name'], new PresenceSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class presence sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.presenceSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class presence sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.presenceSensorObject\n")
			}
			break
		case "capability.relativeHumidityMeasurement":
			globalObjects.eachLine { line ->
				if(line.contains("humidityMeasurementObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def humidityMeasurementObject = new RelativeHumidityMeasurements(sendEvent, 1, init)\n")

			if (humidityMeasurementObjects == 0) {
				humidityMeasurementObject0 = metaData['name']
				this[humidityMeasurementObject0] = new RelativeHumidityMeasurements({}, 1, true)
			} else if (humidityMeasurementObjects == 1) {
				humidityMeasurementObject1 = metaData['name']
				this[humidityMeasurementObject1] = new RelativeHumidityMeasurements({}, 1, true)
			} else if (humidityMeasurementObjects == 2) {
				humidityMeasurementObject2 = metaData['name']
				this[humidityMeasurementObject2] = new RelativeHumidityMeasurements({}, 1, true)
			}

			humidityMeasurementObjects=humidityMeasurementObjects+1

			settings.put(metaData['name'], new RelativeHumidityMeasurements({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class humidity measurement!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.humidityMeasurementObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class humidity measurement!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.humidityMeasurementObject\n")
			}
			break
		case "capability.relaySwitch":
			globalObjects.eachLine { line ->
				if(line.contains("relaySwitchObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def relaySwitchObject = new RelaySwitches(sendEvent, 1, init)\n")

			if (relaySwitchObjects == 0) {
				relaySwitchObject0 = metaData['name']
				this[relaySwitchObject0] = new RelaySwitches({}, 1, true)
			} else if (relaySwitchObjects == 1) {
				relaySwitchObject1 = metaData['name']
				this[relaySwitchObject1] = new RelaySwitches({}, 1, true)
			} else if (relaySwitchObjects == 2) {
				relaySwitchObject2 = metaData['name']
				this[relaySwitchObject2] = new RelaySwitches({}, 1, true)
			}

			relaySwitchObjects=relaySwitchObjects+1
			
			settings.put(metaData['name'], new RelaySwitches({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class relay switch!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.relaySwitchObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class relay switch!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.relaySwitchObject\n")
			}
			break
		case "capability.sleepSensor":
			globalObjects.eachLine { line ->
				if(line.contains("sleepSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def sleepSensorObject = new SleepSensors(sendEvent, 1, init)\n")

			if (sleepSensorObjects == 0) {
				sleepSensorObject0 = metaData['name']
				this[sleepSensorObject0] = new SleepSensors({}, 1, true)
			} else if (sleepSensorObjects == 1) {
				sleepSensorObject1 = metaData['name']
				this[sleepSensorObject1] = new SleepSensors({}, 1, true)
			} else if (sleepSensorObjects == 2) {
				sleepSensorObject2 = metaData['name']
				this[sleepSensorObject2] = new SleepSensors({}, 1, true)
			}

			sleepSensorObjects=sleepSensorObjects+1
			
			settings.put(metaData['name'], new SleepSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class sleep sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.sleepSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class sleep sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.sleepSensorObject\n")
			}
			break
		case "capability.smokeDetector":
			globalObjects.eachLine { line ->
				if(line.contains("smokeDetectorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def smokeDetectorObject = new SmokeDetectors(sendEvent, 1, init)\n")

			if (smokeDetectorObjects == 0) {
				smokeDetectorObject0 = metaData['name']
				this[smokeDetectorObject0] = new SmokeDetectors({}, 1, true)
			} else if (smokeDetectorObjects == 1) {
				smokeDetectorObject1 = metaData['name']
				this[smokeDetectorObject1] = new SmokeDetectors({}, 1, true)
			} else if (smokeDetectorObjects == 2) {
				smokeDetectorObject2 = metaData['name']
				this[smokeDetectorObject2] = new SmokeDetectors({}, 1, true)
			}

			smokeDetectorObjects=smokeDetectorObjects+1
			
			settings.put(metaData['name'], new SmokeDetectors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class smoke detector!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.smokeDetectorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class smoke detector!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.smokeDetectorObject\n")
			}
			break
		case "capability.stepSensor":
			globalObjects.eachLine { line ->
				if(line.contains("stepSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def stepSensorObject = new StepSensors(sendEvent, 1, init)\n")

			if (stepSensorObjects == 0) {
				stepSensorObject0 = metaData['name']
				this[stepSensorObject0] = new StepSensors({}, 1, true)
			} else if (stepSensorObjects == 1) {
				stepSensorObject1 = metaData['name']
				this[stepSensorObject1] = new StepSensors({}, 1, true)
			} else if (stepSensorObjects == 2) {
				stepSensorObject2 = metaData['name']
				this[stepSensorObject2] = new StepSensors({}, 1, true)
			}

			stepSensorObjects=stepSensorObjects+1
			
			settings.put(metaData['name'], new StepSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class step sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.stepSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class step sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.stepSensorObject\n")
			}
			break
		case "capability.switch":		
			globalObjects.eachLine { line ->
				if(line.contains("switchObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def switchObject = new Switches(sendEvent, 1, init)\n")

			if (switchObjects == 0) {
				switchObject0 = metaData['name']
				this[switchObject0] = new Switches({}, 1, true)
			} else if (switchObjects == 1) {
				switchObject1 = metaData['name']
				this[switchObject1] = new Switches({}, 1, true)
			} else if (switchObjects == 2) {
				switchObject2 = metaData['name']
				this[switchObject2] = new Switches({}, 1, true)
			}

			switchObjects=switchObjects+1
			
			settings.put(metaData['name'], new Switches({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class switch!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.switchObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class switch!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.switchObject\n")
			}
			break
		case "capability.switchLevel":
			globalObjects.eachLine { line ->
				if(line.contains("switchLevelObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def switchLevelObject = new SwitchLevels(sendEvent, 1, init)\n")

			if (switchLevelObjects == 0) {
				switchLevelObject0 = metaData['name']
				this[switchLevelObject0] = new SwitchLevels({}, 1, true)
			} else if (switchLevelObjects == 1) {
				switchLevelObject1 = metaData['name']
				this[switchLevelObject1] = new SwitchLevels({}, 1, true)
			} else if (switchLevelObjects == 2) {
				switchLevelObject2 = metaData['name']
				this[switchLevelObject2] = new SwitchLevels({}, 1, true)
			}

			switchLevelObjects=switchLevelObjects+1

			settings.put(metaData['name'], new SwitchLevels({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class switch level!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.switchLevelObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class switch level!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.switchLevelObject\n")
			}
			break
		case "capability.temperatureMeasurement":
			globalObjects.eachLine { line ->
				if(line.contains("temperatureMeasurementObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def temperatureMeasurementObject = new TemperatureMeasurements(sendEvent, 1, init)\n")

			if (temperatureMeasurementObjects == 0) {
				temperatureMeasurementObject0 = metaData['name']
				this[temperatureMeasurementObject0] = new TemperatureMeasurements({}, 1, true)
			} else if (temperatureMeasurementObjects == 1) {
				temperatureMeasurementObject1 = metaData['name']
				this[temperatureMeasurementObject1] = new TemperatureMeasurements({}, 1, true)
			} else if (temperatureMeasurementObjects == 2) {
				temperatureMeasurementObject2 = metaData['name']
				this[temperatureMeasurementObject2] = new TemperatureMeasurements({}, 1, true)
			}

			temperatureMeasurementObjects=temperatureMeasurementObjects+1

			settings.put(metaData['name'], new TemperatureMeasurements({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class temperature measurement!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.temperatureMeasurementObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class temperature measurement!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.temperatureMeasurementObject\n")
			}
			break
		case "capability.thermostat":
			globalObjects.eachLine { line ->
				if(line.contains("thermostatObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def thermostatObject = new Thermostats(sendEvent, 1, init)\n")
				
			if (thermostatObjects == 0) {
				thermostatObject0 = metaData['name']
				this[thermostatObject0] = new Thermostats({}, 1, true)
			} else if (thermostatObjects == 1) {
				thermostatObject1 = metaData['name']
				this[thermostatObject1] = new Thermostats({}, 1, true)
			} else if (thermostatObjects == 2) {
				thermostatObject2 = metaData['name']
				this[thermostatObject2] = new Thermostats({}, 1, true)
			}

			thermostatObjects=thermostatObjects+1

			settings.put(metaData['name'], new Thermostats({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class thermostat!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.thermostatObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class thermostat!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.thermostatObject\n")
			}
			break
		case "capability.valve":
			globalObjects.eachLine { line ->
				if(line.contains("valveObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def valveObject = new Valves(sendEvent, 1, init)\n")

			if (valveObjects == 0) {
				valveObject0 = metaData['name']
				this[valveObject0] = new Valves({}, 1, true)
			} else if (valveObjects == 1) {
				valveObject1 = metaData['name']
				this[valveObject1] = new Valves({}, 1, true)
			} else if (valveObjects == 2) {
				valveObject2 = metaData['name']
				this[valveObject2] = new Valves({}, 1, true)
			}

			valveObjects=valveObjects+1

			settings.put(metaData['name'], new Valves({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class valve!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.valveObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class valve!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.valveObject\n")
			}
			break
		case "capability.speechSynthesis":
			globalObjects.eachLine { line ->
				if(line.contains("speechSynthesisObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def speechSynthesisObject = new SpeechSynthesises(sendEvent, 1, init)\n")

			if (speechSynthesisObjects == 0) {
				speechSynthesisObject0 = metaData['name']
				this[speechSynthesisObject0] = new SpeechSynthesises({}, 1, true)
			} else if (speechSynthesisObjects == 1) {
				speechSynthesisObject1 = metaData['name']
				this[speechSynthesisObject1] = new SpeechSynthesises({}, 1, true)
			} else if (speechSynthesisObjects == 2) {
				speechSynthesisObject2 = metaData['name']
				this[speechSynthesisObject2] = new SpeechSynthesises({}, 1, true)
			}

			speechSynthesisObjects=speechSynthesisObjects+1

			settings.put(metaData['name'], new SpeechSynthesises({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class speech synthesis!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.speechSynthesisObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class speech synthesis!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.speechSynthesisObject\n")
			}
			break
		case "capability.waterSensor":
			globalObjects.eachLine { line ->
				if(line.contains("waterSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def waterSensorObject = new WaterSensors(sendEvent, 1, init)\n")

			if (waterSensorObjects == 0) {
				waterSensorObject0 = metaData['name']
				this[waterSensorObject0] = new WaterSensors({}, 1, true)
			} else if (waterSensorObjects == 1) {
				waterSensorObject1 = metaData['name']
				this[waterSensorObject1] = new WaterSensors({}, 1, true)
			} else if (waterSensorObjects == 2) {
				waterSensorObject2 = metaData['name']
				this[waterSensorObject2] = new WaterSensors({}, 1, true)
			}

			waterSensorObjects=waterSensorObjects+1

			settings.put(metaData['name'], new WaterSensors({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class water sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.waterSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class water sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.waterSensorObject\n")
			}
			break
		case "capability.touchSensor":
			globalObjects.eachLine { line ->
				if(line.contains("touchSensorObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def touchSensorObject = new NfcTouch(sendEvent, 1)\n")

			if (touchSensorObjects == 0) {
				touchSensorObject0 = metaData['name']
				this[touchSensorObject0] = new NfcTouch({}, 1)
			} else if (touchSensorObjects == 1) {
				touchSensorObject1 = metaData['name']
				this[touchSensorObject1] = new NfcTouch({}, 1)
			} else if (touchSensorObjects == 2) {
				touchSensorObject2 = metaData['name']
				this[touchSensorObject2] = new NfcTouch({}, 1)
			}

			touchSensorObjects=touchSensorObjects+1

			settings.put(metaData['name'], new NfcTouch({}, 1))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class Touch Sensor!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.touchSensorObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class Touch Sensor!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.touchSensorObject\n")
			}
			break
		case "capability.imageCapture":
			globalObjects.eachLine { line ->
				if(line.contains("imageCaptureObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def imageCaptureObject = new ImageCaptures(sendEvent, 1, init)\n")

			if (imageCaptureObjects == 0) {
				imageCaptureObject0 = metaData['name']
				this[imageCaptureObject0] = new ImageCaptures({}, 1, true)
			} else if (imageCaptureObjects == 1) {
				imageCaptureObject1 = metaData['name']
				this[imageCaptureObject1] = new ImageCaptures({}, 1, true)
			} else if (imageCaptureObjects == 2) {
				imageCaptureObject2 = metaData['name']
				this[imageCaptureObject2] = new ImageCaptures({}, 1, true)
			}

			imageCaptureObjects=imageCaptureObjects+1

			settings.put(metaData['name'], new ImageCaptures({}, 1, true))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class Image Capture!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.imageCaptureObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class Image Capture!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.imageCaptureObject\n")
			}
			break
		case "device.mobilePresence":
			globalObjects.eachLine { line ->
				if(line.contains("mobilePresenceObject")){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def mobilePresenceObject = new MobilePresences(sendEvent, 1)\n")

			if (mobilePresenceObjects == 0) {
				mobilePresenceObject0 = metaData['name']
				this[mobilePresenceObject0] = new MobilePresences({}, 1)
			} else if (mobilePresenceObjects == 1) {
				mobilePresenceObject1 = metaData['name']
				this[mobilePresenceObject1] = new MobilePresences({}, 1)
			} else if (mobilePresenceObjects == 2) {
				mobilePresenceObject2 = metaData['name']
				this[mobilePresenceObject2] = new MobilePresences({}, 1)
			}

			mobilePresenceObjects=mobilePresenceObjects+1

			settings.put(metaData['name'], new MobilePresences({}, 1))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class mobile presence!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.mobilePresenceObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class mobile presence!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.mobilePresenceObject\n")
			}
			break
		case "device.aeonKeyFob":
			globalObjects.eachLine { line ->
				if(line.contains("aeonKeyFobObject")){
					contains = 1
			    	}
			}
			if (contains == 0)
				globalObjects.append("@Field def aeonKeyFobObject = new AeonKeyFobs(sendEvent, 1)\n")

			if (aeonKeyFobObjects == 0) {
				aeonKeyFobObject0 = metaData['name']
				this[aeonKeyFobObject0] = new AeonKeyFobs({}, 1)
			} else if (aeonKeyFobObjects == 1) {
				aeonKeyFobObject1 = metaData['name']
				this[aeonKeyFobObject1] = new AeonKeyFobs({}, 1)
			} else if (aeonKeyFobObjects == 2) {
				aeonKeyFobObject2 = metaData['name']
				this[aeonKeyFobObject2] = new AeonKeyFobs({}, 1)
			}

			aeonKeyFobObjects=aeonKeyFobObjects+1

			settings.put(metaData['name'], new AeonKeyFobs({}, 1)) 

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class aeon key fob!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.aeonKeyFobObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class aeon key fob!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.aeonKeyFobObject\n")
			}
			break
		case "mode":
			//def randomVariable = Math.abs(new Random().nextInt() % 3)
			def modes = ["away", "home", "night"]
			// Always assign a different mode to each app
			def userInput
			if (assignDifferentModes) {			
				userInput = modes[chooseMode]
				if (chooseMode < 3)
					chooseMode++;
				else
					chooseMode = chooseMode%3
			} else {
				userInput = modes[1]
			}

			if (modeVariables == 0) {
				mode0 = metaData['name']
				this[mode0] = userInput
			} else if (modeVariables == 1) {
				mode1 = metaData['name']
				this[mode1] = userInput
			} else if (modeVariables == 2) {
				mode2 = metaData['name']
				this[mode2] = userInput
			} else if (modeVariables == 3) {
				mode3 = metaData['name']
				this[mode3] = userInput
			} else if (modeVariables == 4) {
				mode4 = metaData['name']
				this[mode4] = userInput
			} else if (modeVariables == 5) {
				mode5 = metaData['name']
				this[mode5] = userInput
			}

			if (modeVariables != 5)
				modeVariables=modeVariables+1
			else
				modeVariables=0


			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for mode!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for mode!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			}
			break
		case "decimal":
			//def userInput = Math.abs(new Random().nextInt() % 60) + 40
			def userInput = 50

			if (decimalVariables == 0) {
				decimal0 = metaData['name']
				this[decimal0] = userInput
			} else if (decimalVariables == 1) {
				decimal1 = metaData['name']
				this[decimal1] = userInput
			} else if (decimalVariables == 2) {
				decimal2 = metaData['name']
				this[decimal2] = userInput
			} else if (decimalVariables == 3) {
				decimal3 = metaData['name']
				this[decimal3] = userInput
			} else if (decimalVariables == 4) {
				decimal4 = metaData['name']
				this[decimal4] = userInput
			} else if (decimalVariables == 5) {
				decimal5 = metaData['name']
				this[decimal5] = userInput
			}

			if (decimalVariables != 5)
				decimalVariables=decimalVariables+1
			else
				decimalVariables=0

			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for decimal number!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = "+userInput+"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for decimal number!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = "+userInput+"\n")
			}
			break
		case "text":
			def userInput = "This is just a text!"
			
			if (textVariables == 0) {
				text0 = metaData['name']
				this[text0] = userInput
			} else if (textVariables == 1) {
				text1 = metaData['name']
				this[text1] = userInput
			} else if (textVariables == 2) {
				text2 = metaData['name']
				this[text2] = userInput
			} else if (textVariables == 3) {
				text3 = metaData['name']
				this[text3] = userInput
			} else if (textVariables == 4) {
				text4 = metaData['name']
				this[text4] = userInput
			} else if (textVariables == 5) {
				text5 = metaData['name']
				this[text5] = userInput
			}

			if (textVariables != 5)
				textVariables=textVariables+1
			else
				textVariables=0


			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for text!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for text!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			}
			break
		case "number":
			//def userInput = Math.abs(new Random().nextInt() % 60) + 40
			def userInput = 50

			if (numberVariables == 0) {
				number0 = metaData['name']
				this[number0] = userInput
			} else if (numberVariables == 1) {
				number1 = metaData['name']
				this[number1] = userInput
			} else if (numberVariables == 2) {
				number2 = metaData['name']
				this[number2] = userInput
			} else if (numberVariables == 3) {
				number3 = metaData['name']
				this[number3] = userInput
			} else if (numberVariables == 4) {
				number4 = metaData['name']
				this[number4] = userInput
			} else if (numberVariables == 5) {
				number5 = metaData['name']
				this[number5] = userInput
			}


			if (numberVariables != 5)
				numberVariables=numberVariables+1
			else
				numberVariables=0
			

			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for number!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = "+userInput+"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for number!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = "+userInput+"\n")
			}
			break
		case "time":
			def userInput = "15:00"
			
			if (timeVariables == 0) {
				time0 = metaData['name']
				this[time0] = userInput
			} else if (timeVariables == 1) {
				time1 = metaData['name']
				this[time1] = userInput
			} else if (timeVariables == 2) {
				time2 = metaData['name']
				this[time2] = userInput
			} else if (timeVariables == 3) {
				time3 = metaData['name']
				this[time3] = userInput
			} else if (timeVariables == 4) {
				time4 = metaData['name']
				this[time4] = userInput
			} else if (timeVariables == 5) {
				time5 = metaData['name']
				this[time5] = userInput
			}

			if (timeVariables != 5)
				timeVariables=timeVariables+1
			else
				timeVariables=0

			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for time!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for time!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			}
			break
		case "enum":
			if (metaData['options'] != null)
				modes = metaData['options']
			else if (metaData['metadata'] != null) { // If it is not named 'options' then it is captured as 'metadata'
				modes = metaData['metadata']
				modes = modes['values']
			}
			def userInput = modes[0]
	
			if (enumVariables == 0) {
				enum0 = metaData['name']
				this[enum0] = userInput
			} else if (enumVariables == 1) {
				enum1 = metaData['name']
				this[enum1] = userInput
			} else if (enumVariables == 2) {
				enum2 = metaData['name']
				this[enum2] = userInput
			} else if (enumVariables == 3) {
				enum3 = metaData['name']
				this[enum3] = userInput
			} else if (enumVariables == 4) {
				enum4 = metaData['name']
				this[enum4] = userInput
			} else if (enumVariables == 5) {
				enum5 = metaData['name']
				this[enum5] = userInput
			}

			if (enumVariables != 5)
				enumVariables=enumVariables+1
			else
				enumVariables=0			


			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for enum!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for enum!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			}
			break
		case "boolean":
			//def userInput = Math.abs(new Random().nextInt() % 2)
			def userInput = true

			if (boolVariables == 0) {
				bool0 = metaData['name']
				this[bool0] = userInput
			} else if (boolVariables == 1) {
				bool1 = metaData['name']
				this[bool1] = userInput
			} else if (boolVariables == 2) {
				bool2 = metaData['name']
				this[bool2] = userInput
			} else if (boolVariables == 3) {
				bool3 = metaData['name']
				this[bool3] = userInput
			} else if (boolVariables == 4) {
				bool4 = metaData['name']
				this[bool4] = userInput
			} else if (boolVariables == 5) {
				bool5 = metaData['name']
				this[bool5] = userInput
			}

			if (boolVariables != 5)
				boolVariables=boolVariables+1
			else
				boolVariables=0


			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for boolean!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for boolean!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			}
			break
		case "bool":
			//def userInput = Math.abs(new Random().nextInt() % 2)
			def userInput = true

			if (boolVariables == 0) {
				bool0 = metaData['name']
				this[bool0] = userInput
			} else if (boolVariables == 1) {
				bool1 = metaData['name']
				this[bool1] = userInput
			} else if (boolVariables == 2) {
				bool2 = metaData['name']
				this[bool2] = userInput
			} else if (boolVariables == 3) {
				bool3 = metaData['name']
				this[bool3] = userInput
			} else if (boolVariables == 4) {
				bool4 = metaData['name']
				this[bool4] = userInput
			} else if (boolVariables == 5) {
				bool5 = metaData['name']
				this[bool5] = userInput
			}

			if (boolVariables != 5)
				boolVariables=boolVariables+1
			else
				boolVariables=0


			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for boolean!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for boolean!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			}
			break
		case "phone":
			def userInput = 9495379373

			if (phoneVariables == 0) {
				phone0 = metaData['name']
				this[phone0] = userInput
			} else if (phoneVariables == 1) {
				phone1 = metaData['name']
				this[phone1] = userInput
			} else if (phoneVariables == 2) {
				phone2 = metaData['name']
				this[phone2] = userInput
			} else if (phoneVariables == 3) {
				phone3 = metaData['name']
				this[phone3] = userInput
			} else if (phoneVariables == 4) {
				phone4 = metaData['name']
				this[phone4] = userInput
			} else if (phoneVariables == 5) {
				phone5 = metaData['name']
				this[phone5] = userInput
			}

			if (phoneVariables != 5)
				phoneVariables=phoneVariables+1
			else
				phoneVariables=0

			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for phone!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = "+userInput+"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for phone!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = "+userInput+"\n")
			}
			break
		case "contact":
			def userInput = "AJ"
	
			if (contactVariables == 0) {
				contact0 = metaData['name']
				this[contact0] = userInput
			} else if (contactVariables == 1) {
				contact1 = metaData['name']
				this[contact1] = userInput
			} else if (contactVariables == 2) {
				contact2 = metaData['name']
				this[contact2] = userInput
			} else if (contactVariables == 3) {
				contact3 = metaData['name']
				this[contact3] = userInput
			} else if (contactVariables == 4) {
				contact4 = metaData['name']
				this[contact4] = userInput
			} else if (contactVariables == 5) {
				contact5 = metaData['name']
				this[contact5] = userInput
			}

			if (contactVariables != 5)
				contactVariables=contactVariables+1
			else
				contactVariables=0

			settings.put(metaData['name'], metaData['name'])

			if (App == "App1") {			
				extractedObjectsApp1.append("//Global variable for contact!\n")
				extractedObjectsApp1.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			} else {
				extractedObjectsApp2.append("//Global variable for contact!\n")
				extractedObjectsApp2.append("def "+metaData['name']+" = \""+userInput+"\"\n")
			}
			break
		case "capability.remainingDevices":
			String nameOfObject = thisCapability+"Object"
			globalObjects.eachLine { line ->
				if(line.contains(nameOfObject)){
					contains = 1
			    	}
			}

			if (contains == 0)
				globalObjects.append("@Field def $nameOfObject = new RemainingDevices(sendEvent, 1)\n")

			settings.put(metaData['name'], new RemainingDevices({}, 1))

			if (App == "App1") {
				extractedObjectsApp1.append("//Object for class remaining devices!\n")
				extractedObjectsApp1.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp1.append(metaData['name']+" = obj.$nameOfObject\n")		
			} else {
				extractedObjectsApp2.append("//Object for class remaining devices!\n")
				extractedObjectsApp2.append("def "+metaData['name']+"\n")
				extractedObjectsConstructorApp2.append(metaData['name']+" = obj.$nameOfObject\n")
			}
			break
		default:
			break
	}
}

def label(LinkedHashMap metaData) {
	if (metaData.containsKey('title')) {
		println metaData['title']
	}
	if (metaData.containsKey('options')) {
		println "Options: "+metaData['options']
	}

	println("//IGNORE--ForMobileUse//")
}

def mode(LinkedHashMap metaData) {
	if (metaData.containsKey('title')) {
		println metaData['title']
	}
	if (metaData.containsKey('options')) {
		println "Options: "+metaData['options']
	}

	println("//IGNORE--ForMobileUse//")
}

def href(LinkedHashMap metaData) {
	println("//IGNORE--some data//")
}

def href(LinkedHashMap metaData, String name) {
	println("//IGNORE--some data//")
}
/////Input Methods/////


/////MethodsForExtraction/////
def definition(LinkedHashMap metaData) {
	println("///Just some information///")
}

def preferences(Closure inputData) {
	find(inputData) //Run the closure to extract pages/sections/inputMethods
	settings.put("END", "END")
	if (App == "App1") {		
		extractedObjectsConstructorApp1.append("//Global variable for settings!\n")
		extractedObjectsConstructorApp1.append("settings = [")
		settings.each{
    			key, value ->
			if (value != "END")
				extractedObjectsConstructorApp1.append("$key: $key, ")
			else
				extractedObjectsConstructorApp1.append("END: \"END\"]\n")
		}
	} else {
		extractedObjectsConstructorApp2.append("//Global variable for settings!\n")
		extractedObjectsConstructorApp2.append("settings = [")
		settings.each{
    			key, value ->
			if (value != "END")
				extractedObjectsConstructorApp2.append("$key: $key, ")
			else
				extractedObjectsConstructorApp2.append("END: \"END\"]\n")
		}
		def line
		File GlobalVariablesBothApps = new File("GlobalVariables/GlobalVariablesBothApps.groovy")
		GlobalVariablesBothApps.write("")
		GlobalVariablesBothApps.append("//Creating Global variables for both apps\n")
		GlobalVariablesBothApps.append("@Field def sendEvent = {eventDataMap -> eventHandler(eventDataMap)}\n")
		GlobalVariablesBothApps.append("@Field def locationObject = new LocationVar(sendEvent, init)\n")
		GlobalVariablesBothApps.append("@Field def appObject = new Touched(sendEvent, 0)\n")
		
		globalObjects.withReader { reader ->
			while ((line = reader.readLine()) != null) {
				GlobalVariablesBothApps.append(line)
				GlobalVariablesBothApps.append("\n")
			}
		 }
	}
}

def image(String data) {
	println("//IGNORE--some image//")
}

def page(LinkedHashMap metaData, Closure inputData) {
	if (metaData.containsKey('name'))
		println(metaData['name'])
	if (metaData.containsKey('title'))
		println(metaData['title'])

	find(inputData) //Run the closure to extract sections/inputMethods
}

def page(LinkedHashMap metaData) {
	def nameOfFunction = metaData['name']
	"$nameOfFunction"() //Call the page	
}

def dynamicPage(LinkedHashMap metaData, Closure inputData) {
	if (metaData.containsKey('name'))
		println(metaData['name'])
	if (metaData.containsKey('title'))
		println(metaData['title'])

	find(inputData) //Run the closure to extract sections/inputMethods
}

def paragraph(String paragraphText) {
	println(paragraphText)
}

def paragraph(LinkedHashMap metaData, String paragraphText) {
	println(paragraphText)
}

def section(String title, Closure inputData) {
	println(title)
	find(inputData) //Run the closure to extract inputMethods
}

def section(Closure inputData) {
	find(inputData) //Run the closure to extract inputMethods
}

def section(LinkedHashMap metaData, Closure inputData) {
	find(inputData) //Run the closure to extract inputMethods
}

def section(LinkedHashMap metaData, String data, Closure inputData) {
	find(inputData)
}

def mappings(Closure inputData) {
	println("//IGNORE--some data//")
}
/////MethodsForExtraction/////

