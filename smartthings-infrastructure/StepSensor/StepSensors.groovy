//Create a class for step sensor
package StepSensor
import Timer.SimulatedTimer

public class StepSensors {
	private int deviceNumbers
	private List stepSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "stepSensorID0"
	private String label = "stepSensor0"
	private String displayName = "stepSensor0"
	private int goal = 1000
	private int steps = 0

		
	StepSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.stepSensors = []

		if (init) {
			this.goal = 50
			this.steps = 35
		} else {
			this.goal = 40
			this.steps = 60
		}
		stepSensors.add(new StepSensor(id, label, displayName, this.steps, this.goal))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["name"] == "steps") {
			if (eventDataMap["value"].toInteger() != stepSensors[0].steps) {
				this.steps = eventDataMap["value"].toInteger()
				stepSensors[0].setValue(eventDataMap["value"], "steps")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "goal") {
			if (eventDataMap["value"].toInteger() != stepSensors[0].goal) {
				this.goal = eventDataMap["value"].toInteger()
				stepSensors[0].setValue(eventDataMap["value"], "goal")
				sendEvent(eventDataMap)
			}
		}
	}

	//Methods for closures
	def count(Closure Input) {
		stepSensors.count(Input)
	}
	def size() {
		stepSensors.size()
	}
	def each(Closure Input) {
		stepSensors.each(Input)
	}
	def find(Closure Input) {
		stepSensors.find(Input)
	}
	def sort(Closure Input) {
		stepSensors.sort(Input)
	}
	def collect(Closure Input) {
		stepSensors.collect(Input)
	}


	def currentValue(String deviceFeature) {
		stepSensors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		stepSensors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		stepSensors[ix]
	}
}
