/**
 * Goal:  Maven-Download, Import, Compile, and Run a Java program that uses Google's "Guava" library
 * Date:  8/22/13
 * Bonus: I linked it to the source code too
 */
import com.google.common.base.Joiner;

public class TryGuava {
	public static void main(String[] args) {
		Joiner joiner = Joiner.on("; ").skipNulls();
		System.out.println(joiner.join("Someone", null, "SomeoneElse", "ThirdPerson"));
	}
}
