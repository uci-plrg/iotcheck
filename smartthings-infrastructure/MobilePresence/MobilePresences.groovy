//Create a class for mobile presence
package MobilePresence
import Timer.SimulatedTimer

public class MobilePresences {
	private int deviceNumbers
	private List mobilePresences
	private String deviceNetworkId
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "mobilePresenceID0"
	private String label = "mobilePresence0"
	private String displayName = "mobilePresence0"
	
		
	MobilePresences(Closure sendEvent, int deviceNumbers) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.mobilePresences = []
		this.deviceNetworkId = "mobile0"

		mobilePresences.add(new MobilePresence(id, label, displayName, deviceNetworkId))
	}

	//Methods for closures
	def count(Closure Input) {
		mobilePresences.count(Input)
	}
	def size() {
		mobilePresences.size()
	}
	def each(Closure Input) {
		mobilePresences.each(Input)
	}
	def find(Closure Input) {
		mobilePresences.find(Input)
	}
	def sort(Closure Input) {
		mobilePresences.sort(Input)
	}
	def collect(Closure Input) {
		mobilePresences.collect(Input)
	}


	def getAt(int ix) {
		mobilePresences[ix]
	}
}
