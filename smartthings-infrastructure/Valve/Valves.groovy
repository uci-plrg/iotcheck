//Create a class for valve
package Valve
import Timer.SimulatedTimer

public class Valves {
	int deviceNumbers	
	List valves
	def timers
	def sendEvent

	//If we have only one device
	private String id = "ValveID0"
	private String label = "Valve0"
	private String displayName = "Valve0"
	private String valve = "closed"
	private String valveLatestValue = "closed"

	Valves(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.valves = []
		
		if (init) {
			this.valve = "closed"
			this.valveLatestValue = "closed"
		} else {
			this.valve = "open"
			this.valveLatestValue = "open"
		}
		valves.add(new Valve(sendEvent, id, label, displayName, this.valve, this.valveLatestValue))
	}

	//Methods for closures
	def count(Closure Input) {
		valves.count(Input)
	}
	def size() {
		valves.size()
	}
	def each(Closure Input) {
		valves.each(Input)
	}
	def find(Closure Input) {
		valves.find(Input)
	}
	def sort(Closure Input) {
		valves.sort(Input)
	}
	def collect(Closure Input) {
		valves.collect(Input)
	}

	//By Apps
	def open() {
		if (valve != "open") {
			this.valve = "open"
			this.valveLatestValue = "open"
			valves[0].open()
		}
	}

	def open(LinkedHashMap metaData) {
		if (valve != "open") {
			def task = timers.runAfter(metaData["delay"]) {
				this.valve = "open"
				this.valveLatestValue = "open"
				valves[0].open()
			}
		}
	}

	def close() {
		if (valve != "closed") {
			this.valve = "closed"
			this.valveLatestValue = "closed"			
			valves[0].close()
		}
	}

	def close(LinkedHashMap metaData) {
		if (valve != "closed") {
			def task = timers.runAfter(metaData["delay"]) {
				this.valve = "closed"
				this.valveLatestValue = "closed"
				valves[0].close()
			}
		}
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != valves[0].valve) {
			this.valve = eventDataMap["value"]
			valves[0].setValue(eventDataMap["value"])
		}
	}


	def currentValue(String deviceFeature) {
		valves[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		valves[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		valves[ix]
	}
}
