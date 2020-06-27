package main;

public class Timer {
	
	private long t, ns;
	
	public Timer(double dt) {	
		ns = (long) Math.round(1e9 * dt);
		
		reset();
	}
	
	public void reset() {
		t = System.nanoTime();
	}
	
	public void sleep() {
		long _t;
		while ((_t = System.nanoTime()) < t + ns) {
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		t = _t;
	}

}
