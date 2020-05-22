//Create a class for beacon sensor
package BeaconSensor
import Timer.SimulatedTimer

public class BeaconSensor {
	private String id
	private String label
	private String displayName
	private String presence
	private String currentPresence
	private String presenceLatestValue

	BeaconSensor(String id, String label, String displayName, String presence, String presenceLatestValue) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.presence = presence
		this.currentPresence = presence
		this.presenceLatestValue = presenceLatestValue
	}

	def setValue(String value) {
		println("the beacon sensor with id:$id is triggered to $value!")
		this.presenceLatestValue = value
		this.presence = value
		this.currentPresence = value
	}

	
	def currentValue(String deviceFeature) {
		if (deviceFeature == "beacon") {
			return presence
		}
	}

	def latestValue(String deviceFeature) {
		if (deviceFeature == "beacon") {
			return presenceLatestValue
		}
	}
}
