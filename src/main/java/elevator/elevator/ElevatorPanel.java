package elevator.elevator;

import elevator.emergency.EmergencyService;
import elevator.request.ElevatorRequest;

import java.util.List;
import java.util.function.IntSupplier;

public class ElevatorPanel {
    private final int elevatorId;
    private final IntSupplier totalFloorsSupplier;

    public ElevatorPanel(int elevatorId, IntSupplier totalFloorsSupplier) {
        this.elevatorId = elevatorId;
        this.totalFloorsSupplier = totalFloorsSupplier;
    }

    public ElevatorRequest pressFloorButton(int targetFloor) {
        int totalFloors = totalFloorsSupplier.getAsInt();
        if (targetFloor < 1 || targetFloor > totalFloors) {
            throw new IllegalArgumentException("targetFloor must be in range [1, " + totalFloors + "]");
        }
        return new ElevatorRequest(targetFloor, elevatorId);
    }

    public void pressOpenDoorButton(Elevator elevator) {
        elevator.openDoor();
    }

    public void pressCloseDoorButton(Elevator elevator) {
        elevator.closeDoor();
    }

    public void pressAlarmButton(List<Elevator> elevators, EmergencyService emergencyService) {
        emergencyService.triggerAlarm(elevators);
    }
}
