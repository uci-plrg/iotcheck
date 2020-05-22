//Create a class for button
package Button
import Timer.SimulatedTimer

public class Buttons {
	private int deviceNumbers
	private List buttons
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "buttonID0"
	private String label = "button0"
	private String displayName = "button0"
	private String button = "pushed"
	private int numberOfButtons = 4
	
		
	Buttons(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.buttons = []

		if (init) {
			this.button = "pushed"
		} else {
			this.button = "held"
		}
		buttons.add(new Button(id, label, displayName, button, numberOfButtons))
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		buttons[0].setValue(eventDataMap)
		sendEvent(eventDataMap)
	}

	//Methods for closures
	def count(Closure Input) {
		buttons.count(Input)
	}
	def size() {
		buttons.size()
	}
	def each(Closure Input) {
		buttons.each(Input)
	}
	def sort(Closure Input) {
		buttons.sort(Input)
	}
	def find(Closure Input) {
		buttons.find(Input)
	}
	def collect(Closure Input) {
		buttons.collect(Input)
	}


	//methods
	def eventsSince(Date dateObj) {
		return buttons[0].eventsSince()
	}


	def getAt(int ix) {
		buttons[ix]
	}
}
