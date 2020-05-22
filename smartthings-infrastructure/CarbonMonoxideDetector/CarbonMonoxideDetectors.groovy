//Create a class for carbon monoxide detector
package CarbonMonoxideDetector
import Timer.SimulatedTimer

public class CarbonMonoxideDetectors {
	private int deviceNumbers
	private List carbonMonoxideDetectors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "carbonMonoxideDetectorID0"
	private String label = "carbonMonoxideDetector0"
	private String displayName = "carbonMonoxideDetector0"
	private String carbonMonoxide = "clear"
	private String currentCarbonMonoxideValue = "clear"
	private String carbonMonoxideLatestValue = "clear"

		
	CarbonMonoxideDetectors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.carbonMonoxideDetectors = []
		
		if (init) {
			this.carbonMonoxide = "clear"
			this.currentCarbonMonoxideValue = "clear"
			this.carbonMonoxideLatestValue = "clear"
		} else if (init == 1) {
			this.carbonMonoxide = "detected"
			this.currentCarbonMonoxideValue = "detected"
			this.carbonMonoxideLatestValue = "detected"
		}
		carbonMonoxideDetectors.add(new CarbonMonoxideDetector(id, label, displayName, this.currentCarbonMonoxideValue, this.carbonMonoxideLatestValue))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != carbonMonoxideDetectors[0].currentCarbonMonoxideValue) {
			this.carbonMonoxideLatestValue = eventDataMap["value"]
			this.carbonMonoxide = eventDataMap["value"]
			this.currentCarbonMonoxideValue = eventDataMap["value"]
			carbonMonoxideDetectors[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		carbonMonoxideDetectors.count(Input)
	}
	def size() {
		carbonMonoxideDetectors.size()
	}
	def each(Closure Input) {
		carbonMonoxideDetectors.each(Input)
	}
	def find(Closure Input) {
		carbonMonoxideDetectors.find(Input)
	}
	def sort(Closure Input) {
		carbonMonoxideDetectors.sort(Input)
	}
	def collect(Closure Input) {
		carbonMonoxideDetectors.collect(Input)
	}


	def currentValue(String deviceFeature) {
		carbonMonoxideDetectors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		carbonMonoxideDetectors[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		carbonMonoxideDetectors[ix]
	}
}
