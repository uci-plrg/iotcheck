//Create a class for thermostat device
package Thermostat
import Timer.SimulatedTimer

public class Thermostats{
	int deviceNumbers	
	List thermostats	
	def sendEvent	
	def timers

	//When we have only one device
	private String id = "thermostatID0"
	private String label = "thermostat0"
	private String displayName = "thermostat0"
	private int temperature = 66
	private int currentTemperature = 66
	private int currentCoolingSetpoint = 70
	private int currentHeatingSetpoint = 50
	private int coolingSetpoint = 70
	private int thermostatSetpoint = 60
	private int heatingSetpoint = 50
	private coolingSetpointRange = [70, 90]
	private thermostatSetpointRange = [50, 70]
	private heatingSetpointRange = [20, 50]
	private supportedThermostatFanModes = ["auto", "fanCirculate", "circulate", "fanOn", "on"]
	private supportedThermostatModes = ["auto", "cool", "emergencyHeat", "heat", "off"]
	private String thermostatOperatingState = "cooling"
	private String thermostatFanMode = "off"
	private String thermostatMode = "off"
	private String currentThermostatMode = "off"
	private String climateName = ""
	private String thermostatLatestMode = "off"
	private String thermostatLatestOperatingState = "cooling"
	private String thermostatLatestFanMode = "off"
	private int latestCoolingSetPoint = 70
	private int latestThermostatSetPoint = 60
	private int latestHeatingSetPoint = 50


	Thermostats(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.thermostats = []

		if (init) {
			this.temperature = 60
			this.currentTemperature = 60
			this.currentCoolingSetpoint = 70
			this.coolingSetpoint = 70
			this.currentHeatingSetpoint = 35
			this.heatingSetpoint = 35
			this.thermostatSetpoint = 50
			this.thermostatFanMode = "off"
			this.thermostatLatestFanMode = "off"
			this.thermostatMode = "off"
			this.currentThermostatMode = "off"
			this.thermostatLatestMode = "off"
		} else {
			this.temperature = 66
			this.currentTemperature = 66
			this.currentCoolingSetpoint = 80
			this.coolingSetpoint = 80
			this.currentHeatingSetpoint = 50
			this.heatingSetpoint = 50
			this.thermostatSetpoint = 60
			this.thermostatFanMode = "circulate"
			this.thermostatLatestFanMode = "circulate"
			this.thermostatMode = "off"
			this.currentThermostatMode = "off"
			this.thermostatLatestMode = "off"		
		}
		thermostats.add(new Thermostat(sendEvent, id, label, displayName, this.temperature, this.currentCoolingSetpoint, 
                                               this.currentHeatingSetpoint, this.coolingSetpoint, this.thermostatSetpoint, this.heatingSetpoint, this.coolingSetpointRange,
                                               this.thermostatSetpointRange, this.heatingSetpointRange, this.supportedThermostatFanModes, this.supportedThermostatModes,
                                               this.thermostatOperatingState, this.thermostatFanMode,  this.thermostatMode, this.climateName, 
					       this.thermostatLatestMode, this.thermostatLatestOperatingState, this.thermostatLatestFanMode, this.latestCoolingSetPoint,
		   			       this.latestThermostatSetPoint, this.latestHeatingSetPoint))
	}

	//Methods for closures
	def count(Closure Input) {
		thermostats.count(Input)
	}
	def size() {
		thermostats.size()
	}
	def each(Closure Input) {
		thermostats.each(Input)
	}
	def find(Closure Input) {
		thermostats.find(Input)
	}
	def sort(Closure Input) {
		thermostats.sort(Input)
	}
	def collect(Closure Input) {
		thermostats.collect(Input)
	}

	//By Apps
	def setCoolingSetpoint(int coolingSetpoint) {
		if (coolingSetpoint != this.coolingSetpoint) {
			this.latestCoolingSetPoint = coolingSetpoint
			this.currentCoolingSetpoint = coolingSetpoint
			this.coolingSetpoint = coolingSetpoint
			thermostats[0].setCoolingSetpoint(coolingSetpoint)
		}
	}

	def setCoolingSetpoint(String coolingSetpoint) {
		setCoolingSetpoint(coolingSetpoint.toInteger())
	}

	def setHeatingSetpoint(int heatingSetpoint) {
		if (heatingSetpoint != this.heatingSetpoint) {
			this.latestHeatingSetPoint = heatingSetpoint
			this.currentHeatingSetpoint = heatingSetpoint
			this.heatingSetpoint = heatingSetpoint
			thermostats[0].setHeatingSetpoint(heatingSetpoint)
		}
	}

	def setHeatingSetpoint(String heatingSetpoint) {
		setHeatingSetpoint(heatingSetpoint.toInteger())
	}

	def setSchedule() {
		//Not implemented yet
	}

	def setThermostatFanMode(String thermostatFanMode) {
		if (thermostatFanMode != this.thermostatFanMode) {
			this.thermostatLatestFanMode = thermostatFanMode
			this.thermostatFanMode = thermostatFanMode
			thermostats[0].setThermostatFanMode(thermostatFanMode)
		}
	}

