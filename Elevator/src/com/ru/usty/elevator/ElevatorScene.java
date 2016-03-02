package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {
	
	public static Semaphore personCountMutex;
	public static Semaphore elevatorCountMutex;
	public static Semaphore exitedCountMutex;
	
	public static Semaphore enterSemaphore;
	public static Semaphore exitSemaphore;
	
	public static ElevatorScene scene;
	public static boolean elevatorsMayDie;
	
	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 1500;  //milliseconds

	private int numberOfFloors = 2;
	private int numberOfElevators = 1;
	public int elevatorFloor = 0;
	
	ArrayList<Integer> personCount = null; 
	ArrayList<Integer> elevatorCount = null; //maybe
	ArrayList<Integer> exitedCount = null;

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		
		/**
		 * Important to add code here to make new
		 * threads that run your elevator-runnables
		 * 
		 * Also add any other code that initializes
		 * your system for a new run
		 * 
		 * If you can, tell any currently running
		 * elevator threads to stop
		 */
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		
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
		
		for(int i = 0; i < numberOfElevators; i++){
			Thread thread = new Thread(new Elevator(numberOfFloors,numberOfElevators));
			thread.start();
		}

		/*elevatorsMayDie = true;
		if(elevatorThread != null){
			if(elevatorThread.isAlive()){
				try {
					elevatorThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}*/
		elevatorsMayDie = false;
		
		scene = this;
		//semaphore1 = new Semaphore(0);
		enterSemaphore = new Semaphore(0);
		exitSemaphore = new Semaphore(0);
		
		personCountMutex = new Semaphore(1);
		elevatorCountMutex = new Semaphore(1);

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}
		
		elevatorCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfElevators; i++){
			this.elevatorCount.add(0);
		}
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		
		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */
		
		Thread thread = new Thread(new Person(sourceFloor,destinationFloor));
		thread.start();
		
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {
			return elevatorFloor;
	}

	public void personExitsAtFloor(int floor) {
		try {
			exitedCountMutex.acquire();
				exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();
		} catch (InterruptedException e) {
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

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {
		return personCount.get(floor);
	}

	public void decrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			personCountMutex.acquire();
				personCount.set(floor, (personCount.get(floor) - 1));
			personCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void incrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			personCountMutex.acquire();
				personCount.set(floor, (personCount.get(floor) + 1));
			personCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
			return elevatorCount.get(elevator);
	}
	
	public void decrementNumberOfPeopleInElevator(int elevator){
		try {
			elevatorCountMutex.acquire();
				elevatorCount.set(elevator, (elevatorCount.get(elevator) - 1));
			elevatorCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void incrementNumberOfPeoplInElevator(int elevator){
		try {
			elevatorCountMutex.acquire();
				elevatorCount.set(elevator, (elevatorCount.get(elevator) + 1));
			elevatorCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {
		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {
		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}
}
