//Create a class for contact sensor
package ContactSensor
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class ContactSensor {
	private String id
	private String label
	private String displayName
	private String contactState
	private String currentContact	
	private String latestValue
	private String alarmState
	private List events = []
	private List timeOfEvents = []
	

	ContactSensor(String id, String label, String displayName, String contactState, String currentContact, String alarmState, String latestValue) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.contactState = contactState
		this.currentContact = currentContact
		this.latestValue = latestValue
		this.alarmState = alarmState
	}

	def eventsSince() {
		def evtOpen = [[name: "contact", value: "open", deviceId: "contactSensorID0", descriptionText: "",
				displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtClosed = [[name: "contact", value: "closed", deviceId: "contactSensorID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def init = Verify.getInt(0,4)
		def evtToSend = []
		if (init == 0) {//return empty set
			return evtToSend
		} else if (init == 1) {//send one open event
			evtOpen.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 2) {//send two open events
			evtOpen.each{
				evtToSend.add(it)
			}
			evtOpen.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 3) {//send one closed event
			evtClosed.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 4) {//send two closed events
			evtClosed.each{
				evtToSend.add(it)
			}
			evtClosed.each{
				evtToSend.add(it)
			}
			return evtToSend
		}
	}

	def setValue(String value) {
		println("the contact sensor with id:$id is triggered to $value!")
		this.contactState = value
		this.currentContact = value
		this.latestValue = value
	}

	
	def on() {
		println("the contact sensor with id:$id is armed!")
		this.alarmState = "armed"
	}

	def off() {
		println("the contact sensor with id:$id is not armed!")
		this.alarmState = "not armed"
	}
	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "contact") {
			return contactState
		}
	}

	def currentState(String deviceFeature) {
		if (deviceFeature == "contact") {
			return contactState
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "contact") {
			return latestValue
		}
	}
}
