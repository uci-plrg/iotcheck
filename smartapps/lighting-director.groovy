/**
 *  Lighting Director
 *
 * Source: https://github.com/tslagle13/SmartThings/blob/master/Director-Series-Apps/Lighting-Director/Lighting%20Director.groovy
 *
 *  Current Version: 2.9.4
 *
 *
 *  Changelog:
 *  Version - 1.3
 *  Version - 1.30.1 Modification by Michael Struck - Fixed syntax of help text and titles of scenarios, along with a new icon
 *  Version - 1.40.0 Modification by Michael Struck - Code optimization and added door contact sensor capability		
 *  Version - 1.41.0 Modification by Michael Struck - Code optimization and added time restrictions to each scenario
 *  Version - 2.0  Tim Slagle - Moved to only have 4 slots.  Code was to heavy and needed to be trimmed.
 *  Version - 2.1  Tim Slagle - Moved time interval inputs inline with STs design.
 *  Version - 2.2  Michael Struck - Added the ability to activate switches via the status locks and fixed some syntax issues
 *  Version - 2.5  Michael Struck - Changed the way the app unschedules re-triggered events
 *  Version - 2.5.1 Tim Slagle - Fixed Time Logic
 *  Version - 2.6 Michael Struck - Added the additional restriction of running triggers once per day and misc cleanup of code
 *  Version - 2.7 Michael Struck - Added feature that turns off triggering if the physical switch is pressed.
 *  Version - 2.81 Michael Struck - Fixed an issue with dimmers not stopping light action
 *  Version - 2.9 Michael Struck - Fixed issue where button presses outside of the time restrictions prevent the triggers from firing and code optimization 
 *  Version - 2.9.1 Tim Slagle - Further enhanced time interval logic.  
 *  Version - 2.9.2 Brandon Gordon - Added support for acceleration sensors.
 *  Version - 2.9.3 Brandon Gordon - Added mode change subscriptions.
 *  Version - 2.9.4 Michael Struck - Code Optimization when triggers are tripped
 *
*  Source code can be found here: https://github.com/tslagle13/SmartThings/blob/master/smartapps/tslagle13/vacation-lighting-director.groovy
 *
 *  Copyright 2015 Tim Slagle and Michael Struck
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
 */
 
definition(
    name: "Lighting Director",
    namespace: "tslagle13",
    author: "Tim Slagle & Michael Struck",
    description: "Control up to 4 sets (scenarios) of lights based on motion, door contacts and illuminance levels.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Lighting-Director/LightingDirector.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Lighting-Director/LightingDirector@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Lighting-Director/LightingDirector@2x.png")

preferences {
    page(name: "timeIntervalInputA", title: "Only during a certain time", refreshAfterSelection:true) {
		section {
			input "A_timeStart", "time", title: "Starting", required: false, refreshAfterSelection:true
			input "A_timeEnd", "time", title: "Ending", required: false, refreshAfterSelection:true
		}
        }
    page name:"pageSetup"
    page name:"pageSetupScenarioA"
}

// Show setup page
def pageSetup() {

    def pageProperties = [
        name:       "pageSetup",
        nextPage:   null,
        install:    true,
        uninstall:  true
    ]

	return dynamicPage(pageProperties) {
        section("Setup Menu") {
            href "pageSetupScenarioA", title: getTitle(settings.ScenarioNameA), description: getDesc(settings.ScenarioNameA), state: greyOut(settings.ScenarioNameA)
            }
        section([title:"Options", mobileOnly:true]) {
            label title:"Assign a name", required:false
        }
    }
}