	def setThermostatMode(String thermostatMode) {
		if (thermostatMode != this.thermostatMode) {
			this.thermostatLatestMode = thermostatMode
			this.thermostatMode = thermostatMode
			this.currentThermostatMode = thermostatMode
			thermostats[0].setThermostatMode(thermostatMode)
		}
	}

	def cool() {
		if (thermostatMode != "cool") {
			this.thermostatLatestMode = "cool"
			this.thermostatMode = "cool"
			this.currentThermostatMode = "cool"
			thermostats[0].cool()
		}
	}

	def heat() {
		if (thermostatMode != "heat") {
			this.thermostatLatestMode = "heat"
			this.thermostatMode = "heat"
			this.currentThermostatMode = "heat"
			thermostats[0].heat()
		}
	}
	
	def auto() {
		if (thermostatMode != "auto") {
			this.thermostatLatestMode = "auto"
			this.thermostatMode = "auto"
			this.currentThermostatMode = "auto"
			thermostats[0].auto()
		}
	}

	def emergencyHeat() {
		if (thermostatMode != "emergencyHeat") {
			this.thermostatLatestMode = "emergencyHeat"
			this.thermostatMode = "emergencyHeat"
			this.currentThermostatMode = "emergencyHeat"
			thermostats[0].emergencyHeat()
		}
	}

	def off() {
		if (thermostatMode != "off") {
			this.thermostatLatestMode = "off"
			this.thermostatMode = "off"
			this.currentThermostatMode = "off"
			thermostats[0].off()
		}
	}

	def setClimate(String info, String givenClimateName) {
		if (givenClimateName != climateName) {
			this.climateName = givenClimateName
			thermostats[0].setClimate(info, givenClimateName)
		}
	}

	def setHold(String info1, int coolingSetpoint, int heatingSetpoint, String info2, String info3) {
		if ((coolingSetpoint != this.coolingSetpoint) || (heatingSetpoint != this.heatingSetpoint)) {
			this.currentCoolingSetpoint = coolingSetpoint
			this.coolingSetpoint = coolingSetpoint
			this.currentHeatingSetpoint = heatingSetpoint
			this.heatingSetpoint = heatingSetpoint
			this.latestCoolingSetPoint = coolingSetpoint
	                this.latestHeatingSetPoint = heatingSetpoint
			thermostats[0].setHold(info1, coolingSetpoint, heatingSetpoint, info2, info3)
		}
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["name"] == "temperature") {
			if (eventDataMap["value"].toInteger() != thermostats[0].temperature) {
				this.temperature = eventDataMap["value"].toInteger()
				this.currentTemperature = eventDataMap["value"].toInteger()
				thermostats[0].setValue(eventDataMap["value"], "temperature")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "heatingSetpoint") {
			if (eventDataMap["value"].toInteger() != thermostats[0].heatingSetpoint) {
				this.latestHeatingSetPoint = eventDataMap["value"].toInteger()
				this.heatingSetpoint = eventDataMap["value"].toInteger()
				thermostats[0].setValue(eventDataMap["value"], "heatingSetpoint")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "coolingSetpoint") {
			if (eventDataMap["value"].toInteger() != thermostats[0].coolingSetpoint) {
				this.latestCoolingSetPoint = eventDataMap["value"].toInteger()
				this.coolingSetpoint = eventDataMap["value"].toInteger()
				thermostats[0].setValue(eventDataMap["value"], "coolingSetpoint")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "thermostatSetpoint") {
			if (eventDataMap["value"].toInteger() != thermostats[0].thermostatSetpoint) {
				this.latestThermostatSetPoint = eventDataMap["value"].toInteger()
				this.thermostatSetpoint = eventDataMap["value"].toInteger()
				thermostats[0].setValue(eventDataMap["value"], "thermostatSetpoint")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "thermostatMode") {
			if (eventDataMap["value"] != thermostats[0].thermostatMode) {
				this.thermostatLatestMode = eventDataMap["value"]
				this.thermostatMode = eventDataMap["value"]
				this.currentThermostatMode = eventDataMap["value"]
				thermostats[0].setValue(eventDataMap["value"], "thermostatMode")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "thermostatFanMode") {
			if (eventDataMap["value"] != thermostats[0].thermostatFanMode) {
				this.thermostatLatestFanMode = eventDataMap["value"]
				this.thermostatFanMode = eventDataMap["value"]
				thermostats[0].setValue(eventDataMap["value"], "thermostatFanMode")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "thermostatOperatingState") {
			if (eventDataMap["value"] != thermostats[0].thermostatOperatingState) {
				this.thermostatLatestOperatingState = eventDataMap["value"]
				this.thermostatOperatingState = eventDataMap["value"]
				thermostats[0].setValue(eventDataMap["value"], "thermostatOperatingState")
				sendEvent(eventDataMap)
			}
		}
	}

	def currentValue(String deviceFeature) {
		thermostats[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		thermostats[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		thermostats[ix]
	}
}

