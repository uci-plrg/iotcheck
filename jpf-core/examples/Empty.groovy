class Empty {

	// This function runs when the SmartApp is installed
	def installed() {
		// This is a standard debug statement in Groovy
		//log.debug "Installed with settings: ${settings}"
		//int x = 5;
		//int y = 6;
		//int result = x + y;
		//return result;
		println "installed() is called!"
		boolean thisBoolean = false;
		initialize(thisBoolean)
	}

	// This function is where you initialize callbacks for event listeners
	def initialize(thisBoolean) {
		// The subscribe function takes a input, a state, and a callback method
		//subscribe(contact, "contact.open", openHandler)
		//subscribe(contact, "contact.closed", closedHandler)
		thisBoolean = true;
		println "Boolean is now " + thisBoolean
		println "initialize() is called!"
	}

	static void main(String[] args) {		

		Empty empty = new Empty();
		//int result = emp.installed();
		//println result;
		empty.installed()
		int x = 5;
		int y = 6;
		int result = x + y;
		println result
		println "End of call!"
	}	
}