// Show "pageSetupScenarioA" page
def pageSetupScenarioA() {
    //input name: "A_switches", type: "capability.switch", title: "Control the following switches...", multiple: true, required: false
    def inputLightsA = [
        name:       "A_switches",
        type:       "capability.switch",
        title:      "Control the following switches...",
        multiple:   true,
        required:   false
    ]
    //input name: "A_dimmers", type: "capability.switchLevel", title: "Dim the following...", multiple: true, required: false
    def inputDimmersA = [
        name:       "A_dimmers",
        type:       "capability.switchLevel",
        title:      "Dim the following...",
        multiple:   true,
        required:   false
    ]
    //input name: "A_motion", type: "capability.motionSensor", title: "Using these motion sensors...", multiple: true, required: false
    def inputMotionA = [
        name:       "A_motion",
        type:       "capability.motionSensor",
        title:      "Using these motion sensors...",
        multiple:   true,
        required:   false
    ]
    //input name: "A_acceleration", type: "capability.accelerationSensor", title: "Or using these acceleration sensors...", multiple: true, required: false
	def inputAccelerationA = [
		name:       "A_acceleration",
		type:       "capability.accelerationSensor",
		title:      "Or using these acceleration sensors...",
		multiple:   true,
		required:   false
	]
    //input name: "A_contact", type: "capability.contactSensor", title: "Or using these contact sensors...", multiple: true, required: false
    def inputContactA = [
        name:       "A_contact",
        type:       "capability.contactSensor",
        title:      "Or using these contact sensors...",
        multiple:   true,
        required:   false
    ]
    //input name: "A_triggerOnce", type: "bool", title: "Trigger only once per day...", defaultValue: false
    def inputTriggerOnceA = [
    	name:       "A_triggerOnce",
        type:       "bool",
        title:      "Trigger only once per day...",
        defaultValue:false
    ]
    //input name: "A_switchDisable", type: "bool", title: "Stop triggering if physical switches/dimmers are turned off...", defaultValue: false
    def inputSwitchDisableA = [
    	name:       "A_switchDisable",
        type:       "bool",
        title:      "Stop triggering if physical switches/dimmers are turned off...",
        defaultValue:false
    ]
    //input name: "A_lock", type: "capability.lock", title: "Or using these locks....", multiple: true, required: false
    def inputLockA = [
        name:       "A_lock",
        type:       "capability.lock",
        title:      "Or using these locks...",
        multiple:   true,
        required:   false
    ]
    //input name: "A_mode", type: "mode", title: "Only during the following modes...", multiple: true, required: false
    def inputModeA = [
        name:       "A_mode",
        type:       "mode",
        title:      "Only during the following modes...",
        multiple:   true,
        required:   false
    ]
    //input name: "A_day", type: "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"], title: "Only on certain days of the week...", multiple: true, required: false
    def inputDayA = [
        name:       "A_day",
        type:       "enum",
        options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
        title:      "Only on certain days of the week...",
        multiple:   true,
        required:   false
    ]
    
    //input name: "A_level", type: "enum", options: [10,20,30,40,50,60,70,80,90,100], title: "Set dimmers to this level", multiple: false, required: false
    def inputLevelA = [
        name:       "A_level",
        type:       "enum",
        options: [10,20,30,40,50,60,70,80,90,100],
        title:      "Set dimmers to this level",
        multiple:   false,
        required:   false
    ]
    
    //input name: "A_turnOnLux", type: "number", title: "Only run this scenario if lux is below...", multiple: false, required: false
    def inputTurnOnLuxA = [
        name:       "A_turnOnLux",
        type:       "number",
        title:      "Only run this scenario if lux is below...",
        multiple:   false,
        required:   false
    ]
    //input name: "A_luxSensors", type: "capability.illuminanceMeasurement", title: "On these lux sensors", multiple: false, required: false
    def inputLuxSensorsA = [
        name:       "A_luxSensors",
        type:       "capability.illuminanceMeasurement",
        title:      "On these lux sensors",
        multiple:   false,
        required:   false
    ]
     //input name: "A_turnOff", type: "number", title: "Turn off this scenario after motion stops or doors close/lock (minutes)...", multiple: false, required: false
    def inputTurnOffA = [
        name:       "A_turnOff",
        type:       "number",
        title:      "Turn off this scenario after motion stops or doors close/lock (minutes)...",
        multiple:   false,
        required:   false
    ]
    //input name: "ScenarioNameA", type: "text", title: "Scenario Name", multiple: false, required: false
    def inputScenarioNameA = [
        name:       "ScenarioNameA",
        type:       "text",
        title:      "Scenario Name",
        multiple:   false,
        required:   false
    ]
    
    def pageProperties = [
        name:       "pageSetupScenarioA",
    ]

    return dynamicPage(pageProperties) {
section("Name your scenario") {
            input inputScenarioNameA
        }

section("Devices included in the scenario") {
            input inputMotionA
			input inputAccelerationA
            input inputContactA
            input inputLockA
            input inputLightsA
            input inputDimmersA
            }

section("Scenario settings") {
            input inputLevelA
            input inputTurnOnLuxA
            input inputLuxSensorsA
            input inputTurnOffA
            }
            
section("Scenario restrictions") {            
            input inputTriggerOnceA
            input inputSwitchDisableA
            href "timeIntervalInputA", title: "Only during a certain time...", description: getTimeLabel(A_timeStart, A_timeEnd), state: greyedOutTime(A_timeStart, A_timeEnd), refreshAfterSelection:true
            input inputDayA
            input inputModeA
            }

section("Help") {
            paragraph helpText()
            }
    }
    
}



