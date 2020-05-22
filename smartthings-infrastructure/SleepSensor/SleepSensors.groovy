//Create a class for sleep sensor
package SleepSensor
import Timer.SimulatedTimer

public class SleepSensors {
	private int deviceNumbers
	private List sleepSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "sleepSensorID0"
	private String label = "sleepSensor0"
	private String displayName = "sleepSensor0"
	private String sleeping = "sleeping"

		
	SleepSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.sleepSensors = []

		if (init) {
			this.sleeping = "sleeping"
		} else {
			this.sleeping = "not sleeping"
		}
		sleepSensors.add(new SleepSensor(id, label, displayName, this.sleeping))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != sleepSensors[0].sleeping) {
			this.sleeping = eventDataMap["value"]
			sleepSensors[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		sleepSensors.count(Input)
	}
	def size() {
		sleepSensors.size()
	}
	def each(Closure Input) {
		sleepSensors.each(Input)
	}
	def find(Closure Input) {
		sleepSensors.find(Input)
	}
	def sort(Closure Input) {
		sleepSensors.sort(Input)
	}
	def collect(Closure Input) {
		sleepSensors.collect(Input)
	}

	def getAt(int ix) {
		sleepSensors[ix]
	}
}
