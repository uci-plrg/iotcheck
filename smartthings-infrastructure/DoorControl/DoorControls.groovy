//Create a class for door control device
package DoorControl
import Timer.SimulatedTimer

public class DoorControls {
	int deviceNumbers	
	List doorControls
	def timers
	def sendEvent

	//If we have only one device
	private String id = "DoorControlID0"
	private String label = "DoorControl0"
	private String displayName = "DoorControl0"
	private String doorState = "closed"
	private String doorLatestValue = "closed"

	DoorControls(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.doorControls = []
		
		if (init) {
			this.doorState = "closed"
			this.doorLatestValue = "closed"
		} else {
			this.doorState = "open"
			this.doorLatestValue = "open"
		}
		doorControls.add(new DoorControl(sendEvent, id, label, displayName, this.doorState, this.doorLatestValue))
	}

	//Methods for closures
	def count(Closure Input) {
		doorControls.count(Input)
	}
	def size() {
		doorControls.size()
	}
	def each(Closure Input) {
		doorControls.each(Input)
	}
	def find(Closure Input) {
		doorControls.find(Input)
	}
	def sort(Closure Input) {
		doorControls.sort(Input)
	}
	def collect(Closure Input) {
		doorControls.collect(Input)
	}

	//By Apps
	def open() {
		if (doorState != "open")
			doorControls[0].open()
	}

	def open(LinkedHashMap metaData) {
		if (doorState != "open") {
			def task = timers.runAfter(metaData["delay"]) {
				doorControls[0].open()
			}
		}
	}

	def close() {
		if (doorState != "closed")
			doorControls[0].close()
	}

	def close(LinkedHashMap metaData) {
		if (doorState != "closed") {
			def task = timers.runAfter(metaData["delay"]) {
				doorControls[0].close()
			}
		}
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != doorControls[0].doorState) {
			this.doorState = eventDataMap["value"]
			this.doorLatestValue = eventDataMap["value"]
			doorControls[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}


	def currentValue(String deviceFeature) {
		doorControls[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		doorControls[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		doorControls[ix]
	}
}
