/////////////////////////////////////////////////////////////////////
def timeToday(String time, Object timeZone) {
	def timeOfDay = new Date()
	def _inputTime = time.split(':')
	def inputTime = Integer.parseInt(_inputTime[0])*3600+Integer.parseInt(_inputTime[1])*60+1564191100415
	timeOfDay.time = inputTime
	return timeOfDay
}

def timeToday(String time) {
	def timeOfDay = new Date()
	def _inputTime = time.split(':')
	def inputTime = Integer.parseInt(_inputTime[0])*3600+Integer.parseInt(_inputTime[1])*60+1564191100415
	timeOfDay.time = inputTime
	return timeOfDay
}


def timeTodayAfter(Date date, String time, Object timeZone) {
	def timeOfDay = new Date()
	def _inputTime = time.split(':')
	def inputTime = Integer.parseInt(_inputTime[0])*3600+Integer.parseInt(_inputTime[1])*60+1564191100415
	timeOfDay.time = inputTime
	return timeOfDay
}
