//Create a class for carbon monoxide detector
package CarbonMonoxideDetector
import Timer.SimulatedTimer

public class CarbonMonoxideDetector {
	private String id
	private String label
	private String displayName
	private String carbonMonoxide
	private String currentCarbonMonoxideValue
	private String carbonMonoxideLatestValue

	CarbonMonoxideDetector(String id, String label, String displayName, String carbonMonoxide, String carbonMonoxideLatestValue) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.carbonMonoxide = carbonMonoxide
		this.currentCarbonMonoxideValue = carbonMonoxide
		this.carbonMonoxideLatestValue = carbonMonoxideLatestValue
	}

	def setValue(String value) {
		println("the carbon monoxide detector with id:$id is triggered to $value!")
		this.carbonMonoxideLatestValue = value
		this.carbonMonoxide = value
		this.currentCarbonMonoxideValue = value
	}

	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "carbonMonoxide") {
			return currentCarbonMonoxideValue
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "carbonMonoxide") {
			return carbonMonoxideLatestValue
		}
	}
}
