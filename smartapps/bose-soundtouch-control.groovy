/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Bose® SoundTouch® Control
 *
 *  Author: SmartThings & Joe Geiger
 *
 *  Date: 2015-30-09
 */
definition(
    name: "Bose® SoundTouch® Control",
    namespace: "smartthings",
    author: "SmartThings & Joe Geiger",
    description: "Control your Bose® SoundTouch® when certain actions take place in your home.",
    category: "SmartThings Labs",
    iconUrl: "https://d3azp77rte0gip.cloudfront.net/smartapps/fcf1d93a-ba0b-4324-b96f-e5b5487dfaf5/images/BoseST_icon.png",
    iconX2Url: "https://d3azp77rte0gip.cloudfront.net/smartapps/fcf1d93a-ba0b-4324-b96f-e5b5487dfaf5/images/BoseST_icon@2x.png",
    iconX3Url: "https://d3azp77rte0gip.cloudfront.net/smartapps/fcf1d93a-ba0b-4324-b96f-e5b5487dfaf5/images/BoseST_icon@2x-1.png"
)

preferences {
	page(name: "mainPage", title: "Control your Bose® SoundTouch® when something happens", install: true, uninstall: true)
	page(name: "timeIntervalInput", title: "Only during a certain time") {
		section {
			input "starting", "time", title: "Starting", required: false
			input "ending", "time", title: "Ending", required: false
		}
	}
}

// input "motion", "capability.motionSensor", title: "Motion Here", required: false, multiple: true
// input "contact", "capability.contactSensor", title: "Contact Opens", required: false, multiple: true
// input "contactClosed", "capability.contactSensor", title: "Contact Closes", required: false, multiple: true
// input "acceleration", "capability.accelerationSensor", title: "Acceleration Detected", required: false, multiple: true
// input "mySwitch", "capability.switch", title: "Switch Turned On", required: false, multiple: true
// input "mySwitchOff", "capability.switch", title: "Switch Turned Off", required: false, multiple: true
// input "arrivalPresence", "capability.presenceSensor", title: "Arrival Of", required: false, multiple: true
// input "departurePresence", "capability.presenceSensor", title: "Departure Of", required: false, multiple: true
// input "smoke", "capability.smokeDetector", title: "Smoke Detected", required: false, multiple: true
// input "water", "capability.waterSensor", title: "Water Sensor Wet", required: false, multiple: true
// input "button1", "capability.button", title: "Button Press", required:false, multiple:true //remove from production
// input "triggerModes", "mode", title: "System Changes Mode", required: false, multiple: true
// input "timeOfDay", "time", title: "At a Scheduled Time", required: false

