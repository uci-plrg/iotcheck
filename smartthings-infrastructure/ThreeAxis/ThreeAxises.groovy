//Create a class for three axis
package ThreeAxis
import Timer.SimulatedTimer

public class ThreeAxises {
	private int deviceNumbers
	private List threeAxises
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "threeAxisID0"
	private String label = "threeAxis0"
	private String displayName = "threeAxis0"
	private LinkedHashMap threeAxis = [x:0, y:0, z:0]
	
		
	ThreeAxises(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.threeAxises = []
		
		if (init) {
			this.threeAxis = [x:0, y:0, z:0]
		} else {
			this.threeAxis = [x:1, y:2, z:3]
		}
		threeAxises.add(new ThreeAxis(id, label, displayName, threeAxis))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		threeAxises[0].setValue(eventDataMap)
		sendEvent(eventDataMap)
	}

	//Methods for closures
	def count(Closure Input) {
		threeAxises.count(Input)
	}
	def size() {
		threeAxises.size()
	}
	def each(Closure Input) {
		threeAxises.each(Input)
	}
	def sort(Closure Input) {
		threeAxises.sort(Input)
	}
	def find(Closure Input) {
		threeAxises.find(Input)
	}
	def collect(Closure Input) {
		threeAxises.collect(Input)
	}
	
	def currentState(String deviceFeature) {
		threeAxises[0].currentValue(deviceFeature)
	}

	def currentValue(String deviceFeature) {
		threeAxises[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		threeAxises[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		threeAxises[ix]
	}
}
