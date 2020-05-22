//Create a class for battery
package Battery
import Timer.SimulatedTimer

public class Battery {
	private String id
	private String label
	private String displayName
	private int battery
	private int currentBattery
	private int batteryLatestValue

	Battery(String id, String label, String displayName, int battery) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.battery = battery
		this.currentBattery = battery
		this.batteryLatestValue = battery
	}

	//By Model Checker
	def setValue(String value) {
		println("the battery level with id:$id is changed to $value!")
		this.battery = value.toInteger()
		this.currentBattery = value.toInteger()
		this.batteryLatestValue = value.toInteger()
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "battery") {
			return battery
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "battery") {
			return batteryLatestValue
		}
	}

}
