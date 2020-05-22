//Create a class for alarm device
package Alarm
import Timer.SimulatedTimer

public class Alarm {
	private String id
	private String label
	private String displayName
	private String alarm
	private String currentAlarm
	private String alarmLatestValue
	def sendEvent	
	def timers
	

	Alarm(Closure sendEvent, String id, String label, String displayName, String alarm, String currentAlarm, String alarmLatestValue) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.id = id
		this.label = label
		this.displayName = displayName
		this.alarm = alarm
		this.currentAlarm = currentAlarm
		this.alarmLatestValue = alarmLatestValue
	}

	//By model checker
	def setValue(String value) {
		println("the alarm with id:$id is triggered to $value!")
		this.alarmLatestValue = value
		this.alarm = value
		this.currentAlarm = value
	}


	//By Apps
	def both() {
		if (alarm != "both") {
			println("the alarm with id:$id is changed to both!")
			this.alarmLatestValue = "both"
			this.alarm = "both"
			this.currentAlarm = "both"
			sendEvent([name: "alarm", value: "both", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def on() {
		both()
	}

	def off() {
		if (alarm != "off") {
			println("the alarm with id:$id is changed to off!")
			this.alarmLatestValue = "off"
			this.alarm = "off"
			this.currentAlarm = "off"
			sendEvent([name: "alarm", value: "off", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def siren() {
		if (alarm != "siren") {
			println("the alarm with id:$id is changed to siren!")
			this.alarmLatestValue = "siren"
			this.alarm = "siren"
			this.currentAlarm = "siren"
			sendEvent([name: "alarm", value: "siren", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def strobe() {
		if (alarm != "strobe") {
			println("the alarm with id:$id is changed to strobe!")
			this.alarmLatestValue = "strobe"
			this.alarm = "strobe"
			this.currentAlarm = "strobe"
			sendEvent([name: "alarm", value: "strobe", deviceId: this.id, descriptionText: "",
			    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "alarm") {
			return currentAlarm
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "alarm") {
			return alarmLatestValue
		}
	}
}
