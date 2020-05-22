//Create a class for remaining devices
package RemainingDevices
import Timer.SimulatedTimer

public class RemainingDevices {
	private int deviceNumbers
	def sendEvent
		
	RemainingDevices(Closure sendEvent, int deviceNumbers) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
	}

	def setValue(LinkedHashMap eventDataMap) {
		sendEvent(eventDataMap)
	}
}
