//Create a class for Touch sensor
package appTouch

public class Touched{
	def sendEvent
	private String id
	private int isTouched
	private String label
	private String name


	Touched(Closure sendEvent, int isTouched) {
		this.sendEvent = sendEvent
		this.isTouched = isTouched
		this.label = "app0"
		this.id = "appID0"
		this.name = "app0"
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		println("The application is Touched!")
		this.isTouched = 1 //Do we need this?
		sendEvent(eventDataMap)
	}
}
