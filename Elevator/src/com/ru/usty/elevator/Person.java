package com.ru.usty.elevator;

public class Person implements Runnable{
	
	int sourrceFloor, destinationFloor;
	public Person(int src, int dst){
		this.sourrceFloor = src;
		this.destinationFloor = dst;
	}

	@Override
	public void run() {
		ElevatorScene.scene.incrementNumberOfPeopleWaitingAtFloor(sourrceFloor);
		
		try {
			ElevatorScene.enterSemaphore.acquire();//wait
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		ElevatorScene.scene.incrementNumberOfPeoplInElevator(0);
		ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(sourrceFloor);
		
		try {
			ElevatorScene.exitSemaphore.acquire(); //wait
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ElevatorScene.scene.personExitsAtFloor(destinationFloor);
		ElevatorScene.scene.decrementNumberOfPeopleInElevator(0);
		
		System.out.println("Person Thread Released");
	}
}
