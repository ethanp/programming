import java.util.Set;
import java.util.TreeSet;

/**
 * Ethan Petuchowski 12/13/14
 */
public class Main {

	static Set<String> strings = new TreeSet<String>();

    static class HelloRunnable implements Runnable {

	    String myString;
	    public HelloRunnable(String s) {
		    myString = s;
	    }

	    /* "The general contract of the method `run` is that
         *  it may take any action whatsoever."
         */
	    public void run() {
	        System.out.println("Hello from thread!");
		    Main.addString(myString);
	    }
	}
    public static void main(String[] args) throws InterruptedException {
	    for (int i = 0; i < 26; i++) {
		    String letter = String.format("%c", 'a'+i);
		    Runnable runnable = new HelloRunnable(letter);
		    Thread thread = new Thread(runnable);
		    thread.start();
	    }
	    Thread.sleep(20);
	    System.out.println(strings);
    }
	public static void addString(String s) {
		synchronized (strings) {
			strings.add(s);
		}
	}
}
