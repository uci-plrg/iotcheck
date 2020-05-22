//Create a class for thermostat device
package Thermostat
import Timer.SimulatedTimer

public class Thermostat {
	private String id
	private String label
	private String displayName
	private int temperature
	private int currentTemperature
	private int currentCoolingSetpoint
	private int currentHeatingSetpoint
	private int coolingSetpoint
	private int latestCoolingSetPoint
	private int thermostatSetpoint
	private int latestThermostatSetPoint
	private int heatingSetpoint
	private int latestHeatingSetPoint
	private List coolingSetpointRange
	private List thermostatSetpointRange
	private List heatingSetpointRange
	private List supportedThermostatFanModes
	private List supportedThermostatModes
	private String thermostatOperatingState
	private String thermostatFanMode
	private String thermostatMode
	private String currentThermostatMode
	private String climateName
	private String thermostatLatestMode
	private String thermostatLatestOperatingState
	private String thermostatLatestFanMode
	def sendEvent
	def timers


	Thermostat(Closure sendEvent, String id, String label, String displayName, int temperature, int currentCoolingSetpoint, int currentHeatingSetpoint, int coolingSetpoint, 
                   int thermostatSetpoint, int heatingSetpoint, List coolingSetpointRange, List thermostatSetpointRange, List heatingSetpointRange, 
                   List supportedThermostatFanModes, List supportedThermostatModes, String thermostatOperatingState, String thermostatFanMode, String thermostatMode,
		   String climateName, String thermostatLatestMode, String thermostatLatestOperatingState, String thermostatLatestFanMode, int latestCoolingSetPoint,
		   int latestThermostatSetPoint, int latestHeatingSetPoint) {
		this.id = id
		this.label = label
		this.sendEvent = sendEvent
		this.temperature = temperature
		this.currentTemperature = currentTemperature
		this.currentCoolingSetpoint = currentCoolingSetpoint
		this.currentHeatingSetpoint = currentHeatingSetpoint
		this.coolingSetpoint = coolingSetpoint
		this.thermostatSetpoint = thermostatSetpoint
		this.heatingSetpoint = heatingSetpoint
		this.coolingSetpointRange = coolingSetpointRange
		this.thermostatSetpointRange = thermostatSetpointRange
		this.heatingSetpointRange = heatingSetpointRange
		this.supportedThermostatFanModes = supportedThermostatFanModes
		this.supportedThermostatModes = supportedThermostatModes
		this.thermostatOperatingState = thermostatOperatingState
		this.thermostatFanMode = thermostatFanMode
		this.thermostatMode = thermostatMode
		this.currentThermostatMode = thermostatMode
		this.climateName = climateName
		this.thermostatLatestMode = thermostatLatestMode
		this.thermostatLatestOperatingState = thermostatLatestOperatingState
		this.thermostatLatestFanMode = thermostatLatestFanMode
		this.latestHeatingSetPoint = latestHeatingSetPoint
		this.latestThermostatSetPoint = latestThermostatSetPoint
		this.latestCoolingSetPoint = latestCoolingSetPoint
	}


