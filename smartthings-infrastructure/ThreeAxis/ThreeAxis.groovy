//Create a class for three axis
package ThreeAxis
import Timer.SimulatedTimer
import groovy.json.JsonSlurper

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class ThreeAxis {
	private String id
	private String label
	private String displayName
	private LinkedHashMap threeAxis

	ThreeAxis(String id, String label, String displayName, LinkedHashMap threeAxis) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.threeAxis = threeAxis
	}

	def setValue(LinkedHashMap eventDataMap) {
		threeAxis = new groovy.json.JsonSlurper().parseText(eventDataMap["value"])
		println("the three axis of cube is chagned to $threeAxis!")
	}

	def currentState(String deviceFeature) {
		currentValue(deviceFeature)
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "threeAxis" || deviceFeature == "status")
			return threeAxis
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "threeAxis" || deviceFeature == "status")
			return threeAxis
	}
}
