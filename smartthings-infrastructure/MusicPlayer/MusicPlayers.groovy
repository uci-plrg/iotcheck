//Create a class for music player
package MusicPlayer
import Timer.SimulatedTimer

public class MusicPlayers {
	private int deviceNumbers
	private List musicPlayers
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "musicPlayerID0"
	private String label = "musicPlayer0"
	private String displayName = "musicPlayer0"
	private int level = 20
	private int duration = 10
	private String mute = "unmuted"
	private String status = "pause"
	private int trackNumber = 1
	private String trackData = "someTrack"
	private String trackDescription = "someDescriptions"

	
		
	MusicPlayers(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent
		this.deviceNumbers = deviceNumbers
		this.musicPlayers = []
		
		if (init) {
			this.level = 20
			this.duration = 10
			this.mute = "unmuted"
			this.status = "paused"
			this.trackNumber = 1
			this.trackData = "someTrack"
			this.trackDescription = "someDescriptions"
		} else {
			this.level = 30
			this.duration = 20
			this.mute = "muted"
			this.status = "playing"
			this.trackNumber = 2
			this.trackData = "someOtherTrack"
			this.trackDescription = "someOtherDescriptions"
		}
		musicPlayers.add(new MusicPlayer(sendEvent, id, label, displayName, this.level, this.mute, this.status, this.trackNumber, this.trackData, this.trackDescription))
	}

	//By model checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["name"] == "status") {
			if (eventDataMap["value"] != musicPlayers[0].status) {
				this.status = eventDataMap["value"]
				musicPlayers[0].setValue(eventDataMap["value"], "status")
				sendEvent(eventDataMap)
			}	
		} else if (eventDataMap["name"] == "level") {
			if (eventDataMap["value"].toInteger() != musicPlayers[0].level) {
				this.level = eventDataMap["value"].toInteger()
				musicPlayers[0].setValue(eventDataMap["value"], "level")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "trackDescription") {
			if (eventDataMap["value"] != musicPlayers[0].trackDescription) {
				this.trackDescription = eventDataMap["value"]
				musicPlayers[0].setValue(eventDataMap["value"], "trackDescription")
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "trackData") {
			if (eventDataMap["value"] != musicPlayers[0].trackData) {
				this.trackData = eventDataMap["value"]
				musicPlayers[0].setValue(eventDataMap["value"], "trackData")				
				sendEvent(eventDataMap)
			}
		} else if (eventDataMap["name"] == "mute") {
			if (eventDataMap["value"] != musicPlayers[0].mute) {
				this.mute = eventDataMap["value"]
				musicPlayers[0].setValue(eventDataMap["value"], "mute")
				sendEvent(eventDataMap)
			}
		}
	}

	//Methods for closures
	def count(Closure Input) {
		musicPlayers.count(Input)
	}
	def size() {
		musicPlayers.size()
	}
	def each(Closure Input) {
		musicPlayers.each(Input)
	}
	def find(Closure Input) {
		musicPlayers.find(Input)
	}
	def sort(Closure Input) {
		musicPlayers.sort(Input)
	}
	def collect(Closure Input) {
		musicPlayers.collect(Input)
	}

	//methods
	def on(LinkedHashMap metaData) {
		on()
	}
	def on() {
		if (status != "on") {
			this.status = "on"
			musicPlayers[0].on()
		}
	}
	def off(LinkedHashMap metaData) {
		off()
	}
	def off() {
		if (status != "off") {
			this.status = "off"
			musicPlayers[0].off()
		}
	}
	def mute(LinkedHashMap metaData) {
		mute()
	}
	def mute() {
		if (mute != "muted") {
			this.mute = "muted"
			musicPlayers[0].mute()
		}
	}
	def nextTrack(LinkedHashMap metaData) {
		nextTrack()
	}
	def nextTrack() {
		if (status != "play") {
			this.status = "play"
		}
		this.trackNumber = this.trackNumber+1
		musicPlayers[0].nextTrack()
	}
	def pause(LinkedHashMap metaData) {
		pause()
	}
	def pause() {
		if (status != "pause") {
			this.status = "pause"
			musicPlayers[0].pause()			
		}
	}
	def play(LinkedHashMap metaData) {
		play()
	}
	def play() {
		if (status != "play") {
			this.status = "play"
			musicPlayers[0].play()
		}
	}
	def playTrack(LinkedHashMap metaData) {
		playTrack("someTrack")
	}
	def playTrackAndResume(String trackData, int duration) {
		playTrack(trackData)
		if (duration != this.duration) {
			this.duration = duration
		}
	}
	def playTrackAndResume(String trackData, int duration, int level) {
		playTrackAndResume(trackData, duration)
		if (level != this.level) {
			this.level = level
		}
	}
	def playSoundAndTrack(String trackData, int duration, int trackNumber, int level) {
		playTrackAndResume(trackData, duration)
		if (level != this.level) {
			this.level = level
		}
		if (trackNumber!= this.trackNumber) {
			this.trackNumber = trackNumber
		}
	}
	def playTrackAtVolume(String trackData, int level) {
		playTrack(trackData)
		if (level != this.level) {
			this.level = level
		}
	}
	def playTrack(String trackToPlay) {
		if (status != "play") {
			this.status = "play"
		}
		musicPlayers[0].playTrack(trackToPlay)
	}
	def previousTrack(LinkedHashMap metaData) {
		previousTrack()
	}
	def previousTrack() {
		if (status != "play") {
			this.status = "play"
		}
		if (this.trackNumber != 1)
			this.trackNumber = this.trackNumber-1
		musicPlayers[0].previousTrack()
	}
	def restoreTrack(LinkedHashMap metaData) {
		restoreTrack()
	}
	def restoreTrack(String trackToRestore) {
		musicPlayers[0].restoreTrack(trackToRestore)
	}
	def resumeTrack(LinkedHashMap metaData) {
		resumeTrack()
	}
	def resumeTrack(String trackToResume) {
		if (status != "play") {
			this.status = "play"
		}
		musicPlayers[0].resumeTrack(trackToResume)
	}
	def setLevel(LinkedHashMap metaData) {
		setLevel()
	}
	def setLevel(int level) {
		if (level != this.level) {
			this.level = level	
			musicPlayers[0].setLevel(level)
		}
	}
	def setTrack(LinkedHashMap metaData) {
		setTrack()
	}
	def setTrack(String trackToSet) {
		if (status != "play") {
			this.status = "play"
		}
		musicPlayers[0].setTrack(trackToSet)
	}
	def stop(LinkedHashMap metaData) {
		stop()
	}
	def stop() {
		if (status != "stop") {
			this.status = "stop"
			musicPlayers[0].stop()
		}
	}
	def statesSince(String info, Date dateObj, LinkedHashMap map) {
		return musicPlayers[0].statesSince()
	}
	def playText(LinkedHashMap metaData) {
		playText()
	}
	def playText(String text) {
		if (status != "play") {
			this.status = "play"
		}
		musicPlayers[0].playText(text)
	}

	def currentValue(String deviceFeature) {
		musicPlayers[0].currentValue(deviceFeature)
	}

	def latestValue(String deviceFeature) {
		musicPlayers[0].latestValue(deviceFeature)
	}

	def getAt(int ix) {
		musicPlayers[ix]
	}
}
