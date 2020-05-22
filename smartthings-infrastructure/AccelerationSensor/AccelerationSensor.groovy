//Create a class for acceleration sensor
package AccelerationSensor
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class AccelerationSensor {
	private String id
	private String label
	private String displayName
	private String acceleration
	private String currentAcceleration
	private String accelerationLatestValue

	AccelerationSensor(String id, String label, String displayName, String acceleration, String accelerationLatestValue) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.acceleration = acceleration
		this.currentAcceleration = acceleration
		this.accelerationLatestValue = accelerationLatestValue
	}

	def setValue(String value) {
		println("the acceleration sensor with id:$id is triggered to $value!")
		this.accelerationLatestValue = value
		this.acceleration = value
		this.currentAcceleration = value
	}

	def statesSince() {
		def evtActive = [[name: "acceleration", value: "active", deviceId: "accelerationSensorID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtInactive = [[name: "acceleration", value: "inactive", deviceId: "accelerationSensorID0", descriptionText: "",
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

	def eventsSince() {
		def evtActive = [[name: "acceleration", value: "active", deviceId: "accelerationSensorID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtInactive = [[name: "acceleration", value: "inactive", deviceId: "accelerationSensorID0", descriptionText: "",
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

	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "acceleration") {
			return acceleration
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "acceleration") {
			return accelerationLatestValue
		}
	}
}