def installed() {
    initialize()
}

def updated() {
    unschedule()
    unsubscribe()
    initialize()
}

def initialize() {

	midNightReset()

	if(A_motion) {
		//subscribe(A_motion, "motion", onEventA)
		subscribe(A_motion, "motion", onEventA)
	}

	if(A_acceleration) {
		subscribe(A_acceleration, "acceleration", onEventA)
	}

	if(A_contact) {
		subscribe(A_contact, "contact", onEventA)
	}

	if(A_lock) {
		subscribe(A_lock, "lock", onEventA)
	}

	if(A_switchDisable) {
		subscribe(A_switches, "switch.off", onPressA)
	    subscribe(A_dimmers, "switch.off", onPressA)
	}

}

def onEventA(evt) {

if (/*(!A_triggerOnce || (A_triggerOnce && !state.A_triggered)) && (!A_switchDisable || (A_switchDisable && !state.A_triggered))*/true) { //Checks to make sure this scenario should be triggered more then once in a day
if ((!A_mode || A_mode.contains(location.mode)) && true/*getTimeOk (A_timeStart, A_timeEnd) && getDayOk(A_day)*/) { //checks to make sure we are not opperating outside of set restrictions.
if ((!A_luxSensors) || (A_luxSensors.latestValue("illuminance") <= A_turnOnLux)){ //checks to make sure illimunance is either not cared about or if the value is within the restrictions
def A_levelOn = A_level as Integer

//Check states of each device to see if they are to be ignored or if they meet the requirments of the app to produce an action.
if (getInputOk(A_motion, A_contact, A_lock, A_acceleration)) {
        	log.debug("Motion, Door Open or Unlock Detected Running '${ScenarioNameA}'")
            settings.A_dimmers?.setLevel(A_levelOn)
            settings.A_switches?.on()
            if (A_triggerOnce){
            	state.A_triggered = true
                if (!A_turnOff) {
					runOnce (getMidnight(), midNightReset)
                }
            }
        	if (state.A_timerStart){
            	unschedule(delayTurnOffA)
            	state.A_timerStart = false
        	}
}

//if none of the above paramenters meet the expectation of the app then turn off
else {
		
    	if (settings.A_turnOff) {
			runIn(A_turnOff * 60, "delayTurnOffA")
        	state.A_timerStart = true
        }
        else {
        	settings.A_switches?.off()
			settings.A_dimmers?.setLevel(0)
        	if (state.A_triggered) {
    			runOnce (getMidnight(), midNightReset)
    		}
        }
}
}
}
else{
log.debug("Motion, Contact or Unlock detected outside of mode or time/day restriction.  Not running scenario.")
}
}
}

