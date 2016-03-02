package com.ru.usty.elevator;

public class Elevator implements Runnable {
	
	int numberOfFloors, numberOfElevators;
	public Elevator(int floors, int elevators){
		this.numberOfFloors = floors;
		this.numberOfElevators = elevators;
	}
	
	@Override
	public void run() {
		while(!ElevatorScene.elevatorsMayDie){
			for(int i = 0; i < 6; i++){
				ElevatorScene.enterSemaphore.release();
			}
			goWAIT();
			int peopleInElevator = ElevatorScene.scene.getNumberOfPeopleInElevator(0);
			for(int i = 6; i > peopleInElevator; i--){
				try {
					ElevatorScene.enterSemaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			goUP();
			goWAIT();
			for(int i = 0; i < 6; i++){
				ElevatorScene.exitSemaphore.release();
			}
			goWAIT();
			peopleInElevator = ElevatorScene.scene.getNumberOfPeopleInElevator(0);
			for(int i = 0; i < peopleInElevator; i++){
				try {
					ElevatorScene.exitSemaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			goDOWN();
			goWAIT();
		}
	}
	public void goWAIT(){
		try {
			Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void goUP(){
		ElevatorScene.scene.elevatorFloor++;
	}
	public void goDOWN(){
		ElevatorScene.scene.elevatorFloor--;
	}
}
