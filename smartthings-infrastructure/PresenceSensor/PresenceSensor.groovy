//Create a class for presence sensor
package PresenceSensor
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class PresenceSensor {
	private String id
	private String label
	private String displayName
	private String presence
	private String currentPresence
	private String presenceLatestValue

	PresenceSensor(String id, String label, String displayName, String presence, String presenceLatestValue) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.presence = presence
		this.currentPresence = presence
		this.presenceLatestValue = presenceLatestValue
	}

	def setValue(String value) {
		println("the presence sensor with id:$id is triggered to $value!")
		this.presenceLatestValue = value
		this.presence = value
		this.currentPresence = value
	}
	
	def statesSince() {
		eventsSince()
	}
	
	def statesSince(String info, Date dateObj) {
		statesSince()
	}
	
	def eventsSince(Date dateObj) {
		eventsSince()
	}

	def eventsSince() {
		def evtActive = [[name: "presence", value: "present", deviceId: "motionSensorID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtInactive = [[name: "presence", value: "not present", deviceId: "motionSensorID0", descriptionText: "",
				    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def init = Verify.getInt(0,4)
		def evtToSend = []
		if (init == 0) {//return empty set
			return evtToSend
		} else if (init == 1) {//send one active event
			evtActive.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 2) {//send two active events
			evtActive.each{
				evtToSend.add(it)
			}
			evtActive.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 3) {//send one inactive event
			evtInactive.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 4) {//send two inactive events
			evtInactive.each{
				evtToSend.add(it)
			}
			evtInactive.each{
				evtToSend.add(it)
			}
			return evtToSend
		}
	}

	def currentState(String deviceFeature) {
		return [rawDateCreated: [time: System.currentTimeMillis()]]
	}
	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "presence") {
			return presence
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "presence") {
			return presenceLatestValue
		}
	}
}
