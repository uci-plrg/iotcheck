//Create a class for presence sensor
package MotionSensor
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class MotionSensors {
	private int deviceNumbers
	private List motionSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "motionSensorID0"
	private String label = "motionSensor0"
	private String displayName = "motionSensor0"
	private String motion = "inactive"
	private String currentMotion = "inactive"
	private String motionLatestValue = "inactive"

		
	MotionSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.motionSensors = []

		if (init) {
			this.motion = "inactive"
			this.currentMotion = "inactive"
			this.motionLatestValue = "inactive"
		} else {
			this.motion = "active"
			this.currentMotion = "active"
			this.motionLatestValue = "active"
		}
		motionSensors.add(new MotionSensor(id, label, displayName, this.motion, this.motionLatestValue))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != motionSensors[0].motion) {
			this.motionLatestValue = eventDataMap["value"]
			this.motion = eventDataMap["value"]
			this.currentMotion = eventDataMap["value"]
			motionSensors[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		motionSensors.count(Input)
	}
	def size() {
		motionSensors.size()
	}
	def each(Closure Input) {
		motionSensors.each(Input)
	}
	def find(Closure Input) {
		motionSensors.find(Input)
	}
	def sort(Closure Input) {
		motionSensors.sort(Input)
	}
	def collect(Closure Input) {
		motionSensors.collect(Input)
	}
	
	def currentState(String deviceFeature) {
		currentValue(deviceFeature)
	}

	def currentValue(String deviceFeature) {
		motionSensors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		motionSensors[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def statesSince(String info, Date dateObj) {
		return motionSensors[0].statesSince()
	}

	def eventsSince(Date dateObj) {
		return motionSensors[0].statesSince()
	}

	def getAt(int ix) {
		motionSensors[ix]
	}
}
