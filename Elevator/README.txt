In this archive is an update for the Elevator assignment

1.
Replace ElevatorGraphics and TestSuite with the new classes, entirely.

2.
Add this code to you ElevatorScene class:

/**
 * COPY THIS INTO ElevatorScene
 * Start copy here
 */
	ArrayList<Integer> exitedCount = null;
	public static Semaphore exitedCountMutex;

	public void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}
/**
 * End copy here
 */

3.
Add this code at the end of the restartScene() function in ElevatorScene

/**
 * COPY THIS INTO restartScene
 * Start copy here
 */
	if(exitedCount == null) {
		exitedCount = new ArrayList<Integer>();
	}
	else {
		exitedCount.clear();
	}
	for(int i = 0; i < getNumberOfFloors(); i++) {
		this.exitedCount.add(0);
	}
	exitedCountMutex = new Semaphore(1);
/**
 * End copy here
 */

4.
Finally make sure that every person thread calls personExitsAtFloor(floor) with the correct value of floor when it gets out of the elevator, before it finishes.



TestSuite:

You can now call runTest(i) for i between 0 and 9.

The new test case 5 is more difficult than it was, and should truly cause a starvation onthe middle floor, until the other floors are more or less empty.  The old test case 5 is now test case 6.

Test case 9 is of the easiest level of complexity but tests both taking in full elevators of 6 and also elevators with les than 6, so it's a richer test case than test case 0.


ElevatorGraphics:

The visualization should now show people after they have exited the elevators.