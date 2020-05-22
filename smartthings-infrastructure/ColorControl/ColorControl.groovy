//Create a class for color control
package ColorControl
import Timer.SimulatedTimer


public class ColorControl {
	def sendEvent
	private String id
	private String label
	private String displayName
	private String color
	private String currentColor
	private String currentSwitch
	private int level
	private int currentLevel
	private int hue
	private int currentHue
	private int saturation
	private int currentSaturation
	private int colorTemperature
	
	ColorControl(Closure sendEvent, String id, String label, String displayName, String color, int hue, int saturation, int level, String currentSwitch, int colorTemperature) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.color = color
		this.currentColor = color
		this.hue = hue
		this.currentHue = hue
		this.saturation = saturation
		this.currentSaturation = saturation
		this.level = level
		this.currentLevel = level
		this.currentSwitch = currentSwitch
		this.colorTemperature = colorTemperature
		this.sendEvent = sendEvent
	}
	
	//By model checker
	def setValue(String value, String name) {
		if ((name == "color") && (value != this.color)) {
			this.color = value
			this.currentColor = value
			println("the color of the light is changed to $value!")
		} else if ((name == "hue") && (value != this.hue)) {
			this.hue = value.toInteger()
			this.currentHue = value.toInteger()
			println("The hue level of the light is changed to $value!")
		} else if ((name == "saturation") && (value != this.saturation)) {
			this.saturation = value.toInteger()
			this.currentSaturation = value.toInteger()
			println("The saturation level of the light is changed to $value!")
		} else if ((name == "level") && (value != this.level)) {
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
	def setColor(LinkedHashMap metaData) {
		def hexColor = metaData.hex
		def newColor
		switch (hexColor) {
			case "#0000FF":
				newColor = "Blue"
				break;
			case "#00FF00":
				newColor = "Green"
				break;
			case "#FFFF00":
				newColor = "Yellow"
				break;
			case "#FF6000":
				newColor = "Orange"
				break;
			case "#BF7FBF":
				newColor = "Purple"
				break;
			case "#FF5F5F":
				newColor = "Pink"
				break;
			case "#FF0000":
				newColor = "Red"
				break;
			default:
				newColor = "Blue"
				break;
		}
		if (newColor != this.color) {
			this.currentColor = newColor
			this.color = newColor
			println("The color of the light is changed to $newColor!")
			sendEvent([name: "color", value: "$newColor", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setColor(String color) {
		if (color != this.color) {
			this.currentColor = color
			this.color = color
			println("The color of the light is changed to $color!")
			sendEvent([name: "color", value: "$color", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setHue(int hue) {
		if (hue != this.hue) {
			this.hue = hue
			this.currentHue = hue
			println("The hue level of the light is changed to $hue!")
			sendEvent([name: "hue", value: "$hue", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	
	def setHue(double hue) {
		if (hue != this.hue) {
			this.hue = hue
			this.currentHue = hue
			println("The hue level of the light is changed to $hue!")
			sendEvent([name: "hue", value: "$hue", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setSaturation(int saturation) {
		if (saturation != this.saturation) {
			this.currentSaturation = saturation
			this.saturation = saturation
			println("The saturation level of the light is changed to $saturation!")
			sendEvent([name: "saturation", value: "$saturation", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	
	def setSaturation(double saturation) {
		if (saturation != this.saturation) {
			this.currentSaturation = saturation
			this.saturation = saturation
			println("The saturation level of the light is changed to $saturation!")
			sendEvent([name: "saturation", value: "$saturation", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}


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

	def on() {
		if (this.currentSwitch != "on") {
			this.currentSwitch = currentSwitch
			println("The light is changed to on!")
			sendEvent([name: "switch", value: "on", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def off() {
		if (this.currentSwitch != "off") {
			this.currentSwitch = currentSwitch
			println("The light is changed to off!")
			sendEvent([name: "switch", value: "off", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "color") {
			return color
		} else if (deviceFeature == "saturation") {
			return saturation
		} else if (deviceFeature == "hue") {
			return hue
		} else if (deviceFeature == "level") {
			return level
		} else if (deviceFeature == "colorTemperature") {
			return colorTemperature
		} else if (deviceFeature == "switch") {
			return currentSwitch
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "color") {
			return color
		} else if (deviceFeature == "saturation") {
			return saturation
		} else if (deviceFeature == "hue") {
			return hue
		} else if (deviceFeature == "level") {
			return level
		} else if (deviceFeature == "colorTemperature") {
			return colorTemperature
		} else if (deviceFeature == "switch") {
			return currentSwitch
		}
	}
}
