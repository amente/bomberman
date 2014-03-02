/* AUTHOR: Andrew Belu
	Launches a test with input number of threads
	For example usage, see: Tests/DoubleBufferTests.java
*/

package bomberman.test;

public class ThreadedTestRunner {
	public static boolean RunTest(Runnable test, int numThreads) {
		Thread[] threads = new Thread[numThreads];
		
		for (int i = 0; i < numThreads; i++){
			Thread t = new Thread(test);
			threads[i] = t;
			t.start();
		}
		
		for (int i = 0; i < numThreads; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				return false;
			}
		}
		
		return true;
	}
}
