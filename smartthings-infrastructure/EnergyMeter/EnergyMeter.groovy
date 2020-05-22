//Create a class for energy meter
package EnergyMeter
import Timer.SimulatedTimer

public class EnergyMeter {
	private String id
	private String label
	private String displayName
	private int energy
	private int currentEnergy
	private String status

	EnergyMeter(String id, String label, String displayName, int energy, String status) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.energy = energy
		this.status = status
	}

	//By Model Checker
	def setValue(String value) {
		println("the enery is changed to $value!")
		this.energy = value.toInteger()
		this.currentEnergy = value.toInteger()
	}

	def reset() {
		if (status != "on") {
			status = "on"
			println("the energy meter is on!")
		}
	}

	def off() {
		if (status != "off") {
			status = "off"
			println("the energy meter is off!")
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "energy") {
			return energy
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "energy") {
			return energy
		}
	}
}
