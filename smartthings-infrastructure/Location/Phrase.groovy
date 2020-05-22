//Create a class for Phrase
package Location

class Phrase {
	private phrases
	
	Phrase() {
		this.phrases = [[id:0, label:"Good Night!"],[id:1, label:"Good Morning!"],
				[id:2, label:"Goodbye!"],[id:3, label:"I'm Back!"]]
	} 
	def getPhrases() {
		return this.phrases
	}
	def execute(String phrase) {
		println(phrase)
	}
}

