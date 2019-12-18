package info.util;

/**
 *  Use this class to avoid too much write operations
 *  Waits for a certain time after triggerChange is used
 **/
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class AsyncTimeout<T> {

	Timer timer;
	private Function<T, Void> func;
	private long seconds;
	private T arg0;

	public AsyncTimeout(long seconds, Function<T, Void> function) {
		this.seconds = seconds;
		this.func = function;
	}

	public void fire(T arg0) {
		this.arg0 = arg0;
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new RemindTask(), this.seconds*1000);
		}
	}

	class RemindTask extends TimerTask {
		public void run() {
			AsyncTimeout.this.func.apply(arg0);
			System.out.println("Time's up!: ");
			timer.cancel();
			timer = null;
		}
	}
}