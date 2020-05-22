import groovy.transform.CompileStatic

// This function runs when the SmartApp is installed
@CompileStatic
def installed() {
    // This is a standard debug statement in Groovy
    //log.debug "Installed with settings: ${settings}"
    initialize()
}

// This function runs when the SmartApp has been updated
@CompileStatic
def updated() {
    //log.debug "Updated with settings: ${settings}"
    // Notice that all event subscriptions are removed when a SmartApp is updated
    //unsubscribe()
    initialize()
}

// This function is where you initialize callbacks for event listeners
@CompileStatic
def initialize() {
    // The subscribe function takes a input, a state, and a callback method
    //subscribe(contact, "contact.open", openHandler)
    //subscribe(contact, "contact.closed", closedHandler)
}

// These are our callback methods
@CompileStatic
def openHandler(evt) {
    //log.debug "$evt.name: $evt.value"
    // Turn the light on
    //light.on()
}

@CompileStatic
def closedHandler(evt) {
    //log.debug "$evt.name: $evt.value"
    // Turn the light off and lock the lock
    //light.off()
}
