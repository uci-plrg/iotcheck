//Create a class for relative humidity measurement
package RelativeHumidityMeasurement
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class RelativeHumidityMeasurement {
	private String id
	private String label
	private String displayName
	private int humidity
	private int currentHumidity

	RelativeHumidityMeasurement(String id, String label, String displayName, int humidity) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.humidity = humidity
	}

	def eventsSince() {
		def evtHumidity = [[name: "humidity", value: this.humidity.toString(), deviceId: "humidityMeasurementID0", descriptionText: "",
				displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def init = Verify.getInt(0,1)
		def evtToSend = []
		if (init == 0) {//return empty set
			return evtToSend
		} else if (init == 1) {//send one open event
			evtHumidity.each{
				evtToSend.add(it)
			}
			return evtToSend
		}
	}

	//By Model Checker
	def setValue(String value) {
		println("the humidity is changed to $value!")
		this.humidity = value.toInteger()
		this.currentHumidity = value.toInteger()
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "humidity") {
			return humidity
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "humidity") {
			return humidity
		}
	}
}
