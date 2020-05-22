//Create a class for aeon key fob
package AeonKeyFob
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class AeonKeyFob {
	private String id
	private String label
	private String displayName

	AeonKeyFob(String id, String label, String displayName) {
		this.id = id
		this.label = label
		this.displayName = displayName
	}

	def setValue(LinkedHashMap eventDataMap) {
		def data = eventDataMap["data"]
		def value = eventDataMap["value"]
		println("the button with number $data is $value!")
	}

	def eventsSince() {
		def evtHeld = [[name: "button", value: "held", deviceId: "aeonKeyFobID0", descriptionText: "",
				displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtPushed = [[name: "button", value: "pushed", deviceId: "aeonKeyFobID0", descriptionText: "",
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
