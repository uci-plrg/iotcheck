//Create a class for presence sensor
package MotionSensor
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class MotionSensor {
	private String id
	private String label
	private String displayName
	private String motion
	private String currentMotion
	private String motionLatestValue

	MotionSensor(String id, String label, String displayName, String motion, String motionLatestValue) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.motion = motion
		this.currentMotion = motion
		this.motionLatestValue = motionLatestValue
	}

	def setValue(String value) {
		println("the motion sensor with id:$id is triggered to $value!")
		this.motionLatestValue = value
		this.motion = value
		this.currentMotion = value
	}

	def statesSince() {
		eventsSince()
	}

	def eventsSince() {
		def evtActive = [[name: "motion", value: "active", deviceId: "motionSensorID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtInactive = [[name: "motion", value: "inactive", deviceId: "motionSensorID0", descriptionText: "",
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
		currentValue(deviceFeature)
	}

	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "motion") {
			return motion
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "motion") {
			return motionLatestValue
		}
	}
}
