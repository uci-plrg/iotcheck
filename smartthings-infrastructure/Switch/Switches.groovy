//Create a class for switch device
package Switch
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify


public class Switches {
	int deviceNumbers	
	List switches
	def timers
	def sendEvent

	//If we have only one device
	private String id = "switchID0"
	private String label = "switch0"
	private String displayName = "switch0"
	private String switchState = "off"
	private String currentSwitch = "off"
	private int currentLevel = 50
	private String switchLatestValue = "off"

	Switches(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.switches = []

		if (init) {
			this.switchState = "off"
			this.currentSwitch = "off"
			this.switchLatestValue = "off"
			this.currentLevel = 50
		} else {
			this.switchState = "on"
			this.currentSwitch = "on"
			this.switchLatestValue = "on"
			this.currentLevel = 60
		}
		switches.add(new Switch(sendEvent, id, label, displayName, this.switchState, this.currentSwitch, this.currentLevel, this.switchLatestValue))
	}

	//Methods for closures
	def count(Closure Input) {
		switches.count(Input)
	}
	def size() {
		switches.size()
	}
	def each(Closure Input) {
		switches.each(Input)
	}
	def eachWithIndex(Closure Input) {
		switches.eachWithIndex(Input)
	}
	def find(Closure Input) {
		switches.find(Input)
	}
	def sort(Closure Input) {
		switches.sort(Input)
	}
	def collect(Closure Input) {
		switches.collect(Input)
	}

	//By Apps
	def eventsSince(Date dateObj, LinkedHashMap metaData) {
		return switches[0].eventsSince()
	}

	def setLevel(int level) {
		currentLevel = level
		switches[0].setLevel(level)
	}

	def on() {
		switchLatestValue = "on"
		switchState = "on"
		currentSwitch = "on"
		switches[0].on()
	}

	def on(LinkedHashMap metaData) {
		def task = timers.runAfter(metaData["delay"]) {
			switchLatestValue = "on"
			switchState = "on"
			currentSwitch = "on"
			switches[0].on()
		}
	}

	def off() {
		switchLatestValue = "off"
		switchState = "off"
		currentSwitch = "off"
		switches[0].off()
	}

	def off(LinkedHashMap metaData) {
		def task = timers.runAfter(metaData["delay"]) {
			switchLatestValue = "off"
			switchState = "off"
			currentSwitch = "off"
			switches[0].off()
		}
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != switches[0].switchState) {
			this.switchState = eventDataMap["value"]
			this.switchLatestValue = eventDataMap["value"]
			switches[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}


	def currentValue(String deviceFeature) {
		switches[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		switches[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		switches[ix]
	}
}
