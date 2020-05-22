//Create a class for beacon sensor
package BeaconSensor
import Timer.SimulatedTimer

public class BeaconSensors {
	private int deviceNumbers
	private List beaconSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "beaconSensorID0"
	private String label = "beaconSensor0"
	private String displayName = "beaconSensor0"
	private String presence = "not present"
	private String currentPresence = "not present"
	private String presenceLatestValue = "not present"

		
	BeaconSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.beaconSensors = []

		if (init) {
			this.presence = "not present"
			this.currentPresence = "not present"
			this.presenceLatestValue = "not present"
		} else {
			this.presence = "present"
			this.currentPresence = "present"
			this.presenceLatestValue = "present"
		}
		beaconSensors.add(new BeaconSensor(id, label, displayName, this.presence, this.presenceLatestValue))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != beaconSensors[0].presence) {
			this.presenceLatestValue = eventDataMap["value"]
			this.presence = eventDataMap["value"]
			this.currentPresence = eventDataMap["value"]
			beaconSensors[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		beaconSensors.count(Input)
	}
	def size() {
		beaconSensors.size()
	}
	def each(Closure Input) {
		beaconSensors.each(Input)
	}
	def sort(Closure Input) {
		beaconSensors.sort(Input)
	}
	def find(Closure Input) {
		beaconSensors.find(Input)
	}
	def collect(Closure Input) {
		beaconSensors.collect(Input)
	}


	def currentValue(String deviceFeature) {
		beaconSensors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		beaconSensors[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		beaconSensors[ix]
	}
}
