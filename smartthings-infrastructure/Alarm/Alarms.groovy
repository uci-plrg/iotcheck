//Create a class for alarm device
package Alarm
import Timer.SimulatedTimer

public class Alarms {
	int deviceNumbers	
	List alarms
	def timers
	def sendEvent

	//If we have only one device
	private String id = "alarmID0"
	private String label = "alarm0"
	private String displayName = "alarm0"
	private String alarm = "off"
	private String currentAlarm = "off"
	private String alarmLatestValue = "off"

	Alarms(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.timers = new SimulatedTimer()
		this.deviceNumbers = deviceNumbers
		this.alarms = []

		if (init) {
			this.alarm = "off"
			this.currentAlarm = "off"
			this.alarmLatestValue = "off"
		} else {
			this.alarm = "on"
			this.currentAlarm = "on"
			this.alarmLatestValue = "on"
		}
		alarms.add(new Alarm(sendEvent, id, label, displayName, this.alarm, this.currentAlarm, this.alarmLatestValue))
	}
		
	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != alarms[0].alarm) {
			this.alarmLatestValue = eventDataMap["value"]
			this.alarm = eventDataMap["value"]
			this.currentAlarm = eventDataMap["value"]
			alarms[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	//Methods for closures
	def count(Closure Input) {
		alarms.count(Input)
	}
	def size() {
		alarms.size()
	}
	def each(Closure Input) {
		alarms.each(Input)
	}
	def find(Closure Input) {
		alarms.find(Input)
	}
	def sort(Closure Input) {
		alarms.sort(Input)
	}
	def collect(Closure Input) {
		alarms.collect(Input)
	}

	//By Apps
	def both() {
		if (alarm != "both") {
			alarmLatestValue = "both"
			alarm = "both"
			currentAlarm = "both"
			alarms[0].both()
		}
	}

	def off() {
		if (alarm != "off") {
			alarmLatestValue = "off"
			alarm = "off"
			currentAlarm = "off"
			alarms[0].off()
		}
	}

	def on() {
		both()
	}

	def siren() {
		if (alarm != "siren") {
			alarmLatestValue = "siren"
			alarm = "siren"
			currentAlarm = "siren"
			alarms[0].siren()
		}
	}

	def strobe() {
		if (alarm != "strobe") {
			alarmLatestValue = "strobe"
			alarm = "strobe"
			currentAlarm = "strobe"
			alarms[0].strobe()
		}
	}

	def currentValue(String deviceFeature) {
		alarms[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		alarms[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		alarms[ix]
	}
}
