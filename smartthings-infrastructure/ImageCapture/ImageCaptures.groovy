//Create a class for image capture sensor
package ImageCapture
import Timer.SimulatedTimer

public class ImageCaptures {
	private int deviceNumbers
	private List imageCaptureSensors
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "imageCaptureID0"
	private String label = "imageCapture0"
	private String displayName = "imageCapture0"
	private String image = "imageData"
	private String alarmState = "armed"

		
	ImageCaptures(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.imageCaptureSensors = []

		if (init) {
			this.alarmState = "armed"
		} else {
			this.alarmState = "not armed"
		}
		imageCaptureSensors.add(new ImageCapture(id, label, displayName, this.image, this.alarmState))
	}


	//Methods for closures
	def count(Closure Input) {
		imageCaptureSensors.count(Input)
	}
	def size() {
		imageCaptureSensors.size()
	}
	def each(Closure Input) {
		imageCaptureSensors.each(Input)
	}
	def find(Closure Input) {
		imageCaptureSensors.find(Input)
	}
	def sort(Closure Input) {
		imageCaptureSensors.sort(Input)
	}
	def collect(Closure Input) {
		imageCaptureSensors.collect(Input)
	}

	def alarmOn() {
		if (alarmState != "armed") {
			this.alarmState = "armed"
			imageCaptureSensors[0].alarmOn()
		}
	}

	def alarmOff() {
		if (alarmState != "not armed") {
			this.alarmState = "not armed"
			imageCaptureSensors[0].alarmOff()
		}
	}

	def take() {
		imageCaptureSensors[0].take()
	}

	def take(LinkedHashMap metaData) {
		imageCaptureSensors[0].take(metaData)
	}

	def getAt(int ix) {
		imageCaptureSensors[ix]
	}
}