def mainPage() {
	dynamicPage(name: "mainPage") {
		def anythingSet = anythingSet()
		if (anythingSet) {
			section("When..."){
				ifSet "motion", "capability.motionSensor", title: "Motion Here", required: false, multiple: true
				ifSet "contact", "capability.contactSensor", title: "Contact Opens", required: false, multiple: true
				ifSet "contactClosed", "capability.contactSensor", title: "Contact Closes", required: false, multiple: true
				ifSet "acceleration", "capability.accelerationSensor", title: "Acceleration Detected", required: false, multiple: true
				ifSet "mySwitch", "capability.switch", title: "Switch Turned On", required: false, multiple: true
				ifSet "mySwitchOff", "capability.switch", title: "Switch Turned Off", required: false, multiple: true
				ifSet "arrivalPresence", "capability.presenceSensor", title: "Arrival Of", required: false, multiple: true
				ifSet "departurePresence", "capability.presenceSensor", title: "Departure Of", required: false, multiple: true
				ifSet "smoke", "capability.smokeDetector", title: "Smoke Detected", required: false, multiple: true
				ifSet "water", "capability.waterSensor", title: "Water Sensor Wet", required: false, multiple: true
				ifSet "button1", "capability.button", title: "Button Press", required:false, multiple:true //remove from production
				ifSet "triggerModes", "mode", title: "System Changes Mode", required: false, multiple: true
				ifSet "timeOfDay", "time", title: "At a Scheduled Time", required: false
			}
		}
		section(anythingSet ? "Select additional triggers" : "When...", hideable: anythingSet, hidden: true){
			ifUnset "motion", "capability.motionSensor", title: "Motion Here", required: false, multiple: true
			ifUnset "contact", "capability.contactSensor", title: "Contact Opens", required: false, multiple: true
			ifUnset "contactClosed", "capability.contactSensor", title: "Contact Closes", required: false, multiple: true
			ifUnset "acceleration", "capability.accelerationSensor", title: "Acceleration Detected", required: false, multiple: true
			ifUnset "mySwitch", "capability.switch", title: "Switch Turned On", required: false, multiple: true
			ifUnset "mySwitchOff", "capability.switch", title: "Switch Turned Off", required: false, multiple: true
			ifUnset "arrivalPresence", "capability.presenceSensor", title: "Arrival Of", required: false, multiple: true
			ifUnset "departurePresence", "capability.presenceSensor", title: "Departure Of", required: false, multiple: true
			ifUnset "smoke", "capability.smokeDetector", title: "Smoke Detected", required: false, multiple: true
			ifUnset "water", "capability.waterSensor", title: "Water Sensor Wet", required: false, multiple: true
			ifUnset "button1", "capability.button", title: "Button Press", required:false, multiple:true //remove from production
			ifUnset "triggerModes", "mode", title: "System Changes Mode", required: false, multiple: true
			ifUnset "timeOfDay", "time", title: "At a Scheduled Time", required: false
		}
		section("Perform this action"){
			input "actionType", "enum", title: "Action?", required: true, defaultValue: "play", options: [
				"Turn On & Play",
				"Turn Off",
				"Toggle Play/Pause",
				"Skip to Next Track",
				"Skip to Beginning/Previous Track"
			]
		}
		section {
			input "bose", "capability.musicPlayer", title: "Bose® SoundTouch® music player", required: true
		}
		section("More options", hideable: true, hidden: true) {
			input "volume", "number", title: "Set the volume volume", description: "0-100%", required: false
			input "frequency", "decimal", title: "Minimum time between actions (defaults to every event)", description: "Minutes", required: false
			href "timeIntervalInput", title: "Only during a certain time"
			input "days", "enum", title: "Only on certain days of the week", multiple: true, required: false,
				options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
			//if (settings.modes) {
            	input "modes", "mode", title: "Only when mode is", multiple: true, required: false
         //   }
			input "oncePerDay", "bool", title: "Only once per day", required: false, defaultValue: false
		}
		section([mobileOnly:true]) {
			label title: "Assign a name", required: false
			mode title: "Set for specific mode(s)"
		}
	}
}

private anythingSet() {
	for (name in ["motion","contact","contactClosed","acceleration","mySwitch","mySwitchOff","arrivalPresence","departurePresence","smoke","water","button1","triggerModes","timeOfDay"]) {
		if (settings[name]) {
			return true
		}
	}
	return false
}

private ifUnset(Map options, String name, String capability) {
	if (!settings[name]) {
		input(options, name, capability)
	}
}

