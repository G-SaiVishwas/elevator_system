package elevator.building;

import elevator.controller.ElevatorController;
import elevator.dispatch.DispatchAlgorithm;
import elevator.elevator.Elevator;
import elevator.elevator.ElevatorPanel;
import elevator.emergency.EmergencyService;
import elevator.floor.Floor;
import elevator.request.Direction;
import elevator.request.ElevatorRequest;
import elevator.request.FloorRequest;

import java.util.ArrayList;
import java.util.List;

public class Building {
    private final List<Floor> floors;
    private final List<Elevator> elevators;
    private final ElevatorController elevatorController;
    private final EmergencyService emergencyService;

    public Building(int totalFloors, int totalElevators, DispatchAlgorithm dispatchAlgorithm) {
        if (totalFloors < 1) {
            throw new IllegalArgumentException("totalFloors must be >= 1");
        }
        if (totalElevators < 1) {
            throw new IllegalArgumentException("totalElevators must be >= 1");
        }

        this.floors = new ArrayList<>();
        this.elevators = new ArrayList<>();

        for (int floorNumber = 1; floorNumber <= totalFloors; floorNumber++) {
            floors.add(new Floor(floorNumber));
        }

        for (int elevatorId = 1; elevatorId <= totalElevators; elevatorId++) {
            elevators.add(new Elevator(elevatorId, 1));
        }

        this.elevatorController = new ElevatorController(elevators, dispatchAlgorithm);
        this.emergencyService = new EmergencyService();

        for (Elevator elevator : elevators) {
            elevator.setElevatorPanel(new ElevatorPanel(elevator.getId(), this::getTotalFloors));
        }
    }

    public int getTotalFloors() {
        return floors.size();
    }

    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }

    public Elevator getElevatorById(int elevatorId) {
        for (Elevator elevator : elevators) {
            if (elevator.getId() == elevatorId) {
                return elevator;
            }
        }
        throw new IllegalArgumentException("Unknown elevatorId: " + elevatorId);
    }

    public void setDispatchAlgorithm(DispatchAlgorithm dispatchAlgorithm) {
        elevatorController.setDispatchAlgorithm(dispatchAlgorithm);
    }

    public void pressOutsideFloorButton(int floorNumber, Direction direction) {
        Floor floor = getFloorByNumber(floorNumber);
        FloorRequest request;
        if (direction == Direction.UP) {
            request = floor.getOutsidePanel().pressUpButton();
        } else if (direction == Direction.DOWN) {
            request = floor.getOutsidePanel().pressDownButton();
        } else {
            throw new IllegalArgumentException("Outside floor button only accepts UP or DOWN");
        }
        elevatorController.submitFloorRequest(request);
    }

    public void pressInsideElevatorButton(int elevatorId, int targetFloor) {
        Elevator elevator = getElevatorById(elevatorId);
        ElevatorRequest request = elevator.getElevatorPanel().pressFloorButton(targetFloor);
        elevatorController.submitElevatorRequest(request);
    }

    public void triggerAlarm(int elevatorId) {
        Elevator elevator = getElevatorById(elevatorId);
        elevator.getElevatorPanel().pressAlarmButton(elevators, emergencyService);
    }

    public void putElevatorInMaintenance(int elevatorId) {
        getElevatorById(elevatorId).setMaintenanceMode(true);
    }

    public void restoreElevatorFromMaintenance(int elevatorId) {
        getElevatorById(elevatorId).setMaintenanceMode(false);
    }

    public int addFloor() {
        int newFloorNumber = floors.size() + 1;
        floors.add(new Floor(newFloorNumber));
        return newFloorNumber;
    }

    public void tick() {
        elevatorController.tick();
    }

    public void printElevatorStates() {
        for (Elevator elevator : elevators) {
            System.out.println(elevator);
        }
    }

    private Floor getFloorByNumber(int floorNumber) {
        if (floorNumber < 1 || floorNumber > floors.size()) {
            throw new IllegalArgumentException("Unknown floor: " + floorNumber);
        }
        return floors.get(floorNumber - 1);
    }
}
