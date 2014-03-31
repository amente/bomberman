package bomberman.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PerformanceTests {
	public static void main(String args[]) {
		String[] args2 = new String[3];
		for (int i = 0; i < 3; i++) {
			args2[i] = args[i];
		}
		
		int timeout = Integer.parseInt(args[3]);
		
		double currentTime = System.currentTimeMillis();
		TestDriver.RunTest(args2, true, timeout);
		double endTime = System.currentTimeMillis() - currentTime;
		
		List<Double> latencies = new ArrayList<Double>();
		double average_latency = 0;
		System.out.println("Test took " + endTime + " milliseconds.");
		for(Tuple<String, Double> t : TestDriver.latencies) {
			//System.out.println(t.x + " - took " + t.y + " milliseconds to perform." );
			latencies.add(t.y);
			average_latency += t.y;
		}
		
		if (latencies.size() > 0) {
			average_latency = average_latency / latencies.size();
			
			Collections.sort(latencies,new Comparator<Double>() {
				@Override
				public int compare(Double a, Double b) {
					if (a-b < 0) { return -1; }
					if (a-b == 0) { return 0; }
					else {return 1;}
				}
			});
			
			System.out.println("Minimum latency - " + latencies.get(0));
			System.out.println("Average latency - " + average_latency);
			System.out.println("Maximum latency - " + latencies.get(latencies.size() - 1));
		}
		
		
		
	}
}
