//Create a class for switch level
package SwitchLevel
import Timer.SimulatedTimer

public class SwitchLevel {
	private String id
	private String label
	private String displayName
	private String switchState
	private String currentSwitch
	private int currentLevel
	private int rate
	private int hue
	private int saturation
	private String switchLatestValue
	def sendEvent	
	def timers
	

	SwitchLevel(Closure sendEvent, String id, String label, String displayName, int level, int hue, int saturation, String switchState, String switchLatestValue) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.id = id
		this.label = label
		this.displayName = displayName
		this.currentLevel = level
		this.rate = level
		this.hue = hue
		this.saturation = saturation
		this.switchState = switchState
		this.currentSwitch = switchState
		this.switchLatestValue = switchLatestValue
	}

	//By Apps
	def setColor(LinkedHashMap metaData) {
		if ((this.currentLevel != metaData["level"]) || (this.hue != metaData["hue"]) || (this.saturation != metaData["saturation"])) {
			this.currentLevel = metaData["level"]
			this.rate = metaData["level"]
			this.hue = metaData["hue"]
			this.saturation = metaData["saturation"]
			println("the switch with id:$id is setted to level ${this.currentLevel} and hue to $hue and saturation to $saturation!")
			sendEvent([name: "level", value: "${this.currentLevel}", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			sendEvent([name: "hue", value: "$hue", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			sendEvent([name: "saturation", value: "$saturation", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def setLevel(String level) {
		def newLevel = level.toInteger()
		setLevel(newLevel)
	}	

	def setLevel(int level) {
		if (this.currentLevel != level) {
			println("the switch with id:$id is setted to level integer $level!")
			this.currentLevel = level
			this.rate = level
			sendEvent([name: "level", value: "$level", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	
	def setLevel(long level) {
		if (this.currentLevel != level) {
			println("the switch with id:$id is setted to level long $level!")
			this.currentLevel = level
			this.rate = level
			sendEvent([name: "level", value: "$level", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def on() {
		if (this.switchState != "on") {
			println("the switch with id:$id is on!")
			this.switchLatestValue = "on"
			this.switchState = "on"
			this.currentSwitch = "on"
			sendEvent([name: "switch", value: "on", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def on(LinkedHashMap metaData) {
		if (this.switchState != "on") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the switch with id:$id is on!")
				this.switchLatestValue = "on"
				this.switchState = "on"
				this.currentSwitch = "on"
				sendEvent([name: "switch", value: "on", deviceId: this.id, descriptionText: "",
				    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}

	def off() {
		if (this.switchState != "off") {
			println("the switch with id:$id is off!")
			this.switchLatestValue = "off"
			this.switchState = "off"
			this.currentSwitch = "off"
			sendEvent([name: "switch", value: "off", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def off(LinkedHashMap metaData) {
		if (this.switchState != "off") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the switch with id:$id is off!")
				this.switchLatestValue = "off"
				this.switchState = "off"
				this.currentSwitch = "off"
				sendEvent([name: "switch", value: "off", deviceId: this.id, descriptionText: "",
				    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}

	//By Model Checker
	def setValue(String value, String name) {
		if (name == "switch") {
			println("the switch with id:$id is $value!")
			this.switchLatestValue = value
			this.switchState = value
			this.currentSwitch = value
		} else if (name == "level") {
			println("the switch with id:$id is setted to level $value!")
			this.currentLevel = value.toInteger()
			this.rate = value.toInteger()
		}
	}


	def currentValue(String deviceFeature) {
		if (deviceFeature == "level") {
			return currentLevel
		} else if (deviceFeature == "switch") {
			return switchState
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "level") {
			return currentLevel
		} else if (deviceFeature == "switch") {
			return switchState
		}
	}
}
