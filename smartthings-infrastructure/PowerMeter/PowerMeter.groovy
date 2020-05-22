//Create a class for power meter
package PowerMeter
import Timer.SimulatedTimer

public class PowerMeter {
	private String id
	private String label
	private String displayName
	private int power
	private int currentPower

	PowerMeter(String id, String label, String displayName, int power) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.power = power
	}

	//By Model Checker
	def setValue(String value) {
		println("the power is changed to $value!")
		this.power = value.toInteger()
		this.currentPower = value.toInteger()
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "power") {
			return power
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "power") {
			return power
		}
	}

}
