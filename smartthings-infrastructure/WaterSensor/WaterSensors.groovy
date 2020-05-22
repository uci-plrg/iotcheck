//Create a class for water sensor
package WaterSensor
import Timer.SimulatedTimer

public class WaterSensors {
	private int deviceNumbers
	private List waterSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "waterSensorID0"
	private String label = "waterSensor0"
	private String displayName = "waterSensor0"
	private String water = "dry"

		
	WaterSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.waterSensors = []

		if (init) {
			this.water = "dry"
		} else {
			this.water = "wet"
		}
		waterSensors.add(new WaterSensor(id, label, displayName, this.water))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != waterSensors[0].water) {
			this.water = eventDataMap["value"]
			waterSensors[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		waterSensors.count(Input)
	}
	def size() {
		waterSensors.size()
	}
	def each(Closure Input) {
		waterSensors.each(Input)
	}
	def find(Closure Input) {
		waterSensors.find(Input)
	}
	def sort(Closure Input) {
		waterSensors.sort(Input)
	}
	def collect(Closure Input) {
		waterSensors.collect(Input)
	}

	def getAt(int ix) {
		waterSensors[ix]
	}
}
