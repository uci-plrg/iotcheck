//Create a class for sleep sensor
package SleepSensor
import Timer.SimulatedTimer

public class SleepSensor {
	private String id
	private String label
	private String displayName
	private String sleeping

	SleepSensor(String id, String label, String displayName, String sleeping) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.sleeping = sleeping
	}

	//By Model Checker
	def setValue(String value) {
		println("the sleeping state is changed to $value!")
		this.sleeping = value
	}
}
