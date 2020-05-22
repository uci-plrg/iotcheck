//Create a class for illuminance measurement
package IlluminanceMeasurement
import Timer.SimulatedTimer

public class IlluminanceMeasurement {
	private String id
	private String label
	private String displayName
	private int illuminance
	private int currentIlluminance

	IlluminanceMeasurement(String id, String label, String displayName, int illuminance) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.illuminance = illuminance
		this.currentIlluminance = illuminance
	}

	//By Model Checker
	def setValue(String value) {
		println("the illuminance level is changed to $value!")
		this.illuminance = value.toInteger()
		this.currentIlluminance = value.toInteger()
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "illuminance") {
			return illuminance
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "illuminance") {
			return illuminance
		}
	}

}
