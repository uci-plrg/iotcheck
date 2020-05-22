//Create a class for Touch sensor
package NfcTouch

public class NfcTouch{
	def sendEvent
	private int isTouched
	private String displayName = "NfcTouch0"

	NfcTouch(Closure sendEvent, int isTouched) {
		this.sendEvent = sendEvent
		this.isTouched = isTouched
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		println("NFC is Touched!")
		this.isTouched = 1 //Do we need this?
		sendEvent(eventDataMap)
	}
}
