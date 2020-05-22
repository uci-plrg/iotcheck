//Create a class for switch device
package Switch
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify


public class Switch {
	private String id
	private String label
	private String displayName
	private String switchState
	private String currentSwitch
	private int currentLevel
	private String switchLatestValue
	def sendEvent	
	def timers
	

	Switch(Closure sendEvent, String id, String label, String displayName, String switchState, String currentSwitch, int currentLevel, String switchLatestValue) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.currentSwitch = currentSwitch
		this.currentLevel = currentLevel
		this.id = id
		this.label = label
		this.displayName = displayName
		this.switchState = switchState
		this.switchLatestValue = switchLatestValue
	}

	def eventsSince() {
		def evtOn = [[name: "switch", value: "on", deviceId: "switchID0", descriptionText: "",
				displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtOff = [[name: "switch", value: "off", deviceId: "switchID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def init = Verify.getInt(0,4)
		def evtToSend = []
		if (init == 0) {//return empty set
			return evtToSend
		} else if (init == 1) {//send one open event
			evtOn.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 2) {//send two open events
			evtOn.each{
				evtToSend.add(it)
			}
			evtOn.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 3) {//send one closed event
			evtOff.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 4) {//send two closed events
			evtOff.each{
				evtToSend.add(it)
			}
			evtOff.each{
				evtToSend.add(it)
			}
			return evtToSend
		}
	}

	//By Apps
	def setLevel(int level) {
		if (this.currentLevel != level) {
			println("the switch with id:$id is setted to level $level!")
			this.currentLevel = level
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
	def setValue(String value) {
		println("the switch with id:$id is $value!")
		this.switchLatestValue = value
		this.switchState = value
		this.currentSwitch = value
	}
	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "switch") {
			return switchState
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "switch") {
			return switchLatestValue
		}
	}
}
