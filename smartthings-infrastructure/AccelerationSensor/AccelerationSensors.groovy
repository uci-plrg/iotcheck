//Create a class for acceleration sensor
package AccelerationSensor
import Timer.SimulatedTimer

public class AccelerationSensors {
	private int deviceNumbers
	private List accelerationSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "accelerationSensorID0"
	private String label = "accelerationSensor0"
	private String displayName = "accelerationSensor0"
	private String acceleration = "inactive"
	private String currentAcceleration = "inactive"
	private String accelerationLatestValue = "inactive"
	

		
	AccelerationSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.accelerationSensors = []

		if (init) {
			this.acceleration = "inactive"
			this.accelerationLatestValue = "inactive"
		} else {
			this.acceleration = "active"
			this.accelerationLatestValue = "active"
		}
		accelerationSensors.add(new AccelerationSensor(id, label, displayName, this.acceleration, this.accelerationLatestValue))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != accelerationSensors[0].acceleration) {
			this.accelerationLatestValue = eventDataMap["value"]
			this.acceleration = eventDataMap["value"]
			this.currentAcceleration = eventDataMap["value"]
			accelerationSensors[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//methods
	def eventsSince(Date dateObj) {
		return accelerationSensors[0].eventsSince()
	}
		
	def statesSince(String info, Date dateObj) {
		return accelerationSensors[0].statesSince()
	}

	//Methods for closures
	def count(Closure Input) {
		accelerationSensors.count(Input)
	}
	def size() {
		accelerationSensors.size()
	}
	def each(Closure Input) {
		accelerationSensors.each(Input)
	}
	def sort(Closure Input) {
		accelerationSensors.each(Input)
	}
	def find(Closure Input) {
		accelerationSensors.find(Input)
	}
	def collect(Closure Input) {
		accelerationSensors.collect(Input)
	}


	def currentValue(String deviceFeature) {
		accelerationSensors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		accelerationSensors[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		accelerationSensors[ix]
	}
}
