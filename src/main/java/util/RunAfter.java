package util;

import java.util.Timer;
import java.util.TimerTask;


public class RunAfter<T>  {
	
	    Timer timer;
	    private T value;
		private int seconds;

	    public RunAfter(int seconds) {
	       this.seconds = seconds;
		}

	    public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
				if(timer == null){
			    	timer = new Timer();
			        timer.schedule(new RemindTask(), this.seconds*1000);
		        }
		}

		class RemindTask extends TimerTask {
	        public void run() {
	            System.out.println("Time's up!: "+RunAfter.this.getValue());
	            timer.cancel(); //Terminate the timer thread
	            timer = null;
	        }
	    }
	    
	    
	    
}