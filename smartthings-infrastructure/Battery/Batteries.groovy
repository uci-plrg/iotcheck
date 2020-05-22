//Create a class for battery
package Battery
import Timer.SimulatedTimer

public class Batteries {
	private int deviceNumbers
	private List batteries
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "batteryID0"
	private String label = "battery0"
	private String displayName = "battery0"
	private int battery = 50
	private int currentBattery = 50
	private int batteryLatestValue = 50

		
	Batteries(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.batteries = []

		if (init) {
			this.battery = 50
			this.currentBattery = 50
			this.batteryLatestValue = 50
		} else {
			this.battery = 35
			this.currentBattery = 35
			this.batteryLatestValue = 35
		}

		batteries.add(new Battery(id, label, displayName, this.battery))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"].toInteger() != batteries[0].battery) {
			this.battery = eventDataMap["value"].toInteger()
			this.currentBattery = eventDataMap["value"].toInteger()
			this.batteryLatestValue = eventDataMap["value"].toInteger()
			batteries[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		batteries.count(Input)
	}
	def size() {
		batteries.size()
	}
	def each(Closure Input) {
		batteries.each(Input)
	}
	def sort(Closure Input) {
		batteries.sort(Input)
	}
	def find(Closure Input) {
		batteries.find(Input)
	}
	def collect(Closure Input) {
		batteries.collect(Input)
	}


	def currentValue(String deviceFeature) {
		batteries[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		batteries[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		batteries[ix]
	}
}
