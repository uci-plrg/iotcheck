//Create a class for relative humidity measurement
package RelativeHumidityMeasurement
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class RelativeHumidityMeasurements {
	private int deviceNumbers
	private List humidityMeasurements
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "humidityMeasurementID0"
	private String label = "humidityMeasurement0"
	private String displayName = "humidityMeasurement0"
	private int humidity = 50
	private int currentHumidity = 50

		
	RelativeHumidityMeasurements(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.humidityMeasurements = []

		if (init) {
			this.humidity = 50
			this.currentHumidity = 50
		} else {
			this.humidity = 60
			this.currentHumidity = 60
		}
		humidityMeasurements.add(new RelativeHumidityMeasurement(id, label, displayName, this.humidity))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"].toInteger() != humidityMeasurements[0].humidity) {
			this.humidity = eventDataMap["value"].toInteger()
			this.currentHumidity = eventDataMap["value"].toInteger()
			humidityMeasurements[0].setValue(eventDataMap["value"])	
			sendEvent(eventDataMap)
		}
	}

	def eventsSince(Date dateObj) {
		return humidityMeasurements[0].eventsSince()
	}

	//Methods for closures
	def count(Closure Input) {
		humidityMeasurements.count(Input)
	}
	def size() {
		humidityMeasurements.size()
	}
	def each(Closure Input) {
		humidityMeasurements.each(Input)
	}
	def find(Closure Input) {
		humidityMeasurements.find(Input)
	}
	def sort(Closure Input) {
		humidityMeasurements.sort(Input)
	}
	def collect(Closure Input) {
		humidityMeasurements.collect(Input)
	}


	def currentValue(String deviceFeature) {
		humidityMeasurements[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		humidityMeasurements[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		humidityMeasurements[ix]
	}
}
