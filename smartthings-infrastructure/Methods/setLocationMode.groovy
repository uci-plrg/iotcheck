/////////////////////////////////////////////////////////////////////
def setLocationMode(String mode) {
	log.debug "DEBUG: setLocationMode is called. Current mode is: ${location_mode} and new mode is: ${mode}"
	location.setValue([name: "Location", value: "$mode", deviceId: "locationID0", descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
	location.setValue([name: "mode", value: "$mode", deviceId: "locationID0", descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
	location_mode = mode
}

