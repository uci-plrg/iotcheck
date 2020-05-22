//Create a class for relay switch device
package RelaySwitch
import Timer.SimulatedTimer

public class RelaySwitch {
	private String id
	private String label
	private String displayName
	private String switchState
	private String currentSwitch
	private String switchLatestValue
	def sendEvent	
	def timers
	

	RelaySwitch(Closure sendEvent, String id, String label, String displayName, String switchState, String currentSwitch, String switchLatestValue) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.currentSwitch = currentSwitch
		this.id = id
		this.label = label
		this.displayName = displayName
		this.switchState = switchState
		this.switchLatestValue = switchLatestValue
	}

	//By Apps
	def on() {
		if (switchState != "on") {
			println("the relay switch with id:$id is on!")
			this.switchLatestValue = "on"
			this.switchState = "on"
			this.currentSwitch = "on"
			sendEvent([name: "switch", value: "on", deviceId: this.id, descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def on(LinkedHashMap metaData) {
		if (switchState != "on") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the relay switch with id:$id is on!")
				this.switchLatestValue = "on"
				this.switchState = "on"
				this.currentSwitch = "on"
				sendEvent([name: "switch", value: "on", deviceId: this.id, descriptionText: "",
					  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}

	def off() {
		if (switchState != "off") {
			println("the relay switch with id:$id is off!")
			this.switchLatestValue = "off"
			this.switchState = "off"
			this.currentSwitch = "off"
			sendEvent([name: "switch", value: "off", deviceId: this.id, descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def off(LinkedHashMap metaData) {
		if (switchState != "off") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the relay switch with id:$id is off!")
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
		println("the relay switch with id:$id is $value!")
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
