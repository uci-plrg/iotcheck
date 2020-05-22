//Create a class for speech synthesis
package SpeechSynthesis
import Timer.SimulatedTimer

public class SpeechSynthesis {
	private String id
	private String label
	private String displayName
	private int level
	private boolean oneUser


	SpeechSynthesis(String id, String label, String displayName, int level, boolean oneUser) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.level = level
		this.oneUser = oneUser
	}

	def setLevel(int level) {
		if (level != this.level) {
			println("The level of speech synthesis with id:$id is changed to $level")
			this.level = level
		}
	}

	def speak(String message) {
		println("Speech synthesis with id:$id, SPEAKING:\"$message\"!")
		// As a conflict variable
		if (oneUser) {
			this.oneUser = false
		} else {
			this.oneUser = true
		}
	}
}
