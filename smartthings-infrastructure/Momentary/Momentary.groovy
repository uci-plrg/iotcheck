//Create a class for momentory switch device
package Momentary
import Timer.SimulatedTimer

public class Momentary {
	private String id
	private String label
	private String displayName
	def sendEvent	
	

	Momentary(Closure sendEvent, String id, String label, String displayName) {
		this.sendEvent = sendEvent
		this.id = id
		this.label = label
		this.displayName = displayName
	}

	//By Apps
	def push() {
		println("the momentary switch with id:$id is pushed!")
		sendEvent([name: "momentary", value: "pushed", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
	}

	//By Model Checker
	def setValue(String value) {
		println("the momentary switch with id:$id is pushed!")
	}
	
}
