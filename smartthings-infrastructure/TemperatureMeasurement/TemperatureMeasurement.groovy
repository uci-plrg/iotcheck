//Create a class for temperature measurement
package TemperatureMeasurement
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class TemperatureMeasurement {
	private String id
	private String label
	private String displayName
	private int temperature
	private int currentTemperature

	TemperatureMeasurement(String id, String label, String displayName, int temperature) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.temperature = temperature
		this.currentTemperature = temperature
	}

	def eventsSince() {
		def evtTemp = [[name: "temperature", value: this.temperature.toString(), deviceId: "temperatureMeasurementID0", descriptionText: "",
				displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def init = Verify.getInt(0,1)
		def evtToSend = []
		if (init == 0) {//return empty set
			return evtToSend
		} else if (init == 1) {//send one open event
			evtTemp.each{
				evtToSend.add(it)
			}
			return evtToSend
		}
	}

	//By Model Checker
	def setValue(String value) {
		println("the temperature is changed to $value!")
		this.temperature = value.toInteger()
		this.currentTemperature = value.toInteger()
	}

}
