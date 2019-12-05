package v3.Pathfinding;

public class StopWatch {
	private long startTime;
	private long stopTime;

	/**
	 * starting the stop watch.
	 */
	public void start() {
		startTime = System.nanoTime();
	}

	/**
	 * stopping the stop watch.
	 */
	public void stop() {
		stopTime = System.nanoTime();
	}

	/**
	 * elapsed time in nanoseconds.
	 */
	public long time() {
		return (stopTime - startTime);
	}

	public String getTime() {
		if ((double) time() / 1000000 <= 1000) {
			return (double) time() / 1000000 + " ms";
		} else {
			return (double) time() / 1000000000 + " s";
		}
	}

	public double getTimeValue() {
		if ((double) time() / 1000000 <= 1000) {
			return ((double) time() / 1000000);
		} else {
			return ((double) time() / 1000000000);
		}
	}
}