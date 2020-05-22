/////////////////////////////////////////////////////////////////////
def eventHandler(LinkedHashMap eventDataMap) {
	def value = eventDataMap["value"]
	def name = eventDataMap["name"]
	def deviceId = eventDataMap["deviceId"]
	def descriptionText = eventDataMap["descriptionText"]
	def displayed = eventDataMap["displayed"]
	def linkText = eventDataMap["linkText"]
	def isStateChange = eventDataMap["isStateChange"]
	def unit = eventDataMap["unit"]
	def data = eventDataMap["data"]
	def manualEventsName = ["Touched", "button", "nfcTouch", "threeAxis"]

	def event = new Event(value, name, deviceId, descriptionText, displayed, linkText, linkText, isStateChange, unit, data);
	
	if (manualEventsName.contains(name)) {
		previousValue = true;
		event.setManualTransaction(true);
	} else {
		event.setManualTransaction(false);
	}

	for (int i = 0;i < app2.eventList.size();i++) {
		if (app2.eventList[i] == name &&
                   (app2.valueList[i] == "" || app2.valueList[i] == value)) {
			if (app2.functionList[i] instanceof String) {
				String toCall = app2.functionList[i]
				app2."$toCall"(event)
			}
			else
				app2.functionList[i](event)
		}
	}

	for (int i = 0;i < app1.eventList.size();i++) {
		if (app1.eventList[i] == name &&
                   (app1.valueList[i] == "" || app1.valueList[i] == value)) {
			if (app1.functionList[i] instanceof String) {
				String toCall = app1.functionList[i]
				app1."$toCall"(event)
			}
			else
				app1.functionList[i](event)
		}
	}

        if (manualEventsName.contains(name))
		previousValue = false;
	else if (previousValue)
		event.setManualTransaction(true);
}
