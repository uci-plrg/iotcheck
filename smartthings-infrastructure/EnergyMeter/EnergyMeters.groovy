//Create a class for energy meter
package EnergyMeter
import Timer.SimulatedTimer

public class EnergyMeters {
	private int deviceNumbers
	private List energyMeters
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "energyMeterID0"
	private String label = "energyMeter0"
	private String displayName = "energyMeter0"
	private int energy = 50
	private int currentEnergy = 50
	private String status = "off"

		
	EnergyMeters(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.energyMeters = []

		if (init) {
			this.energy = 50
			this.currentEnergy = 50
		} else {
			this.energy = 60
			this.currentEnergy = 60
		}
		energyMeters.add(new EnergyMeter(id, label, displayName, this.energy, this.status))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"].toInteger() != energyMeters[0].energy) {
			this.energy = eventDataMap["value"].toInteger()
			this.currentEnergy = eventDataMap["value"].toInteger()
			energyMeters[0].setValue(eventDataMap["value"])	
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		energyMeters.count(Input)
	}
	def size() {
		energyMeters.size()
	}
	def each(Closure Input) {
		energyMeters.each(Input)
	}
	def find(Closure Input) {
		energyMeters.find(Input)
	}
	def sort(Closure Input) {
		energyMeters.sort(Input)
	}
	def collect(Closure Input) {
		energyMeters.collect(Input)
	}

	def reset() {
		if (status != "on") {
			status = "on"
			energyMeters[0].reset()
		}
	}
	
	def off() {
		if (status != "off") {
			status = "off"
			energyMeters[0].off()
		}
	}

	def currentValue(String deviceFeature) {
		energyMeters[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		energyMeters[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		energyMeters[ix]
	}
}
