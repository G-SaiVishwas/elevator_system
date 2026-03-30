package elevator;

import elevator.building.Building;
import elevator.dispatch.FCFSDispatchAlgorithm;
import elevator.dispatch.ShortestSeekTimeAlgorithm;
import elevator.elevator.Elevator;
import elevator.request.Direction;

public class Main {
    public static void main(String[] args) {
        Building building = new Building(10, 3, new ShortestSeekTimeAlgorithm());

        System.out.println("Initial state");
        building.printElevatorStates();

        System.out.println("\nOutside requests: floors 1, 5, 8 going UP");
        building.pressOutsideFloorButton(1, Direction.UP);
        building.pressOutsideFloorButton(5, Direction.UP);
        building.pressOutsideFloorButton(8, Direction.UP);

        System.out.println("\nInside destination requests");
        building.pressInsideElevatorButton(1, 7);
        building.pressInsideElevatorButton(2, 9);
        building.pressInsideElevatorButton(3, 6);

        runTicks(building, 4, "ShortestSeekTime run");

        System.out.println("\nTriggering alarm from elevator 1");
        building.triggerAlarm(1);
        runTicks(building, 2, "Post-alarm run");

        System.out.println("\nPutting elevator 2 in maintenance");
        building.putElevatorInMaintenance(2);
        building.pressOutsideFloorButton(6, Direction.DOWN);
        building.pressOutsideFloorButton(3, Direction.UP);
        runTicks(building, 4, "Maintenance dispatch run");

        System.out.println("\nOverweight scenario on elevator 1");
        Elevator elevator1 = building.getElevatorById(1);
        elevator1.setCurrentWeight(900.0);
        building.pressInsideElevatorButton(1, 10);
        runTicks(building, 2, "Overweight run");

        System.out.println("\nResetting elevator 1 weight and restoring elevator 2");
        elevator1.setCurrentWeight(300.0);
        building.restoreElevatorFromMaintenance(2);
        runTicks(building, 2, "Recovery run");

        System.out.println("\nSwitching to FCFS dispatch algorithm");
        building.setDispatchAlgorithm(new FCFSDispatchAlgorithm());
        building.pressOutsideFloorButton(2, Direction.UP);
        building.pressOutsideFloorButton(4, Direction.UP);
        runTicks(building, 4, "FCFS run");

        System.out.println("\nAdding a new floor and requesting service");
        int newFloor = building.addFloor();
        System.out.println("Added floor: " + newFloor);
        building.pressOutsideFloorButton(newFloor, Direction.DOWN);
        runTicks(building, 5, "New floor run");
    }

    private static void runTicks(Building building, int ticks, String label) {
        System.out.println("\n" + label);
        for (int i = 1; i <= ticks; i++) {
            System.out.println("Tick " + i);
            building.tick();
            building.printElevatorStates();
        }
    }
}
