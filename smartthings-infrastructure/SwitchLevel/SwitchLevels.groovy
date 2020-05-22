//Create a class for switch level
package SwitchLevel
import Timer.SimulatedTimer

public class SwitchLevels {
	int deviceNumbers	
	List switchLevels
	def timers
	def sendEvent

	//If we have only one device
	private String id = "switchLevelID0"
	private String label = "switchLevel0"
	private String displayName = "switchLevel0"
	private int currentLevel = 50
	private int rate = 50
	private int hue = 30
	private int saturation = 70
	private String switchState = "off"
	private String currentSwitch = "off"
	private String switchLatestValue = "off"

	SwitchLevels(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.switchLevels = []

		if (init) {
			this.currentLevel = 50
			this.rate = 50
			this.hue = 30
			this.saturation = 70
			this.switchState = "on"
			this.currentSwitch = "on"
			this.switchLatestValue = "on"
		} else {
			this.currentLevel = 60
			this.rate = 60
			this.hue = 50
			this.saturation = 90
			this.switchState = "off"
			this.currentSwitch = "off"
			this.switchLatestValue = "off"
		}
		switchLevels.add(new SwitchLevel(sendEvent, id, label, displayName, this.currentLevel, this.hue, this.saturation, this.switchState, this.switchLatestValue))
	}

	//Methods for closures
	def count(Closure Input) {
		switchLevels.count(Input)
	}
	def size() {
		switchLevels.size()
	}
	def each(Closure Input) {
		switchLevels.each(Input)
	}
	def find(Closure Input) {
		switchLevels.find(Input)
	}
	def sort(Closure Input) {
		switchLevels.sort(Input)
	}
	def collect(Closure Input) {
		switchLevels.collect(Input)
	}

	//By Apps
	def setColor(LinkedHashMap metaData) {
		if ((this.currentLevel != metaData["level"]) || (this.hue != metaData["hue"]) || (this.saturation != metaData["saturation"])) {
			this.currentLevel = metaData["level"]
			this.rate = metaData["level"]
			this.hue = metaData["hue"]
			this.saturation = metaData["saturation"]
			switchLevels[0].setColor(metaData)
		}
	}
	
	def setLevel(String level) {
		def newLevel = level.toInteger()
		setLevel(newLevel)
	}

	def setLevel(int level) {
		if (this.currentLevel != level) {
			switchLevels[0].setLevel(level)
			this.currentLevel = level
			this.rate = level
		}
	}
	
	def setLevel(long level) {
		if (this.currentLevel != level) {
			switchLevels[0].setLevel(level)
			this.currentLevel = level
			this.rate = level
		}
	}

	def on() {
		switchLatestValue = "on"
		switchState = "on"
		currentSwitch = "on"
		switchLevels[0].on()
	}

	def on(LinkedHashMap metaData) {
		def task = timers.runAfter(metaData["delay"]) {
			switchLatestValue = "on"
			switchState = "on"
			currentSwitch = "on"
			switchLevels[0].on()
		}
	}

	def off() {
		switchLatestValue = "off"
		switchState = "off"
		currentSwitch = "off"
		switchLevels[0].off()
	}

	def off(LinkedHashMap metaData) {
		def task = timers.runAfter(metaData["delay"]) {
			switchLatestValue = "off"
			switchState = "off"
			currentSwitch = "off"
			switchLevels[0].off()
		}
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["name"] == "switch") {
			if (eventDataMap["value"] != switchLevels[0].switchState) {
				this.switchState = eventDataMap["value"]
				this.switchLatestValue = eventDataMap["value"]
				this.currentSwitch = eventDataMap["value"]
				switchLevels[0].setValue(eventDataMap["value"], "switch")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "level") {
			if (eventDataMap["value"].toInteger() != switchLevels[0].currentLevel) {
				this.currentLevel = eventDataMap["value"].toInteger()
				this.rate = eventDataMap["value"].toInteger()
				switchLevels[0].setValue(eventDataMap["value"], "level")
				sendEvent(eventDataMap)
			}
		}
	}

	def currentValue(String deviceFeature) {
		switchLevels[0].currentValue(deviceFeature)
	}
	
	def latestValue(String deviceFeature) {
		switchLevels[0].latestValue(deviceFeature)
	}
	

	def getAt(int ix) {
		switchLevels[ix]
	}
}
