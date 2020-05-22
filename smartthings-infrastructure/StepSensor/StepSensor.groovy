//Create a class for step sensor
package StepSensor
import Timer.SimulatedTimer

public class StepSensor {
	private String id
	private String label
	private String displayName
	private int goal
	private int steps

	StepSensor(String id, String label, String displayName, int steps, int goal) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.steps = steps
		this.goal = goal
	}

	//By Model Checker
	def setValue(String value, String name) {
		if (name == "steps") {
			println("the number of steps is changed to $value!")
			this.steps = value.toInteger()
		} else if (name == "goal") {
			println("the goal is changed to $value!")
			this.goal = value.toInteger()
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "steps")
			return steps
		else if (deviceFeature == "goal")
			return goal
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "steps")
			return steps
		else if (deviceFeature == "goal")
			return goal
	}
}
