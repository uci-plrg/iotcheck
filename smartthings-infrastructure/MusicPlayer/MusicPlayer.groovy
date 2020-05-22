//Create a class for music player
package MusicPlayer
import Timer.SimulatedTimer

//JPF's Verify API
import gov.nasa.jpf.vm.Verify

public class MusicPlayer {
	private String id
	private String label
	private String displayName
	private int level
	private String mute
	private String status
	private int trackNumber
	private String trackData
	private String trackDescription
	def sendEvent
	
	MusicPlayer(Closure sendEvent, String id, String label, String displayName, int level, String mute, String status, int trackNumber, String trackData, String trackDescription) {
		this.sendEvent = sendEvent
		this.id = id
		this.label = label
		this.displayName = displayName
		this.level = level
		this.mute = mute
		this.status = status
		this.trackNumber = trackNumber
		this.trackData = trackData
		this.trackDescription = trackDescription
	}

	//By model checker
	def setValue(String value, String name) {
		if ((name == "status") && (value != this.status)) {
			this.status = value
			println("the status of the music player with id:$id is changed to $value!")
		} else if ((name == "level") && (value != this.level)) {
			this.level = value.toInteger()
			println("the level sound of the music player with id:$id is changed to $value!")
		} else if ((name == "trackDescription") && (value != this.trackDescription)) {
			this.trackDescription = value
			println("the trackDescription of the music player with id:$id is changed to $value!")
		} else if ((name == "trackData") && (value != this.trackData)) {
			this.trackData = value
			println("the trackData of the music player with id:$id is changed to $value!")
		} else if ((name == "mute") && (value != this.mute)) {
			this.mute = value
			println("the mute state of the music player with id:$id is changed to $value!")
		}
	}

	//methods
	def on(LinkedHashMap metaData) {
		on()
	}
	def on() {
		if (status != "on") {
			println("the music player with id:$id is on!")
			this.status = "on"
			sendEvent([name: "status", value: "on", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def off(LinkedHashMap metaData) {
		off()
	}
	def off() {
		if (status != "off") {
			println("the music player with id:$id is off!")
			this.status = "off"
			sendEvent([name: "status", value: "off", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def mute(LinkedHashMap metaData) {
		mute()
	}
	def mute() {
		if (mute != "muted") {
			println("the music player with id:$id is muted!")
			this.mute = "muted"
			sendEvent([name: "mute", value: "muted", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def nextTrack(LinkedHashMap metaData) {
		nextTrack()
	}
	def nextTrack() {
		trackNumber = trackNumber+1
		def trackPlaying = trackData
		println("the $trackPlaying is selected!")
		if (status != "play") {
			this.status = "play"
			sendEvent([name: "status", value: "play", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
		sendEvent([name: "trackDescription", value: "someDescriptions", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		sendEvent([name: "trackData", value: "someTrack", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
	}
	def pause(LinkedHashMap metaData) {
		pause()
	}
	def pause() {
		if (status != "pause") {
			println("the music player with id:$id is paused!")
			this.status = "pause"
			sendEvent([name: "status", value: "pause", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def play(LinkedHashMap metaData) {
		play()
	}
	def play() {
		if (status != "play") {
			println("the music player with id:$id is starting to play!")
			this.status = "play"
			sendEvent([name: "status", value: "play", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def playTrack(LinkedHashMap metaData) {
		playTrack()
	}
	def playTrack(String trackToPlay) {
		def trackPlaying = trackData
		println("the $trackPlaying is selected to play!")
		if (status != "play") {		
			this.status = "play"
			sendEvent([name: "status", value: "play", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
		sendEvent([name: "trackDescription", value: "someDescriptions", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		sendEvent([name: "trackData", value: "someTrack", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
	}
	def previousTrack(LinkedHashMap metaData) {
		previousTrack()
	}
	def previousTrack() {
		if (trackNumber != 1)
			trackNumber = trackNumber-1
		def trackPlaying = trackData
		println("the $trackPlaying is selected!")
		if (status != "playing") {
			this.status = "playing"
			sendEvent([name: "status", value: "playing", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
		sendEvent([name: "trackDescription", value: "someDescriptions", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		sendEvent([name: "trackData", value: "someTrack", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
	}
	/*def restoreTrack(String trackToRestore) {
		musicPlayers*.restoreTrack(trackToRestore)
	}*/
	def resumeTrack(LinkedHashMap metaData) {
		resumeTrack()
	}
	def resumeTrack(String trackToResume) {
		def trackPlaying = trackData
		println("the $trackPlaying is resumed!")
		if (status != "play") {
			this.status = "play"
			sendEvent([name: "status", value: "play", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def setLevel(LinkedHashMap metaData) {
		setLevel()
	}
	def setLevel(int level) {
		if (level != this.level) {
			this.level = level
			println("the level of sound is changed to $level!")
			sendEvent([name: "level", value: "$level", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def setTrack(LinkedHashMap metaData) {
		setTrack()
	}
	def setTrack(String trackToSet) {
		def trackPlaying = trackData
		println("the $trackPlaying is set!")
		if (status != "play") {
			this.status = "play"
			sendEvent([name: "status", value: "play", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
		sendEvent([name: "trackDescription", value: "someDescriptions", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		sendEvent([name: "trackData", value: "someTrack", deviceId: this.id, descriptionText: "",
			   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
	}
	def stop(LinkedHashMap metaData) {
		stop()
	}
	def stop() {
		if (status != "stop") {
			println("the music player with id:$id is stop!")
			this.status = "stop"
			sendEvent([name: "status", value: "stop", deviceId: this.id, descriptionText: "",
				   displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}
	def statesSince(String info, Date dateObj) {
		statesSince()
	}
	def statesSince() {
		eventsSince()
	}
	def eventsSince(Date dateObj) {
		eventsSince()
	}

	def eventsSince() {
		def evtActive = [[name: "status", value: "on", deviceId: "musicPlayerID0", descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def evtInactive = [[name: "status", value: "off", deviceId: "musicPlayerID0", descriptionText: "",
				    displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}']]
		def init = Verify.getInt(0,4)
		def evtToSend = []
		if (init == 0) {//return empty set
			return evtToSend
		} else if (init == 1) {//send one active event
			evtActive.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 2) {//send two active events
			evtActive.each{
				evtToSend.add(it)
			}
			evtActive.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 3) {//send one inactive event
			evtInactive.each{
				evtToSend.add(it)
			}
			return evtToSend
		} else if (init == 4) {//send two inactive events
			evtInactive.each{
				evtToSend.add(it)
			}
			evtInactive.each{
				evtToSend.add(it)
			}
			return evtToSend
		}
	}
	def playText(LinkedHashMap metaData) {
		playText()
	}
	def playText(String text) {
		println("the music player with id:$id is playing the text:$text!")
		if (status != "play") {
			this.status = "play"
			sendEvent([name: "status", value: "play", deviceId: this.id, descriptionText: "",
				  displayed: true, linkText: "", isStateChange: false, unit: "", data: '{"info": "info"}'])
		}
	}

	def currentValue(String deviceFeature) {
		if (deviceFeature == "playpause") {
			return status
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "playpause") {
			return status
		}
	}
}
