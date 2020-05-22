//Create a class for relay switch device
package RelaySwitch
import Timer.SimulatedTimer

public class RelaySwitches {
	int deviceNumbers	
	List relaySwitches
	def timers
	def sendEvent

	//If we have only one device
	private String id = "relaySwitchID0"
	private String label = "relaySwitch0"
	private String displayName = "relaySwitch0"
	private String switchState = "off"
	private String currentSwitch = "off"
	private String switchLatestValue = "off"

	RelaySwitches(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.relaySwitches = []

		if (init) {
			this.switchState = "off"
			this.currentSwitch = "off"
			this.switchLatestValue = "off"
		} else {
			this.switchState = "on"
			this.currentSwitch = "on"
			this.switchLatestValue = "on"
		}
		relaySwitches.add(new RelaySwitch(sendEvent, id, label, displayName, this.switchState, this.currentSwitch, this.switchLatestValue))
	}

	//Methods for closures
	def count(Closure Input) {
		relaySwitches.count(Input)
	}
	def size() {
		relaySwitches.size()
	}
	def each(Closure Input) {
		relaySwitches.each(Input)
	}
	def find(Closure Input) {
		relaySwitches.find(Input)
	}
	def sort(Closure Input) {
		relaySwitches.sort(Input)
	}
	def collect(Closure Input) {
		relaySwitches.collect(Input)
	}

	//By Apps
	def on() {
		if (switchState != "on") {
			switchLatestValue = "on"
			switchState = "on"
			currentSwitch = "on"
			relaySwitches[0].on()
		}
	}

	def on(LinkedHashMap metaData) {
		if (switchState != "on") {
			def task = timers.runAfter(metaData["delay"]) {
				switchLatestValue = "on"
				switchState = "on"
				currentSwitch = "on"
				relaySwitches[0].on()
			}
		}
	}

	def off() {
		if (switchState != "off") {
			switchLatestValue = "off"
			switchState = "off"
			currentSwitch = "off"
			relaySwitches[0].off()
		}
	}

	def off(LinkedHashMap metaData) {
		if (switchState != "off") {
			def task = timers.runAfter(metaData["delay"]) {
				switchLatestValue = "off"
				switchState = "off"
				currentSwitch = "off"
				relaySwitches[0].off()
			}
		}
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != relaySwitches[0].switchState) {
			this.switchState = eventDataMap["value"]
			this.switchLatestValue = eventDataMap["value"]
			relaySwitches[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}


	def currentValue(String deviceFeature) {
		relaySwitches[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		relaySwitches[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		relaySwitches[ix]
	}
}
