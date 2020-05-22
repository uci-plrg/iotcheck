//Create a class for door control device
package DoorControl
import Timer.SimulatedTimer

public class DoorControl {
	private String id
	private String label
	private String displayName
	private String doorState
	private String doorLatestValue
	def sendEvent	
	def timers
	

	DoorControl(Closure sendEvent, String id, String label, String displayName, String doorState, String doorLatestValue) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.id = id
		this.label = label
		this.displayName = displayName
		this.doorState = doorState
		this.doorLatestValue = doorLatestValue
	}

	//By Apps
	def open() {
		if (doorState != "open") {
			println("the door with id:$id is open!")
			this.doorLatestValue = "open"
			this.doorState = "open"
			sendEvent([name: "doorControl", value: "open", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def open(LinkedHashMap metaData) {
		if (doorState != "open") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the door with id:$id is open!")
				this.doorLatestValue = "open"
				this.doorState = "open"
				sendEvent([name: "doorControl", value: "open", deviceId: this.id, descriptionText: "",
				    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}

	def close() {
		if (doorState != "closed") {
			println("the door with id:$id is closed!")
			this.doorLatestValue = "closed"
			this.doorState = "closed"
			sendEvent([name: "doorControl", value: "closed", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def close(LinkedHashMap metaData) {
		if (doorState != "closed") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the door with id:$id is closed!")
				this.doorLatestValue = "closed"
				this.doorState = "closed"
				sendEvent([name: "doorControl", value: "closed", deviceId: this.id, descriptionText: "",
				    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}

	//By Model Checker
	def setValue(String value) {
		println("the door with id:$id is $value!")
		this.doorLatestValue = value
		this.doorState = value
	}
	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "status") {
			return doorState
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "status") {
			return doorLatestValue
		}
	}
}
