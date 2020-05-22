//Create a class for location variable
package Location

class LocationVar {
	private int contactBookEnabled
    private def modes
	private def timeZone
	private def hubs
    private String mode
	private String locationMode
	private String name
    private List contacts
    private List phoneNumbers
	private String temperatureScale	
	def sendEvent
    	
    	private Phrase helloHome

    	LocationVar(Closure sendEvent, boolean init) {
    	
			if (init) {
				this.hubs = [[id:0, localIP:"128.195.204.105"]]
				this.modes = [[name: "home"],[name: "away"],[name: "night"]]
				this.mode = "away"
				this.locationMode = "away"
				this.helloHome = new Phrase()
				this.contactBookEnabled = 1
				this.contacts = ['AJ']
				this.phoneNumbers = [9495379373]
				this.sendEvent = sendEvent
				this.timeZone = TimeZone.getTimeZone("America/New_York")
				this.name = "hub0"
				this.temperatureScale = "F"
			} else {
				this.hubs = [[id:0, localIP:"128.195.204.105"]]
				this.modes = [[name: "home"],[name: "away"],[name: "night"]]
				this.mode = "home"
				this.locationMode = "home"
				this.helloHome = new Phrase()
				this.contactBookEnabled = 1
				this.contacts = ['AJ']
				this.phoneNumbers = [9495379373]
				this.sendEvent = sendEvent
				this.timeZone = TimeZone.getTimeZone("America/New_York")
				this.name = "hub0"
				this.temperatureScale = "F"
			
			}
    	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (this.mode != eventDataMap['value']) {
			String sentMode = eventDataMap['value']
			println("The location is changed to $sentMode!")
			this.mode = sentMode
			this.locationMode = sentMode
			sendEvent(eventDataMap)
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "sunsetTime" || deviceFeature == "sunset")
			return System.currentTimeMillis()
	}
}
