/////////////////////////////////////////////////////////////////////
////subscribe(obj, func)
def subscribe(Object obj, Closure FunctionToCall) {
	if (obj == app) {
		objectList.add(obj)
		eventList.add("Touched")
                valueList.add("")
		functionList.add(FunctionToCall)
	} else if (obj == location) {
		objectList.add(obj)
		eventList.add("Location")
                valueList.add("")
		functionList.add(FunctionToCall)
	}
}

////subscribe(obj, event, func)
def subscribe(Object obj, String event, Closure FunctionToCall) {
    if (event == "tamper.tampered") {
       event = "contact"      //This really should be its own name
    } else if ((event == "mode")||(event == "mode.away")||(event == "mode.home")||(event == "mode.night")) {
       //This really should be fixed also...
       event = "Location"
    } else if (event == "unlocked") {
      return
    }
    
    int dot = event.indexOf('.')
    String name = ""
    String value = ""
    if (dot != -1) {
      name = event.substring(0, dot)
      value = event.substring(dot + 1)
    } else {
      name = event
    }

    objectList.add(obj)
    eventList.add(name)
    functionList.add(FunctionToCall)
    valueList.add(value)
}
////subscribe(obj, event, nameOfFunc)
def subscribe(Object obj, String event, String FunctionToCall) {
    if (event == "tamper.tampered") {
       event = "contact"      //This really should be its own name
    } else if ((event == "mode")||(event == "mode.away")||(event == "mode.home")||(event == "mode.night")) {
       event = "Location"
    } else if (event == "unlocked") {
       return
    }

    int dot = event.indexOf('.')
    String name = ""
    String value = ""
    if (dot != -1) {
      name = event.substring(0, dot)
      value = event.substring(dot + 1)
    } else {
      name = event
    }

    objectList.add(obj)
    eventList.add(name)
    functionList.add(FunctionToCall)
    valueList.add(value)
}

////subscribe(obj, event, func, data)
def subscribe(Object obj, String event, Closure FunctionToCall, LinkedHashMap metaData) {
	subscribe(obj, event, FunctionToCall)
}
