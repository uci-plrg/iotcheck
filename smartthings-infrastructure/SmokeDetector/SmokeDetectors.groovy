//Create a class for smoke detector
package SmokeDetector
import Timer.SimulatedTimer

public class SmokeDetectors {
	private int deviceNumbers
	private List smokeDetectors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "smokeDetectorID0"
	private String label = "smokeDetector0"
	private String displayName = "smokeDetector0"
	private String smoke = "clear"
	private String currentSmokeValue = "clear"
	private String smokeLatestValue = "clear"
	private String carbonMonoxide = "clear"
	private String currentCarbonMonoxideValue = "clear"
	private String carbonMonoxideLatestValue = "clear"
	private int battery = 50
    private int batteryValue = 50
	private int batteryLatestValue = 50

		
	SmokeDetectors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.smokeDetectors = []

		if (init) {
			this.currentSmokeValue = "clear"
			this.smokeLatestValue = "clear"
			this.carbonMonoxide = "clear"
			this.currentCarbonMonoxideValue = "clear"
			this.carbonMonoxideLatestValue = "clear"
			this.battery = 50
			this.batteryValue = 50
			this.batteryLatestValue = 50
		} else {
			this.currentSmokeValue = "detected"
			this.smokeLatestValue = "detected"
			this.carbonMonoxide = "detected"
			this.currentCarbonMonoxideValue = "detected"
			this.carbonMonoxideLatestValue = "detected"
			this.battery = 60
			this.batteryValue = 60
			this.batteryLatestValue = 60
		}
		smokeDetectors.add(new SmokeDetector(id, label, displayName, this.currentSmokeValue, this.smokeLatestValue, this.carbonMonoxide, this.carbonMonoxideLatestValue, this.battery))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["name"].contains("smoke")) {
			if (eventDataMap["value"] != smokeDetectors[0].currentSmokeValue) {
				this.smokeLatestValue = eventDataMap["value"]
				this.smoke = eventDataMap["value"]
				this.currentSmokeValue = eventDataMap["value"]
				smokeDetectors[0].setValue(eventDataMap["value"], eventDataMap["name"])
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"].contains("carbonMonoxide")) {
			if (eventDataMap["value"] != smokeDetectors[0].currentCarbonMonoxideValue) {
				this.carbonMonoxideLatestValue = eventDataMap["value"]
				this.carbonMonoxide = eventDataMap["value"]
				this.currentCarbonMonoxideValue = eventDataMap["value"]
				smokeDetectors[0].setValue(eventDataMap["value"], eventDataMap["name"])
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"].contains("battery")) {
			if (eventDataMap["value"].toInteger() != smokeDetectors[0].battery) {
				this.battery = eventDataMap["value"].toInteger()
				this.batteryLatestValue = eventDataMap["value"].toInteger()
				smokeDetectors[0].setValue(eventDataMap["value"], eventDataMap["name"])
				sendEvent(eventDataMap)
			}
		}
	}

	//Methods for closures
	def count(Closure Input) {
		smokeDetectors.count(Input)
	}
	def size() {
		smokeDetectors.size()
	}
	def each(Closure Input) {
		smokeDetectors.each(Input)
	}
	def find(Closure Input) {
		smokeDetectors.find(Input)
	}
	def sort(Closure Input) {
		smokeDetectors.sort(Input)
	}
	def collect(Closure Input) {
		smokeDetectors.collect(Input)
	}


	def currentValue(String deviceFeature) {
		smokeDetectors[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		smokeDetectors[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		smokeDetectors[ix]
	}
}
