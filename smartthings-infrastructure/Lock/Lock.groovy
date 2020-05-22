//Create a class for lock device
package Lock
import Timer.SimulatedTimer

public class Lock {
	private String id
	private String label
	private String displayName
	private String lockState
	private String currentLock
	private String lockLatestValue
	def sendEvent	
	def timers


	Lock(Closure sendEvent, String id, String label, String displayName, String lockState, String lockLatestValue) {
		this.id = id
		this.label = label
		this.sendEvent = sendEvent
		this.displayName = displayName
		this.lockState = lockState
		this.currentLock = lockState
		this.lockLatestValue = lockLatestValue
		this.timers = new SimulatedTimer()
	}

	//By Apps
	def lock() {
		if (lockState != "locked") {
			println("the door with id:$id is locked!")
			this.lockLatestValue = "locked"
			this.lockState = "locked"
			this.currentLock = "locked"
			sendEvent([name: "lock", value: "locked", deviceId: this.id, descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def lock(LinkedHashMap metaData) {
		if (lockState != "locked") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the door with id:$id is locked!")
				this.lockLatestValue = "locked"
				this.lockState = "locked"
				this.currentLock = "locked"
				sendEvent([name: "lock", value: "locked", deviceId: this.id, descriptionText: "",
					  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}
	
	def unlock() {
		if (lockState != "unlocked") {
			println("the door with id:$id is unlocked!")
			this.lockLatestValue = "unlocked"
			this.lockState = "unlocked"
			this.currentLock = "unlocked"
			sendEvent([name: "lock", value: "unlocked", deviceId: this.id, descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def unlock(LinkedHashMap metaData) {
		if (lockState != "unlocked") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the door with id:$id is locked!")
				this.lockLatestValue = "unlocked"
				this.lockState = "unlocked"
				this.currentLock = "unlocked"
				sendEvent([name: "lock", value: "unlocked", deviceId: this.id, descriptionText: "",
					  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])				
			}
		}
	}

	//By Model Checker
	def setValue(String value) {
		println("the door with id:$id is $value!")
		this.lockLatestValue = value
		this.lockState = value
		this.currentLock = value
	}
	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "lock") {
			return lockState
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "lock") {
			return lockLatestValue
		}
	}
}
