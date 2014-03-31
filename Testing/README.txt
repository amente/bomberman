We have developed a number of tests to test the scalability of our system. 
Here are some brief descriptions of the tests, what they accomplish, and what a test run results in.

Each test is run through PerformanceTests.java, which computes the time of the test, the minimum latency,
the maximum latency and the average latency of the network calls. It will display them after the tests are 
finished. Choose a test from the package bomberman.test.Resources as the command line parameter to the test.

Test 1: Player spamming buttons
	We ran the performance test in three scenarios, when the player spams a key every 1ms, 10ms, 100ms and
	400ms. We send the same number of events, so ideally every one should get sent, and they should operate
	on the servers timeout (of ~200 ms for these set of tests). 
	
	Therefore, the 1ms, 10ms and 100ms should all take the same amount of time, with the 400ms taking more.
	
	Results:
		1ms interval spamming took 5476 milliseconds.
		10ms interval spamming took 5520 milliseconds.
		100ms interval spamming took 5615 milliseconds.
		400ms interval spamming took 5927 milliseconds.
		
	As we can see, spamming does have a slight effect on the overall responsiveness of the server,
	but not by much. In the long run, it will not affect the game too badly.
	
Test 2: Deploying players on the same machine vs. different machines
	We ran the performance test in two scenarios, when the player is running the server and the test on
	the same machine, and when the player is running the server and the test on different machines. Ideally,
	we want the local machine and the different machine to have the same amount of latency and 
	responsiveness from the server.
	
	Results:
		From same machine: 
			Minimum latency: 0.0ms
			Average latency: 4.86ms
			Maximum latency: 10.0ms
			
		From different machine:
			Minimum latency: 
			Average latency:
			Maximum latency:
			
Test 3: High load vs low load
	We ran the performance test in two scenarios, when there is only one player contributing actions,
	and when there are four players contributing actions. In this case, we expect that the server
	will be able to send GUI updates on the same frequency, but the actions themselves might be a
	bit laggier. 
	
	The test is performed by taking a single player test, and just adding dummy actions for the other 
	three players in the multi-player test. No actions that take longer expected time are used in 
	either test.
	
	Results:
		From one player test:
			Test took 5211.0 milliseconds.
			Minimum latency - 0.0ms
			Average latency - 5.142857142857143ms
			Maximum latency - 10.0ms
			
		From multi-player test:
			Test took 5913.0 milliseconds.
			Minimum latency - 0.0ms
			Average latency - 6.0754716981132075ms
			Maximum latency - 16.0ms
				
	As we can see, the server spent a longer time on the multi-player test than the one player test,
	but the GUI update for the players still updates quickly. This implies that the GUI's are all
	updated responsively, but the actual game does slow down a bit.

Test 4: Packet loss
	We ran a test with an equal number of up moves and down moves. If there is no packet loss, 
	then every time this test is run then the player should end up at the same position that 
	they started at (as the number of up moves and down moves are equal, and they never hit 
	a wall/box). 
	
	Results:
		After two runs, it is notable that the character does not end the test at the same
		point that it started at (or at the same point as the other test). 
		
	Therefore, we experience packet loss when a player sends commands. However, a player
	will often spam their movement keys/bomb keys more than one time trying to move/place a bomb,
	so packet loss at this point does not actually matter, as from the player's view it should
	not be visible (the way most players play games).