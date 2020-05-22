//Create a class for momentory switch device
package Momentary
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class Momentaries {
	int deviceNumbers	
	List momentaries
	def sendEvent

	//If we have only one device
	private String id = "momentaryID0"
	private String label = "momentary0"
	private String displayName = "momentary0"

	Momentaries(Closure sendEvent, int deviceNumbers) {
		this.sendEvent = sendEvent
		this.deviceNumbers = deviceNumbers
		this.momentaries = []
		
		/*def init = Verify.getBoolean()
		if (init) {
			this.doorState = "closed"
			this.doorLatestValue = "closed"
		} else {
			this.doorState = "open"
			this.doorLatestValue = "open"
		}*/
		momentaries.add(new Momentary(sendEvent, id, label, displayName))
	}

	//Methods for closures
	def count(Closure Input) {
		momentaries.count(Input)
	}
	def size() {
		momentaries.size()
	}
	def each(Closure Input) {
		momentaries.each(Input)
	}
	def find(Closure Input) {
		momentaries.find(Input)
	}
	def sort(Closure Input) {
		momentaries.sort(Input)
	}
	def collect(Closure Input) {
		momentaries.collect(Input)
	}

	//By Apps
	def push() {
		momentaries[0].push()
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		momentaries[0].setValue(eventDataMap["value"])
		sendEvent(eventDataMap)
	}

	def getAt(int ix) {
		momentaries[ix]
	}
}
