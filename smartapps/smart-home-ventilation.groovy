/**
 *	Smart Home Ventilation
 *	Version 2.1.2 - 5/31/15
 *
 *	Copyright 2015 Michael Struck
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *	in compliance with the License. You may obtain a copy of the License at:
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *	for the specific language governing permissions and limitations under the License.
 *
 */

definition(
	name: "Smart Home Ventilation",
    namespace: "MichaelStruck",
    author: "Michael Struck",
    description: "Allows for setting up various schedule scenarios for turning on and off home ventilation switches.",
    category: "Convenience",
    iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Home-Ventilation/HomeVent.png",
    iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Home-Ventilation/HomeVent@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/Smart-Home-Ventilation/HomeVent@2x.png")

preferences {
	page name: "mainPage"
}

//----Scheduling Pages
page(name: "A_Scenario") {
    	section{
			input "timeOnA1", title: "Schedule 1 time to turn on", "time", required: false
        	input "timeOffA1", title: "Schedule 1 time to turn off", "time", required: false
		}
    	section{
			input "timeOnA2", title: "Schedule 2 time to turn on", "time", required: false
        	input "timeOffA2", title: "Schedule 2 time to turn off", "time", required: false
		}
    	section{
        	input "timeOnA3", title: "Schedule 3 time to turn on", "time", required: false
        	input "timeOffA3", title: "Schedule 3 time to turn off", "time", required: false
		}
    	section{
        	input "timeOnA4", title: "Schedule 4 time to turn on", "time", required: false
        	input "timeOffA4", title: "Schedule 4 time to turn off", "time", required: false
		}
		section ("Options") {
    		input "titleA", title: "Assign a scenario name", "text", required: false
            input "modeA", "mode", required: false, multiple: true, title: "Run in specific mode(s)", description: "Choose Modes"
		   	input "daysA", "enum", multiple: true, title: "Run on specific day(s)", description: "Choose Days", required: false, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
		}
    }


page(name: "pageAbout", title: "About ${textAppName()}") {
        section {
            paragraph "${textVersion()}\n${textCopyright()}\n\n${textLicense()}\n"
        }
        section("Instructions") {
            paragraph textHelp()
        }
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", install: true, uninstall: true) {
     	section("Select ventilation switches..."){
			input "switches", title: "Switches", "capability.switch", multiple: true
		}
        section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false, defaultValue: "Smart Home Ventilation")
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
        }
    }
}

// Install and initiate

def installed() {
    log.debug "Installed with settings: ${settings}"
    init()
}

def updated() {
    unschedule()
    turnOffSwitch() //Turn off all switches if the schedules are changed while in mid-schedule
    unsubscribe()
    log.debug "Updated with settings: ${settings}"
    init()
}

def init() {
    schedule ("someTime", midNight)
    subscribe(location, "mode", locationHandler)
    startProcess()
}

// Common methods

def startProcess () {
        startDay()
}

def startDay() {
    turnOnSwitch()
}

def incDay() {
    turnOffSwitch()
}

def locationHandler(evt) {
    def result = false
    state.modeChange = true
    switches.each {
    	if (it.currentValue("switch")=="on"){
           result = true
        }
    }
	if (!result) {
    	startProcess()
    }
}

def midNight(){
    startProcess()
}

def turnOnSwitch() {
    switches.on()
    log.debug "Home ventilation switches are on."
}

def turnOffSwitch() {
    switches.each {
    	if (it.currentValue("switch")=="on"){
			it.off()
        }
    }
    log.debug "Home ventilation switches are off."
}


//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Smart Home Ventilation"
}

private def textVersion() {
    def text = "Version 2.1.2 (05/31/2015)"
}

private def textCopyright() {
    def text = "Copyright © 2015 Michael Struck"
}

private def textLicense() {
    def text =
		"Licensed under the Apache License, Version 2.0 (the 'License'); "+
		"you may not use this file except in compliance with the License. "+
		"You may obtain a copy of the License at"+
		"\n\n"+
		"    http://www.apache.org/licenses/LICENSE-2.0"+
		"\n\n"+
		"Unless required by applicable law or agreed to in writing, software "+
		"distributed under the License is distributed on an 'AS IS' BASIS, "+
		"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
		"See the License for the specific language governing permissions and "+
		"limitations under the License."
}

private def textHelp() {
	def text =
    	"Within each scenario, choose a start and end time for the ventilation fan. You can have up to 4 different " +
        "venting scenarios, and 4 schedules within each scenario. Each scenario can be restricted to specific modes or certain days of the week. It is recommended "+
        "that each scenario does not overlap and run in separate modes (i.e. Home, Out of town, etc). Also note that you should  " +
        "avoid scheduling the ventilation fan at exactly midnight; the app resets itself at that time. It is suggested to start any new schedule " +
        "at 12:15 am or later."
}
