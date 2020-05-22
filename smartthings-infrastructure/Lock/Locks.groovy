//Create a class for lock device
package Lock
import Timer.SimulatedTimer

public class Locks{
	int deviceNumbers	
	List locks	
	def sendEvent	
	def timers

	//When we have only one device
	private String id = "lockID0"
	private String label = "lock0"
	private String displayName = "lock0"
	private String lockState = "locked"
	private String currentLock = "locked"
	private String lockLatestValue = "locked"

	Locks(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.locks = []

		if (init) {
			this.lockState = "locked"
			this.currentLock = "locked"
			this.lockLatestValue = "locked"
		} else {
			this.lockState = "unlocked"
			this.currentLock = "unlocked"
			this.lockLatestValue = "unlocked"
		}
		locks.add(new Lock(sendEvent,id, label, displayName, this.lockState, this.lockLatestValue))
	}

	//By Apps
	def lock() {
		if (lockState != "locked") {
			//lockLatestValue = lockState
			lockLatestValue = "locked"
			lockState = "locked"
			currentLock = "locked"
			locks[0].lock()
		}
	}

	def lock(LinkedHashMap metaData) {
		if (lockState != "locked") {
			def task = timers.runAfter(metaData["delay"]) {
				//lockLatestValue = lockState
				lockLatestValue = "locked"
				lockState = "locked"
				currentLock = "locked"
				locks[0].lock()
			}
		}
	}

	def unlock() {
		if (lockState != "unlocked") {
			//lockLatestValue = lockState
			lockLatestValue = "unlocked"
			lockState = "unlocked"
			currentLock = "unlocked"
			locks[0].unlock()
		}
	}


	def unlock(LinkedHashMap metaData) {
		if (lockState != "unlocked") {
			def task = timers.runAfter(metaData["delay"]) {
				//lockLatestValue = lockState
				lockLatestValue = "unlocked"
				lockState = "unlocked"
				currentLock = "unlocked"
				locks[0].unlock()
			}
		}
	}

	//Methods for closures
	def count(Closure Input) {
		locks.count(Input)
	}
	def size() {
		locks.size()
	}
	def each(Closure Input) {
		locks.each(Input)
	}
	def find(Closure Input) {
		locks.find(Input)
	}
	def sort(Closure Input) {
		locks.sort(Input)
	}
	def collect(Closure Input) {
		locks.collect(Input)
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != locks[0].lockState) {
			locks[0].setValue(eventDataMap["value"])
			this.lockState = locks[0].lockState
			this.currentLock = locks[0].lockState
			this.lockLatestValue = locks[0].lockLatestValue
			sendEvent(eventDataMap)
		}
	}

	def currentValue(String deviceFeature) {
		locks[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		locks[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		locks[ix]
	}
}

