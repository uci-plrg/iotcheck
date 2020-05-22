//Create a class for aeon key fob
package AeonKeyFob
import Timer.SimulatedTimer

public class AeonKeyFobs {
	private int deviceNumbers
	private List aeonKeyFobs
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "aeonKeyFobID0"
	private String label = "aeonKeyFob0"
	private String displayName = "aeonKeyFob0"
	
		
	AeonKeyFobs(Closure sendEvent, int deviceNumbers) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.aeonKeyFobs = []

		aeonKeyFobs.add(new AeonKeyFob(id, label, displayName))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		aeonKeyFobs[0].setValue(eventDataMap)
		sendEvent(eventDataMap)
	}

	//Methods for closures
	def count(Closure Input) {
		aeonKeyFobs.count(Input)
	}
	def size() {
		aeonKeyFobs.size()
	}
	def each(Closure Input) {
		aeonKeyFobs.each(Input)
	}
	def sort(Closure Input) {
		aeonKeyFobs.sort(Input)
	}
	def find(Closure Input) {
		aeonKeyFobs.find(Input)
	}
	def collect(Closure Input) {
		aeonKeyFobs.collect(Input)
	}


	//methods
	def eventsSince(Date dateObj) {
		return aeonKeyFobs[0].eventsSince()
	}


	def getAt(int ix) {
		aeonKeyFobs[ix]
	}
}
