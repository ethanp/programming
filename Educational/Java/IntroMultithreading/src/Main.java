import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Ethan Petuchowski 12/13/14
 */
public class Main {
	static int NUM = 26;
	static String NULL_STRING = "";

	/* make the capacity of the queue too small, so the producers
	   and consumer must actually work together */
	static ArrayBlockingQueue<String> strings = new ArrayBlockingQueue<String>(2);

    public static void main(String[] args) throws InterruptedException {
	    ExecutorService threadPool = Executors.newFixedThreadPool(3);
	    new Thread(new Taker()).start();
	    for (int i = 0; i < NUM; i++) {
		    String letter = String.format("%c", 'a'+i);
		    // submit returns a Future that lets you get the return value,
		    // but I have no return value
		    threadPool.submit(new Putter(letter));
	    }
	    threadPool.awaitTermination(3, TimeUnit.SECONDS);
	    threadPool.shutdown();
	    // just for kicks stick a terminator into the queue
	    // to signal that it's over between us
	    new Thread(new Putter(NULL_STRING)).start();
    }
}

class Taker implements Runnable {
	public void run() {
		int i = 0;
		while (true) {
			String s = null;
			int sleepTime = ThreadLocalRandom.current().nextInt(150);
			try {
				Thread.sleep(sleepTime);
				s = Main.strings.take();
			}
			catch (InterruptedException e) { e.printStackTrace(); }
			assert s != null;

			if (s.equals(Main.NULL_STRING)) return;

			// %n means "platform-independent newline char[s]"
			else System.out.printf("%d: %s%n", ++i, s);
		}
	}
}

class Putter implements Runnable {
    String myString;
    public Putter(String s) {
	    myString = s;
    }
    /* "The general contract of the method `run` is that
     *  it may take any action whatsoever" (that sounds good!) */
    public void run() {

	    /* ThreadLocalRandom is a convenience class so that each
	       thread doesn't have to wait on a global random generator */
	    int sleepTime = ThreadLocalRandom.current().nextInt(200);

	    try {
		    Thread.sleep(sleepTime);
		    System.out.println(myString+" adding");
		    Main.strings.put(myString);
	        System.out.println(myString+" added!");
	    }
	    catch (InterruptedException e) {}
    }
}
