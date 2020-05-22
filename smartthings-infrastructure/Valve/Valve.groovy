//Create a class for valve
package Valve
import Timer.SimulatedTimer

public class Valve {
	private String id
	private String label
	private String displayName
	private String valve
	private String valveLatestValue
	def sendEvent	
	def timers
	

	Valve(Closure sendEvent, String id, String label, String displayName, String valve, String valveLatestValue) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.id = id
		this.label = label
		this.displayName = displayName
		this.valve = valve
		this.valveLatestValue = valveLatestValue
	}

	//By Apps
	def open() {
		if (valve != "open") {
			println("the valve with id:$id is open!")
			this.valveLatestValue = "open"
			this.valve = "open"
			sendEvent([name: "contact", value: "open", deviceId: this.id, descriptionText: "",
			           displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def open(LinkedHashMap metaData) {
		if (valve != "open") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the valve with id:$id is open!")
				this.valveLatestValue = "open"
				this.valve = "open"
				sendEvent([name: "contact", value: "open", deviceId: this.id, descriptionText: "",
				           displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}

	def close() {
		if (valve != "closed") {
			println("the valve with id:$id is closed!")
			this.valveLatestValue = "closed"
			this.valve = "closed"
			sendEvent([name: "contact", value: "closed", deviceId: this.id, descriptionText: "",
			           displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def close(LinkedHashMap metaData) {
		if (valve != "closed") {
			def task = timers.runAfter(metaData["delay"]) {
				println("the valve with id:$id is closed!")
				this.valveLatestValue = "closed"
				this.valve = "closed"
				sendEvent([name: "contact", value: "closed", deviceId: this.id, descriptionText: "",
				            displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
			}
		}
	}

	//By Model Checker
	def setValue(String value) {
		println("the valve with id:$id is $value!")
		this.valveLatestValue = value
		this.valve = value
	}
	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "valve") {
			return valve
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "valve") {
			return valveLatestValue
		}
	}
}
