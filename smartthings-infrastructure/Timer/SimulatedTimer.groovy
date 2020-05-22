//Create a class for timer
package Timer

public class SimulatedTimer {
	private Thread thread;

	SimulatedTimer() {
		thread = null;
	}

	//By Apps
	def runAfter(int delay, Closure closure) {
		/*thread = new Thread() {
	
			@Override
			public void run() {
				Thread.sleep(delay)
				closure()
			}
		}.start()
		return thread*/
		closure()
	}

	def runAfter(long delay, Closure closure) {
		/*thread = new Thread() {
	
			@Override
			public void run() {
				Thread.sleep(delay)
				closure()
			}
		}.start()
		return thread*/
		closure()
	}

	def cancel() {
		//if (thread != null)
		//	thread.stop()
	}
}
