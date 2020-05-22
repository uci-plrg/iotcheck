/////////////////////////////////////////////////////////////////////
////schedule(time, nameOfFunction as String)
def schedule(String time, String nameOfFunction) {
	/*def _inputTime = time.split(':')
	Date date = new Date()	
	def _currentTime = date.format("HH:mm:ss").split(':')

	Convert input time and current time to minutes
	def inputTime = Integer.parseInt(_inputTime[0])*3600+Integer.parseInt(_inputTime[1])*60
	def currentTime = Integer.parseInt(_currentTime[0])*3600+Integer.parseInt(_currentTime[1])*60+Integer.parseInt(_currentTime[2])
	def delay

	if (inputTime < currentTime) {
		delay = 24*60*60-inputTime+currentTime
	} else {
		delay = inputTime-currentTime
	}
	timersFuncList.add(nameOfFunction)
	timersList.add(new SimulatedTimer())
	def task = timersList[timersFuncList.indexOf(nameOfFunction)].runAfter(delay*1000*0) {
		"$nameOfFunction"()
	}*/
	"$nameOfFunction"()
}
/////////////////////////////////////////////////////////////////////
////schedule(Date, nameOfFunction as String)
def schedule(Date date, String nameOfFunction) {
	"$nameOfFunction"()
}
////schedule(time, nameOfFunction as Closure)
def schedule(String time, Closure nameOfFunction) {
	/*def _inputTime = time.split(':')
	Date date = new Date()	
	def _currentTime = date.format("HH:mm:ss").split(':')

	Convert input time and current time to minutes
	def inputTime = Integer.parseInt(_inputTime[0])*3600+Integer.parseInt(_inputTime[1])*60
	def currentTime = Integer.parseInt(_currentTime[0])*3600+Integer.parseInt(_currentTime[1])*60+Integer.parseInt(_currentTime[2])
	def delay

	if (inputTime < currentTime) {
		delay = 24*60*60-inputTime+currentTime
	} else {
		delay = inputTime-currentTime
	}

	if (timersFuncList.contains(nameOfFunction)) {
		timersList[timersFuncList.indexOf(nameOfFunction)].cancel()
		def task = timersList[timersFuncList.indexOf(nameOfFunction)].runAfter(0, nameOfFunction)
	} else {
		timersFuncList.add(nameOfFunction)
		timersList.add(new SimulatedTimer())
		def task = timersList[timersFuncList.indexOf(nameOfFunction)].runAfter(0, nameOfFunction)
	}*/
	nameOfFunction()
}