	//By Apps
	def setCoolingSetpoint(int coolingSetpoint) {
		if (this.coolingSetpoint != coolingSetpoint) {
			this.latestCoolingSetPoint = coolingSetpoint
			this.coolingSetpoint = coolingSetpoint
			this.currentCoolingSetpoint = coolingSetpoint
			println("Cooling set point for the thermostat with id:$id is changed to $coolingSetpoint!")
			sendEvent([name: "coolingSetpoint", value: "$coolingSetpoint", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setCoolingSetpoint(String coolingSetpoint) {
		setCoolingSetpoint(coolingSetpoint.toInteger())
	}

	def setHeatingSetpoint(int heatingSetpoint) {
		if (this.heatingSetpoint != heatingSetpoint) {
			this.latestHeatingSetPoint = heatingSetpoint
			this.heatingSetpoint = heatingSetpoint
			this.currentHeatingSetpoint = heatingSetpoint
			println("Heating set point for the thermostat with id:$id is changed to $heatingSetpoint!")
			sendEvent([name: "heatingSetpoint", value: "$heatingSetpoint", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setHeatingSetpoint(String heatingSetpoint) {
		setHeatingSetpoint(heatingSetpoint.toInteger())
	}

	def setSchedule() {
		//Not implemented yet
	}

	def setThermostatFanMode(String thermostatFanMode) {
		if (this.thermostatFanMode != thermostatFanMode) {
			this.thermostatLatestFanMode = thermostatFanMode
			this.thermostatFanMode = thermostatFanMode
			println("Fan mode of the thermostat with id:$id is changed to $thermostatFanMode!")
			sendEvent([name: "thermostatFanMode", value: "$thermostatFanMode", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setThermostatMode(String thermostatMode) {
		if (this.thermostatMode != thermostatMode) {
			this.thermostatLatestMode =thermostatMode
			this.thermostatMode = thermostatMode
			this.currentThermostatMode = thermostatMode
			println("Mode of the thermostat with id:$id is changed to $thermostatMode!")
			sendEvent([name: "thermostatMode", value: "$thermostatMode", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def cool() {
		if (this.thermostatMode != "cool") {
			this.thermostatLatestMode = "cool"
			this.thermostatMode = "cool"
			this.currentThermostatMode = "cool"
			println("Mode of the thermostat with id:$id is changed to cool!")
			sendEvent([name: "thermostatMode", value: "cool", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def heat() {
		if (this.thermostatMode != "heat") {
			this.thermostatLatestMode = "heat"
			this.thermostatMode = "heat"
			this.currentThermostatMode = "heat"
			println("Mode of the thermostat with id:$id is changed to heat!")
			sendEvent([name: "thermostatMode", value: "heat", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def auto() {
		if (this.thermostatMode != "auto") {
			this.thermostatLatestMode = "auto"
			this.thermostatMode = "auto"
			this.currentThermostatMode = "auto"
			println("Mode of the thermostat with id:$id is changed to auto!")
			sendEvent([name: "thermostatMode", value: "auto", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	
	def emergencyHeat() {
		if (this.thermostatMode != "emergencyHeat") {
			this.thermostatLatestMode = "emergencyHeat"
			this.thermostatMode = "emergencyHeat"
			this.currentThermostatMode = "emergencyHeat"
			println("Mode of the thermostat with id:$id is changed to emergencyHeat!")
			sendEvent([name: "thermostatMode", value: "emergencyHeat", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def off() {
		if (this.thermostatMode != "off") {
			this.thermostatLatestMode = "off"
			this.thermostatMode = "off"
			this.currentThermostatMode = "off"
			println("Mode of the thermostat with id:$id is changed to off!")
			sendEvent([name: "thermostatMode", value: "off", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setClimate(String info, String givenClimateName) {
		if (this.climateName != givenClimateName) {
			this.climateName = givenClimateName
			println("Climate name of the thermostat with id:$id is changed to $givenClimateName!")
		}
	}

	def setHold(String info1, int coolingSetpoint, int heatingSetpoint, String info2, String info3) {
		if ((this.heatingSetpoint != heatingSetpoint) || (this.coolingSetpoint != coolingSetpoint)) {
			this.coolingSetpoint = coolingSetpoint
			this.currentCoolingSetpoint = currentCoolingSetpoint
			println("Cooling set point for the thermostat with id:$id is changed to $coolingSetpoint!")
			this.heatingSetpoint = heatingSetpoint
			this.currentHeatingSetpoint = currentHeatingSetpoint
			println("Heating set point for the thermostat with id:$id is changed to $heatingSetpoint!")
			sendEvent([name: "heatingSetpoint", value: "$heatingSetpoint", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			sendEvent([name: "coolingSetpoint", value: "$coolingSetpoint", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	//By Model Checker
	def setValue(String value, String name) {
		if ((name == "temperature") && (value.toInteger() != this.temperature)) {
			println("the temperature is $value!")
			this.temperature = value.toInteger()
			this.currentTemperature = value.toInteger()
		} else if ((name == "heatingSetpoint") && (value.toInteger() != this.heatingSetpoint)) {
			println("the heating set point of the thermostat with id:$id is $value!")
			this.latestHeatingSetPoint = value.toInteger()
			this.heatingSetpoint = value.toInteger()
		} else if ((name == "coolingSetpoint") && (value.toInteger() != this.coolingSetpoint)) {
			println("the cooling set point of the thermostat with id:$id is $value!")
			this.latestCoolingSetPoint = value.toInteger()
			this.coolingSetpoint = value.toInteger()
		} else if ((name == "thermostatSetpoint") && (value.toInteger() != this.thermostatSetpoint)) {
			println("the set point of the thermostat with id:$id is $value!")
			this.latestThermostatSetPoint = value.toInteger()
			this.thermostatSetpoint = value.toInteger()
		} else if ((name == "thermostatMode") && (value != this.thermostatMode)) {
			println("the mode of the thermostat with id:$id is $value!")
			this.thermostatLatestMode = value
			this.thermostatMode = value
		} else if ((name == "thermostatFanMode") && (value != this.thermostatFanMode)) {
			println("the fan mode of the thermostat with id:$id is $value!")
			this.thermostatLatestFanMode = value
			this.thermostatFanMode = value
		} else if ((name == "thermostatOperatingState") && (value != this.thermostatOperatingState)) {
			println("the operating state of the thermostat with id:$id is $value!")
			this.thermostatLatestOperatingState = value
			this.thermostatOperatingState = value
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "thermostatMode")
			return thermostatMode
		else if (deviceFeature == "thermostatOperatingState")
			return thermostatOperatingState
		else if (deviceFeature == "thermostatFanMode")
			return thermostatFanMode
		else if (deviceFeature == "heatingSetpoint")
			return heatingSetpoint
		else if (deviceFeature == "coolingSetpoint")
			return coolingSetpoint
		else if (deviceFeature == "thermostatSetpoint")
			return thermostatSetpoint
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "thermostatMode")
			return thermostatLatestMode
		else if (deviceFeature == "thermostatOperatingState")
			return thermostatLatestOperatingState
		else if (deviceFeature == "thermostatFanMode")
			return thermostatLatestFanMode
		else if (deviceFeature == "heatingSetpoint")
			return latestHeatingSetPoint
		else if (deviceFeature == "coolingSetpoint")
			return latestCoolingSetPoint
		else if (deviceFeature == "thermostatSetpoint")
			return latestThermostatSetPoint
	}

}
