//Create a class for speech synthesis
package SpeechSynthesis
import Timer.SimulatedTimer

public class SpeechSynthesises {
	private int deviceNumbers
	private List speechSynthesises
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "speechSynthesisID0"
	private String label = "speechSynthesis0"
	private String displayName = "speechSynthesis0"
	private int level = 50
	private boolean oneUser = true

		
	SpeechSynthesises(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.speechSynthesises = []

		if (init) {
			this.level = 50
			this.oneUser = true
		} else {
			this.level = 60
			this.oneUser = false
		}
		speechSynthesises.add(new SpeechSynthesis(id, label, displayName, this.level, this.oneUser))
	}

	//Methods for closures
	def count(Closure Input) {
		speechSynthesises.count(Input)
	}
	def size() {
		speechSynthesises.size()
	}
	def each(Closure Input) {
		speechSynthesises.each(Input)
	}
	def find(Closure Input) {
		speechSynthesises.find(Input)
	}
	def sort(Closure Input) {
		speechSynthesises.sort(Input)
	}
	def collect(Closure Input) {
		speechSynthesises.collect(Input)
	}

	def setLevel(int level) {
		if (level != this.level) {
			this.level = level
			speechSynthesises[0].setLevel(level)
		}
	}

	def speak(String message) {
		speechSynthesises[0].speak(message)
		// As a conflict variable
		if (oneUser) {
			this.oneUser = false
		} else {
			this.oneUser = true
		}
	}

	def getAt(int ix) {
		speechSynthesises[ix]
	}
}
