//Create a class for color control
package ColorControl
import Timer.SimulatedTimer

public class ColorControls {
	private int deviceNumbers
	private List colorControls
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "colorControlID0"
	private String label = "colorControl0"
	private String displayName = "colorControl0"
	private String color = "Red"
	private String currentColor = "Red"
	private String currentSwitch = "off"
	private int level = 50
	private int currentLevel = 50
	private int hue = 50
	private int currentHue = 50
	private int saturation = 50
	private int currentSaturation = 50
	private int colorTemperature = 15000
	private boolean colorChanged = false
	

	ColorControls(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.deviceNumbers = deviceNumbers
		this.colorControls = []

		if (init) {
			this.level = 20
			this.currentLevel = 20
			this.hue = 30
			this.currentHue = 30
			this.saturation = 40
			this.currentSaturation = 40
			this.colorTemperature = 10000
			this.color = "Red"
			this.currentSwitch = "off"
		} else {
			this.level = 50
			this.currentLevel = 50
			this.hue = 50
			this.currentHue = 50
			this.saturation = 50
			this.currentSaturation = 50
			this.colorTemperature = 15000
			this.color = "Blue"
			this.currentSwitch = "on"
		}
		colorControls.add(new ColorControl(sendEvent, id, label, displayName, this.color, this.hue, this.saturation, this.level, this.currentSwitch, this.colorTemperature))
	}

	//Methods for closures
	def count(Closure Input) {
		colorControls.count(Input)
	}
	def size() {
		colorControls.size()
	}
	def each(Closure Input) {
		colorControls.each(Input)
	}
	def find(Closure Input) {
		colorControls.find(Input)
	}
	def sort(Closure Input) {
		colorControls.sort(Input)
	}
	def collect(Closure Input) {
		colorControls.collect(Input)
	}

	//By model checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["name"] == "color") {
			if (eventDataMap["value"] != colorControls[0].color) {
				this.currentColor = eventDataMap["value"]
				this.color = eventDataMap["value"]
				colorControls[0].setValue(eventDataMap["value"], "color")
				sendEvent(eventDataMap)
			}	
		} else if (eventDataMap["name"] == "hue") {
			if (eventDataMap["value"].toInteger() != colorControls[0].hue) {
				this.hue = eventDataMap["value"].toInteger()
				this.currentHue = eventDataMap["value"].toInteger()
				colorControls[0].setValue(eventDataMap["value"], "hue")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "saturation") {
			if (eventDataMap["value"].toInteger() != colorControls[0].saturation) {
				this.saturation = eventDataMap["value"].toInteger()
				this.currentSaturation = eventDataMap["value"].toInteger()
				colorControls[0].setValue(eventDataMap["value"], "saturation")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "switch") {
			if (eventDataMap["value"] != colorControls[0].currentSwitch) {
				this.currentSwitch = eventDataMap["value"]
				colorControls[0].setValue(eventDataMap["value"], "switch")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "colorTemperature") {
			if (eventDataMap["value"].toInteger() != colorControls[0].colorTemperature) {
				this.colorTemperature = eventDataMap["value"].toInteger()
				colorControls[0].setValue(eventDataMap["value"], "colorTemperature")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "level") {
			if (eventDataMap["value"].toInteger() != colorControls[0].level) {
				this.currentLevel = eventDataMap["value"].toInteger() 
				this.level = eventDataMap["value"].toInteger()
				colorControls[0].setValue(eventDataMap["value"], "level")
				sendEvent(eventDataMap)
			}
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
			colorControls[0].setColor(newColor)
			if (this.colorChanged) {
				this.colorChanged = false
			} else {
				this.colorChanged = true
			}
		}
		def newLevel = metaData.level
		def newHue = metaData.hue
		def newSaturation = metaData.saturation
		if (newLevel != null) {
			setLevel(newLevel)
		}
		if (newHue != null) {
			setHue(newHue)
		}
		if (newSaturation != null) {
			setSaturation(newSaturation)
		}
	}

	def setColor(String color) {
		if (color != this.color) {
			this.currentColor = color
			this.color = color
			colorControls[0].setColor(color)			
		}
	}

	def setHue(int hue) {
		if (hue != this.hue) {
			this.hue = hue
			this.currentHue = hue
			colorControls[0].setHue(hue)
		}
	}
	
	def setHue(double hue) {
		if (hue != this.hue) {
			this.hue = hue
			this.currentHue = hue
			colorControls[0].setHue((int) hue)
		}
	}

	def setSaturation(int saturation) {
		if (saturation != this.saturation) {
			this.currentSaturation = saturation
			this.saturation = saturation
			colorControls[0].setSaturation(saturation)			
		}	
	}
	
	def setSaturation(double saturation) {
		if (saturation != this.saturation) {
			this.currentSaturation = saturation
			this.saturation = saturation
			colorControls[0].setSaturation(saturation)			
		}	
	}

	def setLevel(int level) {
		if (level != this.level) {
			this.currentLevel = level
			this.level = level
			colorControls[0].setLevel(level)
		}
	}

	def setLevel(long level) {
		if (level != this.level) {
			this.currentLevel = level
			this.level = level
			colorControls[0].setLevel(level)
		}
	}

	def setColorTemperature(int colorTemperature) {
		if (colorTemperature != this.colorTemperature) {
			this.colorTemperature = colorTemperature
			colorControls[0].setColorTemperature(colorTemperature)			
		}
	}

	def setColorTemperature(long colorTemperature) {
		if (colorTemperature != this.colorTemperature) {
			this.colorTemperature = colorTemperature
			colorControls[0].setColorTemperature(colorTemperature)			
		}
	}

	def on() {
		if (this.currentSwitch != "on") {
			this.currentSwitch = "on"
			colorControls[0].on()			
		}
	}

	def off() {
		if (this.currentSwitch != "off") {
			this.currentSwitch = "off"
			colorControls[0].off()
		}
	}

	def currentValue(String deviceFeature) {
		colorControls[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		colorControls[0].latestValue(deviceFeature)
	}	

	def getAt(int ix) {
		colorControls[ix]
	}
}
