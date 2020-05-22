//Create a class for presence sensor
package PresenceSensor
import Timer.SimulatedTimer

public class PresenceSensors {
	private int deviceNumbers
	private List presenceSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "presenceSensorID0"
	private String label = "presenceSensor0"
	private String displayName = "presenceSensor0"
	private String presence = "not present"
	private String currentPresence = "not present"
	private String presenceLatestValue = "not present"

		
	PresenceSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.presenceSensors = []

		if (init) {
			this.presence = "not present"
			this.currentPresence = "not present"
			this.presenceLatestValue = "not present"
		} else {
			this.presence = "present"
			this.currentPresence = "present"
			this.presenceLatestValue = "present"
		}
		presenceSensors.add(new PresenceSensor(id, label, displayName, this.presence, this.presenceLatestValue))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != presenceSensors[0].presence) {
			this.presenceLatestValue = eventDataMap["value"]
			this.presence = eventDataMap["value"]
			this.currentPresence = eventDataMap["value"]
			presenceSensors[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		presenceSensors.count(Input)
	}
	def size() {
		presenceSensors.size()
	}
	def each(Closure Input) {
		presenceSensors.each(Input)
	}
	def find(Closure Input) {
		presenceSensors.find(Input)
	}
	def sort(Closure Input) {
		presenceSensors.sort(Input)
	}
	def collect(Closure Input) {
		presenceSensors.collect(Input)
	}

	def currentState(String deviceFeature) {
		presenceSensors[0].currentState(deviceFeature)	
	}

	def currentValue(String deviceFeature) {
		presenceSensors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		presenceSensors[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def statesSince(String info, Date dateObj) {
		return presenceSensors[0].statesSince()
	}

	def eventsSince(Date dateObj) {
		return presenceSensors[0].statesSince()
	}

	def getAt(int ix) {
		presenceSensors[ix]
	}
}
