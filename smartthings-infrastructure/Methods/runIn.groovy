/////////////////////////////////////////////////////////////////////
////runIn(time, func)
def runIn(int seconds, Closure functionToCall) {
	/*if (timersFuncList.contains(functionToCall)) {
		timersList[timersFuncList.indexOf(functionToCall)].cancel()
		def task = timersList[timersFuncList.indexOf(functionToCall)].runAfter(1000*seconds*0, functionToCall)
	} else {
		timersFuncList.add(functionToCall)
		timersList.add(new SimulatedTimer())
		def task = timersList[timersFuncList.indexOf(functionToCall)].runAfter(1000*seconds*0, functionToCall)
	}*/
	functionToCall()
}

def runIn(int seconds, Closure functionToCall, LinkedHashMap metaData) {
	runIn(seconds, functionToCall)
}

def runIn(int seconds, String nameOfFunction, LinkedHashMap metaData) {
	runIn(seconds, nameOfFunction)
}

def runIn(int seconds, String nameOfFunction) {
	/*timersFuncList.add(nameOfFunction)
	timersList.add(new SimulatedTimer())
	def task = timersList[timersFuncList.indexOf(nameOfFunction)].runAfter(seconds*1000*0) {
		"$nameOfFunction"()
	}*/
	"$nameOfFunction"()
}
