//Create a class for contact sensor
package ContactSensor
import Timer.SimulatedTimer

public class ContactSensors {
	private int deviceNumbers
	private List contacts
	def sendEvent

	//For one device(We cannot have obj.id)-> We should have obj[0].id
	private String id = "contactSensorID0"
	private String label = "contactSensor0"
	private String displayName = "contactSensor0"
	private String contactState = "closed"
	private String currentContact = "closed"
	private String latestValue = "closed"
	private String alarmState = "armed"

		
	ContactSensors(Closure sendEvent, int deviceNumbers, boolean init) {
		this.sendEvent = sendEvent		
		this.deviceNumbers = deviceNumbers
		this.contacts = []
		
		if (init) {
			this.contactState = "closed"
			this.currentContact = "closed"
			this.latestValue = "closed"
			this.alarmState = "armed"
		} else {
			this.contactState = "open"
			this.currentContact = "open"
			this.latestValue = "open"
			this.alarmState = "not armed"
		}

		contacts.add(new ContactSensor(id, label, displayName, this.contactState, this.currentContact, this.alarmState, this.latestValue))
	}

	//Methods for closures
	def count(Closure Input) {
		contacts.count(Input)
	}
	def size() {
		contacts.size()
	}
	def each(Closure Input) {
		contacts.each(Input)
	}
	def find(Closure Input) {
		contacts.find(Input)
	}
	def sort(Closure Input) {
		contacts.sort(Input)
	}
	def collect(Closure Input) {
		contacts.collect(Input)
	}

	//By Model Checker
	def setValue(LinkedHashMap eventDataMap) {
		if (eventDataMap["value"] != contacts[0].contactState) {
			this.latestValue = eventDataMap["value"]
			this.contactState = eventDataMap["value"]
			this.currentContact = eventDataMap["value"]
			contacts[0].setValue(eventDataMap["value"])
			sendEvent(eventDataMap)
		}
	}

	def eventsSince(Date dateObj) {
		return contacts[0].eventsSince()
	}

	def on() {
		this.alarmState = "armed"
		contacts[0].on()
	}

	def off() {
		this.alarmState = "not armed"
		contacts[0].off()
	}

	def currentValue(String deviceFeature) {
		contacts[0].currentValue(deviceFeature)//It is called if we have only one device
	}

	def currentState(String deviceFeature) {
		contacts[0].currentState(deviceFeature)//It is called if we have only one device
	}

	def latestValue(String deviceFeature) {
		contacts[0].latestValue(deviceFeature)//It is called if we have only one device
	}

	def getAt(int ix) {
		contacts[ix]
	}
}
