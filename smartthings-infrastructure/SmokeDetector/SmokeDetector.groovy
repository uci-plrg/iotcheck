//Create a class for smoke detector
package SmokeDetector
import Timer.SimulatedTimer

public class SmokeDetector {
	private String id
	private String label
	private String displayName
	private String smoke
	private String currentSmokeValue
	private String smokeLatestValue
	private String carbonMonoxide
	private String currentCarbonMonoxideValue
	private String carbonMonoxideLatestValue
	private int battery
	private int batteryLatestValue

	SmokeDetector(String id, String label, String displayName, String smoke, String smokeLatestValue, String carbonMonoxide, String carbonMonoxideLatestValue, int battery) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.smoke = smoke
		this.currentSmokeValue = smoke
		this.smokeLatestValue = smokeLatestValue
		this.carbonMonoxide = carbonMonoxide
		this.currentCarbonMonoxideValue = currentCarbonMonoxideValue
		this.carbonMonoxideLatestValue = carbonMonoxideLatestValue
		this.battery = battery
		this.batteryLatestValue = battery
	}

	def setValue(String value, String name) {
		if (name.contains("smoke")) {
			println("the smoke value of smoke detector with id:$id is triggered to $value!")
			this.smokeLatestValue = value
			this.smoke = value
			this.currentSmokeValue = value
		} else if (name.contains("carbonMonoxide")) {
			println("the carbonMonoxide value of smoke detector with id:$id is triggered to $value!")
			this.carbonMonoxideLatestValue = value
			this.carbonMonoxide = value
			this.currentCarbonMonoxideValue = value
		} else if (name.contains("battery")) {
			println("the battery value of smoke detector with id:$id is triggered to $value!")
			this.batteryLatestValue = value.toInteger()
			this.battery = value.toInteger()
		}
		
	}

	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "smoke") {
			return currentSmokeValue
		} else if (deviceFeature == "carbonMonoxide") {
			return currentCarbonMonoxideValue
		} else if (deviceFeature == "battery") {
			return battery
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "smoke") {
			return smokeLatestValue
		} else if (deviceFeature == "carbonMonoxide") {
			return carbonMonoxideLatestValue
		} else if (deviceFeature == "battery") {
			return batteryLatestValue
		}
	}
}
