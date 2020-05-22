//Create a class for color temperature
package ColorTemperature
import Timer.SimulatedTimer

public class ColorTemperatures {
	private int deviceNumbers
	private List colorTemperatues
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "colorTemperatureID0"
	private String label = "colorTemperature0"
	private String displayName = "colorTemperature0"
	private String currentSwitch = "on"
	private int level = 50
	private int currentLevel = 50
	private int colorTemperature = 15000
	

	ColorTemperatures(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.deviceNumbers = deviceNumbers
		this.colorTemperatues = []

		if (init) {
			this.level = 30
			this.currentLevel = 40
			this.colorTemperature = 10000
		} else {
			this.level = 50
			this.currentLevel = 50
			this.colorTemperature = 15000
		}

		colorTemperatues.add(new ColorTemperature(sendEvent, id, label, displayName, this.level, this.currentSwitch, this.colorTemperature))
	}

	//Methods for closures
	def count(Closure Input) {
		colorTemperatues.count(Input)
	}
	def size() {
		colorTemperatues.size()
	}
	def each(Closure Input) {
		colorTemperatues.each(Input)
	}
	def find(Closure Input) {
		colorTemperatues.find(Input)
	}
	def sort(Closure Input) {
		colorTemperatues.sort(Input)
	}
	def collect(Closure Input) {
		colorTemperatues.collect(Input)
	}

	//By model checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["name"] == "switch") {
			if (eventDataMap["value"] != colorTemperatues[0].currentSwitch) {
				this.currentSwitch = eventDataMap["value"]
				colorTemperatues[0].setValue(eventDataMap["value"], "switch")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "colorTemperature") {
			if (eventDataMap["value"].toInteger() != colorTemperatues[0].colorTemperature) {
				this.colorTemperature = eventDataMap["value"].toInteger()
				colorTemperatues[0].setValue(eventDataMap["value"], "colorTemperature")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "level") {
			if (eventDataMap["value"].toInteger() != colorTemperatues[0].level) {
				this.currentLevel = eventDataMap["value"].toInteger() 
				this.level = eventDataMap["value"].toInteger()
				colorTemperatues[0].setValue(eventDataMap["value"], "level")
				sendEvent(eventDataMap)
			}
		}
	}


	//methods
	def setLevel(long level) {
		if (level != this.level) {
			this.currentLevel = level
			this.level = level
			colorTemperatues[0].setLevel(level)
		}
	}

	def setLevel(int level) {
		if (level != this.level) {
			this.currentLevel = level
			this.level = level
			colorTemperatues[0].setLevel(level)
		}
	}

	def setColorTemperature(String colorTemperature) {
		if (colorTemperature != this.colorTemperature) {
			this.colorTemperature = colorTemperature
			colorTemperatues[0].setColorTemperature(colorTemperature)			
		}
	}
	
	def setColorTemperature(int colorTemperature) {
		if (colorTemperature != this.colorTemperature) {
			this.colorTemperature = colorTemperature
			colorTemperatues[0].setColorTemperature(colorTemperature)			
		}
	}

	def on(String currentSwitch) {
		if (currentSwitch != this.currentSwitch) {
			this.currentSwitch = currentSwitch
			colorTemperatues[0].on(currentSwitch)			
		}
	}

	def off(String currentSwitch) {
		if (currentSwitch != this.currentSwitch) {
			this.currentSwitch = currentSwitch
			colorTemperatues[0].off(currentSwitch)			
		}
	}

	def currentValue(String deviceFeature) {
		colorTemperatues[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		colorTemperatues[0].latestValue(deviceFeature)
	}	

	def getAt(int ix) {
		colorTemperatues[ix]
	}
}
