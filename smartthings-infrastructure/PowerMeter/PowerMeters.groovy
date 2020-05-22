//Create a class for power meter
package PowerMeter
import Timer.SimulatedTimer

public class PowerMeters {
	private int deviceNumbers
	private List powerMeters
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "powerMeterID0"
	private String label = "powerMeter0"
	private String displayName = "powerMeter0"
	private int power = 50
	private int currentPower = 50

		
	PowerMeters(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.powerMeters = []

		if (init) {
			this.power = 50
			this.currentPower = 50
		} else {
			this.power = 60
			this.currentPower = 60
		}
		powerMeters.add(new PowerMeter(id, label, displayName, this.power))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"].toInteger() != powerMeters[0].power) {
			this.power = eventDataMap["value"].toInteger()
			this.currentPower = eventDataMap["value"].toInteger()
			powerMeters[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		powerMeters.count(Input)
	}
	def size() {
		powerMeters.size()
	}
	def each(Closure Input) {
		powerMeters.each(Input)
	}
	def find(Closure Input) {
		powerMeters.find(Input)
	}
	def sort(Closure Input) {
		powerMeters.sort(Input)
	}
	def collect(Closure Input) {
		powerMeters.collect(Input)
	}


	def currentValue(String deviceFeature) {
		powerMeters[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		powerMeters[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		powerMeters[ix]
	}
}