def delayTurnOffA(){
	settings.A_switches?.off()
	settings.A_dimmers?.setLevel(0)
	state.A_timerStart = false
	if (state.A_triggered) {
    	runOnce (getMidnight(), midNightReset)
    }

}

//when physical switch is actuated disable the scenario
def onPressA(evt) {
if ((!A_mode || A_mode.contains(location.mode)) && /*getTimeOk (A_timeStart, A_timeEnd) && getDayOk(A_day)*/true) { //checks to make sure we are not opperating outside of set restrictions.
if ((!A_luxSensors) || (A_luxSensors.latestValue("illuminance") <= A_turnOnLux)){ 
if (/*(!A_triggerOnce || (A_triggerOnce && !state.A_triggered)) && (!A_switchDisable || (A_switchDisable && !state.A_triggered))*/true) {    
    if (evt.physical){
    	state.A_triggered = true
        unschedule(delayTurnOffA)
        runOnce (getMidnight(), midNightReset)
        log.debug "Physical switch in '${ScenarioNameA}' pressed. Triggers for this scenario disabled."
	}
}
}}}

//Common Methods

//resets once a day trigger at midnight so trigger can be ran again the next day.
def midNightReset() {
	state.A_triggered = false
    state.B_triggered = false
    state.C_triggered = false
    state.D_triggered = false
}

private def helpText() {
	def text =
		"Select motion sensors, acceleration sensors, contact sensors or locks to control a set of lights. " +
        "Each scenario can control dimmers and switches but can also be " +
        "restricted to modes or between certain times and turned off after " +
        "motion stops, doors close or lock. Scenarios can also be limited to  " +
        "running once or to stop running if the physical switches are turned off."
	text
}

//should scenario be marked complete or not
def greyOut(scenario){
	def result = ""
    if (scenario) {
    	result = "complete"	
    }
    result
}

//should i mark the time restriction green or grey
def greyedOutTime(start, end){
	def result = ""
    if (start || end) {
    	result = "complete"	
    }
    result
}


def getTitle(scenario) {
	def title = "Empty"
	if (scenario) {
		title = scenario
    }
	title
}

//recursively applies label to each scenario depending on if the scenario has deatils inside it or not
def getDesc(scenario) {
	def desc = "Tap to create a scenario"
	if (scenario) {
		desc = "Tap to edit scenario"
    }
	desc	
}


def getMidnight() {
	def midnightToday = timeToday("23:59", location.timeZone)
	midnightToday
}

//used to recursively check device states when methods are triggered 
private getInputOk(motion, contact, lock, acceleration) {

def motionDetected = false
def accelerationDetected = false
def contactDetected = false
def unlockDetected = false
def result = false

if (motion) {
	if (motion.latestValue("motion") == "active") {
		motionDetected = true
	}
}

if (acceleration) {
	if (acceleration.latestValue("acceleration") == "active") {
		accelerationDetected = true
	}
}

if (contact) {
	if (contact.latestValue("contact").contains("open")) {
		contactDetected = true
	}
}

if (lock) {
	if (lock.latestValue("lock").contains("unlocked")) {
		unlockDetected = true
	}
}

result = motionDetected || contactDetected || unlockDetected || accelerationDetected
result

}

private getTimeOk(starting, ending) {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting).time
		def stop = timeToday(ending).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
    
    else if (starting){
    	result = currTime >= start
    }
    else if (ending){
    	result = currTime <= stop
    }
    
	log.trace "timeOk = $result"
	result
}

def getTimeLabel(start, end){
	def timeLabel = "Tap to set"
	
    if(start && end){
    	timeLabel = "Between" + " " + hhmm(start) + " "  + "and" + " " +  hhmm(end)
    }
    else if (start) {
		timeLabel = "Start at" + " " + hhmm(start)
    }
    else if(end){
    timeLabel = "End at" + hhmm(end)
    }
	timeLabel	
}

private hhmm(time, fmt = "h:mm a")
{
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}

private getDayOk(dayList) {
	def result = true
    if (dayList) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = dayList.contains(day)
	}
    result
}
