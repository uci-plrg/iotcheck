//Create a class for water sensor
package WaterSensor
import Timer.SimulatedTimer

public class WaterSensor {
	private String id
	private String label
	private String displayName
	private String water

	WaterSensor(String id, String label, String displayName, String water) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.water = water
	}

	//By Model Checker
	def setValue(String value) {
		println("the water state is changed to $value!")
		this.water = value
	}
}