private ifSet(Map options, String name, String capability) {
	if (settings[name]) {
		input(options, name, capability)
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribeToEvents()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	unschedule()
	subscribeToEvents()
}

def subscribeToEvents() {
	log.trace "subscribeToEvents()"
	subscribe(app, appTouchHandler)
	subscribe(contact, "contact.open", eventHandler1)
	subscribe(contactClosed, "contact.closed", eventHandler1)
	subscribe(acceleration, "acceleration.active", eventHandler1)
	subscribe(motion, "motion.active", eventHandler1)
	subscribe(mySwitch, "switch.on", eventHandler1)
	subscribe(mySwitchOff, "switch.off", eventHandler1)
	subscribe(arrivalPresence, "presence.present", eventHandler1)
	subscribe(departurePresence, "presence.not present", eventHandler1)
	subscribe(smoke, "smoke.detected", eventHandler1)
	subscribe(smoke, "smoke.tested", eventHandler1)
	subscribe(smoke, "carbonMonoxide.detected", eventHandler1)
	subscribe(water, "water.wet", eventHandler1)
	subscribe(button1, "button.pushed", eventHandler1)

	if (triggerModes) {
		subscribe(location, modeChangeHandler)
	}

	if (timeOfDay) {
		schedule(timeOfDay, scheduledTimeHandler)
	}
}

def eventHandler1(evt) {
	if (allOk) {
		def lastTime = state[frequencyKey(evt)]
		if (oncePerDayOk(lastTime)) {
			if (frequency) {
				if (lastTime == null || now() - lastTime >= frequency * 60000) {
					takeAction(evt)
				}
				else {
					log.debug "Not taking action because $frequency minutes have not elapsed since last action"
				}
			}
			else {
				takeAction(evt)
			}
		}
		else {
			log.debug "Not taking action because it was already taken today"
		}
	}
}

def modeChangeHandler(evt) {
	log.trace "modeChangeHandler $evt.name: $evt.value ($triggerModes)"
	if (evt.value in triggerModes) {
		eventHandler1(evt)
	}
}

def scheduledTimeHandler() {
	//eventHandler(null)
}

def appTouchHandler(evt) {
	takeAction(evt)
}

private takeAction(evt) {
	log.debug "takeAction($actionType)"
	def options = [:]
	if (volume) {
		bose.setLevel(volume as Integer)
		options.delay = 1000
	}

	switch (actionType) {
		case "Turn On & Play":
			options ? bose.on(options) : bose.on()
			break
		case "Turn Off":
			options ? bose.off(options) : bose.off()
			break
		case "Toggle Play/Pause":
			def currentStatus = bose.currentValue("playpause")
			if (currentStatus == "play") {
				options ? bose.pause(options) : bose.pause()
			}
			else if (currentStatus == "pause") {
				options ? bose.play(options) : bose.play()
			}
			break
		case "Skip to Next Track":
			options ? bose.nextTrack(options) : bose.nextTrack()
			break
		case "Skip to Beginning/Previous Track":
			options ? bose.previousTrack(options) : bose.previousTrack()
			break
		default:
			log.error "Action type '$actionType' not defined"
	}

	if (frequency) {
		state.lastActionTimeStamp = now()
	}
}

private frequencyKey(evt) {
	//evt.deviceId ?: evt.value
	"lastActionTimeStamp"
}

private dayString(Date date) {
	def df = new java.text.SimpleDateFormat("yyyy-MM-dd")
	if (location.timeZone) {
		df.setTimeZone(location.timeZone)
	}
	else {
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
	}
	df.format(date)
}

private oncePerDayOk(Long lastTime) {
	def result = true
	if (oncePerDay) {
		result = lastTime ? dayString(new Date()) != dayString(new Date(lastTime)) : true
		log.trace "oncePerDayOk = $result"
	}
	result
}

// TODO - centralize somehow
private getAllOk() {
	modeOk && daysOk && timeOk
}

private getModeOk() {
	def result = !modes || modes.contains(location.mode)
	log.trace "modeOk = $result"
	result
}

private getDaysOk() {
	def result = true
	if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	log.trace "daysOk = $result"
	result
}

private getTimeOk() {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting, location?.timeZone).time
		def stop = timeToday(ending, location?.timeZone).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	log.trace "timeOk = $result"
	result
}

private hhmm(time, fmt = "h:mm a")
{
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}

private timeIntervalLabel()
{
	(starting && ending) ? hhmm(starting) + "-" + hhmm(ending, "h:mm a z") : ""
}
// TODO - End Centralize
