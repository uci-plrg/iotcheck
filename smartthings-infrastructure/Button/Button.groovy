//Create a class for button
package Button
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class Button {
	private String id
	private String label
	private String displayName
	private String button
	private int numberOfButtons

	Button(String id, String label, String displayName, String button, int numberOfButtons) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.button = button
		this.numberOfButtons = numberOfButtons
	}

	def setValue(LinkedHashMap eventDataMap) {
		button = eventDataMap["value"]
		println("the button is $button!")
	}

	def eventsSince() {
		def evtHeld = [[name: "button", value: "held", deviceId: "buttonID0", descriptionText: "",
				displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtPushed = [[name: "button", value: "pushed", deviceId: "buttonID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def init = Verify.getInt(0,4)
		def evtToSend = []
		if (init == 0) {//return empty set
			return evtToSend
		} else if (init == 1) {//send one held event
			evtHeld.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 2) {//send two held events
			evtHeld.each{
				evtToSend.add(it)
			}
			evtHeld.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 3) {//send one pushed event
			evtPushed.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 4) {//send two pushed events
			evtPushed.each{
				evtToSend.add(it)
			}
			evtPushed.each{
				evtToSend.add(it)
			}
			return evtToSend
		}
	}
}
