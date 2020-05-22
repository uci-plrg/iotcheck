//Create a class for color temperature
package ColorTemperature
import Timer.SimulatedTimer


public class ColorTemperature {
	def sendEvent
	private String id
	private String label
	private String displayName
	private String currentSwitch
	private int level
	private int currentLevel
	private int colorTemperature
	
	ColorTemperature(Closure sendEvent, String id, String label, String displayName, int level, String currentSwitch, int colorTemperature) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.level = level
		this.currentLevel = level
		this.currentSwitch = currentSwitch
		this.colorTemperature = colorTemperature
		this.sendEvent = sendEvent
	}
	
	//By model checker
	def setValue(String value, String name) {
		if ((name == "level") && (value != this.level)) {
			this.currentLevel = value.toInteger()
			this.level = value.toInteger()
			println("The level of the light is changed to $value!")
		} else if ((name == "currentSwitch") && (value != this.currentSwitch)) {
			this.currentSwitch = value
			println("The light is changed to $value!")
		} else if ((name == "colorTemperature") && (value != this.colorTemperature)) {
			this.colorTemperature = value.toInteger()
			println("The color temperature level of the light is changed to $value!")
		}
	}

	//methods
	def setLevel(int level) {
		if (level != this.level) {
			this.currentLevel = level
			this.level = level
			println("The level of the light is changed to $level!")
			sendEvent([name: "level", value: "$level", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	
	def setLevel(long level) {
		if (level != this.level) {
			this.currentLevel = level
			this.level = level
			println("The level of the light is changed to $level!")
			sendEvent([name: "level", value: "$level", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setColorTemperature(int colorTemperature) {
		if (colorTemperature != this.colorTemperature) {
			this.colorTemperature = colorTemperature
			println("The color temperature level of the light is changed to $colorTemperature!")
			sendEvent([name: "colorTemperature", value: "$colorTemperature", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def on(String currentSwitch) {
		if (currentSwitch != this.currentSwitch) {
			this.currentSwitch = currentSwitch
			println("The light is changed to $currentSwitch!")
			sendEvent([name: "switch", value: "$currentSwitch", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def off(String currentSwitch) {
		if (currentSwitch != this.currentSwitch) {
			this.currentSwitch = currentSwitch
			println("The light is changed to $currentSwitch!")
			sendEvent([name: "switch", value: "$currentSwitch", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "level") {
			return level
		} else if (deviceFeature == "colorTemperature") {
			return colorTemperature
		} else if (deviceFeature == "switch") {
			return currentSwitch
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "level") {
			return level
		} else if (deviceFeature == "colorTemperature") {
			return colorTemperature
		} else if (deviceFeature == "switch") {
			return currentSwitch
		}
	}
}
