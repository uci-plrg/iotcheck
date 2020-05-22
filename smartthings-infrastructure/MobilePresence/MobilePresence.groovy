//Create a class for mobile presence
package MobilePresence
import Timer.SimulatedTimer

public class MobilePresence {
	private String id
	private String label
	private String displayName
	private String deviceNetworkId

	MobilePresence(String id, String label, String displayName, String deviceNetworkId) {
		this.id = id
		this.label = label
		this.displayName = displayName
		this.deviceNetworkId = deviceNetworkId
	}
}
