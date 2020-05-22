//Create a class for image capture sensor
package ImageCapture
import Timer.SimulatedTimer

public class ImageCapture {
	private String id
	private String label
	private String displayName
	private String image
	private String alarmState
	def timers

	ImageCapture(String id, String label, String displayName, String image, String alarmState) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.image = image
		this.alarmState = alarmState
		this.timers = new SimulatedTimer()
	}
	
	def alarmOn() {
		if (alarmState != "armed") {
			println("The camera with id:$id is armed!")
			this.alarmState = "armed"
		}
	}

	def alarmOff() {
		if (alarmState != "not armed") {
			println("The camera with id:$id is not armed!")
			this.alarmState = "not armed"
		}
	}

	def take() {
		println("The camera with id:$id is taken a picture!")
	}

	def take(LinkedHashMap metaData) {
		def task = timers.runAfter(metaData["delay"]) {
			println("The camera with id:$id is taken a picture!")
		}
	}
}
