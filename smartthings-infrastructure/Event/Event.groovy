//Create a class for Events
package Event
import groovy.json.JsonSlurper

public class Event {
	private String deviceId
	private String value
	private String linkText
	private String displayName
	private boolean displayed
	private String name
	private String descriptionText
	private boolean isStateChange
	private String unit
	private String data
	private jsonData
	private int integerValue
	private double doubleValue
	private boolean physical
	private def date
	private boolean isManualTransaction
	
	Event(String value, String name, String deviceId, String descriptionText, boolean displayed, String linkText, String displayName, boolean isStateChange, String unit, String data) {
		this.deviceId = deviceId
		this.linkText = linkText
		this.value = value
		this.displayName = displayName
		this.name = name
		this.descriptionText = descriptionText
		this.isStateChange = isStateChange
		this.unit = unit
		this.data = data
		this.jsonData = new groovy.json.JsonSlurper().parseText(data)
		this.displayed = displayed
		if (["battery", "hue", "saturation", "energy", "level", "temperature", "heatingSetpoint", "coolingSetpoint", "thermostatSetpoint", "illuminance"].contains(name)) {
			int dot = value.indexOf('.')
			if (dot != -1)
	      		value = value.substring(0, dot)
			this.integerValue = value.toInteger()
			this.doubleValue = Double.parseDouble(value);		
		}
		this.physical = true
		this.date = new Date()
	}

	void setManualTransaction(boolean isTrue) {
		isManualTransaction = isTrue;
	}
}
