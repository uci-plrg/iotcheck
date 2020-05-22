//Create a class for temperature measurement
package TemperatureMeasurement
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class TemperatureMeasurements {
	private int deviceNumbers
	private List temperatureMeasurements
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "temperatureMeasurementID0"
	private String label = "temperatureMeasurement0"
	private String displayName = "temperatureMeasurement0"
	private int temperature = 40
	private int currentTemperature = 40

	TemperatureMeasurements(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.temperatureMeasurements = []

		if (init) {
			this.temperature = 40
			this.currentTemperature = 40
		} else {
			this.temperature = 60
			this.currentTemperature = 60
		}	
		temperatureMeasurements.add(new TemperatureMeasurement(id, label, displayName, this.temperature))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"].toInteger() != temperatureMeasurements[0].temperature) {
			this.temperature = eventDataMap["value"].toInteger()
			this.currentTemperature = eventDataMap["value"].toInteger()
			temperatureMeasurements[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	def eventsSince(Date dateObj) {
		return temperatureMeasurements[0].eventsSince()
	}

	//Methods for closures
	def count(Closure Input) {
		temperatureMeasurements.count(Input)
	}
	def size() {
		temperatureMeasurements.size()
	}
	def each(Closure Input) {
		temperatureMeasurements.each(Input)
	}
	def find(Closure Input) {
		temperatureMeasurements.find(Input)
	}
	def sort(Closure Input) {
		temperatureMeasurements.sort(Input)
	}
	def collect(Closure Input) {
		temperatureMeasurements.collect(Input)
	}

	def getAt(int ix) {
		temperatureMeasurements[ix]
	}
}
