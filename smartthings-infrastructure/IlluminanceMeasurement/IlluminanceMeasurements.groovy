//Create a class for illuminance measurement
package IlluminanceMeasurement
import Timer.SimulatedTimer

public class IlluminanceMeasurements {
	private int deviceNumbers
	private List illuminanceMeasurements
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "illuminanceMeasurementsID0"
	private String label = "illuminanceMeasurements0"
	private String displayName = "illuminanceMeasurements0"
	private int illuminance = 5
	private int currentIlluminance = 5

		
	IlluminanceMeasurements(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.illuminanceMeasurements = []

		if (init) {
			this.illuminance = 20000
			this.currentIlluminance = 20000
		} else {
			this.illuminance = 5
			this.currentIlluminance = 5
		}

		illuminanceMeasurements.add(new IlluminanceMeasurement(id, label, displayName, this.illuminance))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"].toInteger() != illuminanceMeasurements[0].illuminance) {
			this.illuminance = eventDataMap["value"].toInteger()
			this.currentIlluminance = eventDataMap["value"].toInteger()
			illuminanceMeasurements[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		illuminanceMeasurements.count(Input)
	}
	def size() {
		illuminanceMeasurements.size()
	}
	def each(Closure Input) {
		illuminanceMeasurements.each(Input)
	}
	def find(Closure Input) {
		illuminanceMeasurements.find(Input)
	}
	def sort(Closure Input) {
		illuminanceMeasurements.sort(Input)
	}
	def collect(Closure Input) {
		illuminanceMeasurements.collect(Input)
	}


	def currentValue(String deviceFeature) {
		illuminanceMeasurements[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		illuminanceMeasurements[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		illuminanceMeasurements[ix]
	}
}
