import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Ethan Petuchowski 12/13/14
 */
public class Main {

	static Set<String> strings = new LinkedHashSet<String>();


    public static void main(String[] args) throws InterruptedException {
	    int NUM = 26;
	    Thread[] threads = new Thread[NUM];
	    for (int i = 0; i < NUM; i++) {
		    String letter = String.format("%c", 'a'+i);
		    Runnable runnable = new HelloRunnable(letter);
		    threads[i] = new Thread(runnable);
		    threads[i].start();
	    }
	    for (Thread t : threads) t.join();
	    System.out.println(strings);
    }
	public static void addString(String s) {
		synchronized (strings) {
			strings.add(s);
		}
	}
}

class HelloRunnable implements Runnable {
    String myString;
    public HelloRunnable(String s) {
	    myString = s;
    }
    /* "The general contract of the method `run` is that
     *  it may take any action whatsoever."
     */
    public void run() {
	    try {
		    /* ThreadLocalRandom is a convenience class so that each
		       thread doesn't have to wait on a global random generator */
		    int sleepTime = ThreadLocalRandom.current().nextInt(100);
		    Thread.sleep(sleepTime);
	    }
	    catch (InterruptedException e) {}
	    System.out.println(myString+" adding myself!");
	    Main.addString(myString);
    }
}
