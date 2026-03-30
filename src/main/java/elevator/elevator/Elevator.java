package elevator.elevator;

import elevator.request.Direction;

import java.util.TreeSet;

public class Elevator {
    private final int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;
    private final double weightLimit;
    private double currentWeight;
    private final TreeSet<Integer> destinationFloors;
    private ElevatorPanel elevatorPanel;

    public Elevator(int id, int startingFloor) {
        this(id, startingFloor, 700.0);
    }

    public Elevator(int id, int startingFloor, double weightLimit) {
        if (id < 1) {
            throw new IllegalArgumentException("id must be >= 1");
        }
        if (startingFloor < 1) {
            throw new IllegalArgumentException("startingFloor must be >= 1");
        }
        if (weightLimit <= 0) {
            throw new IllegalArgumentException("weightLimit must be > 0");
        }
        this.id = id;
        this.currentFloor = startingFloor;
        this.state = ElevatorState.IDLE;
        this.direction = Direction.IDLE;
        this.weightLimit = weightLimit;
        this.currentWeight = 0.0;
        this.destinationFloors = new TreeSet<>();
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorState getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getWeightLimit() {
        return weightLimit;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        if (currentWeight < 0) {
            throw new IllegalArgumentException("currentWeight must be >= 0");
        }
        this.currentWeight = currentWeight;
    }

    public boolean isOverweight() {
        return currentWeight > weightLimit;
    }

    public boolean isInMaintenance() {
        return state == ElevatorState.MAINTENANCE;
    }

    public boolean canAcceptRequests() {
        return !isInMaintenance() && !isOverweight();
    }

    public TreeSet<Integer> getDestinationFloors() {
        return new TreeSet<>(destinationFloors);
    }

    public ElevatorPanel getElevatorPanel() {
        return elevatorPanel;
    }

    public void setElevatorPanel(ElevatorPanel elevatorPanel) {
        if (elevatorPanel == null) {
            throw new IllegalArgumentException("elevatorPanel cannot be null");
        }
        this.elevatorPanel = elevatorPanel;
    }

    public boolean addDestinationFloor(int floor) {
        if (floor < 1 || isInMaintenance()) {
            return false;
        }
        destinationFloors.add(floor);
        updateStateFromQueue();
        return true;
    }

    public void clearDestinationFloors() {
        destinationFloors.clear();
        updateStateFromQueue();
    }

    public void setMaintenanceMode(boolean maintenance) {
        if (maintenance) {
            state = ElevatorState.MAINTENANCE;
            direction = Direction.IDLE;
            destinationFloors.clear();
        } else if (state == ElevatorState.MAINTENANCE) {
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
        }
    }

    public void forceIdleAndClearDestinations() {
        state = ElevatorState.IDLE;
        direction = Direction.IDLE;
        destinationFloors.clear();
    }

    public void openDoor() {
        System.out.println("Elevator " + id + " opening door at floor " + currentFloor);
    }

    public void closeDoor() {
        System.out.println("Elevator " + id + " closing door at floor " + currentFloor);
    }

    public void step() {
        if (isInMaintenance()) {
            return;
        }
        if (destinationFloors.isEmpty()) {
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
            return;
        }

        if (destinationFloors.contains(currentFloor)) {
            destinationFloors.remove(currentFloor);
            openDoor();
            closeDoor();
            if (destinationFloors.isEmpty()) {
                updateStateFromQueue();
                return;
            }
        }

        if (isOverweight()) {
            System.out.printf("Elevator %d overweight (%.1f/%.1f kg). Refusing to move.%n", id, currentWeight, weightLimit);
            return;
        }

        int targetFloor = determineTargetFloor();

        if (currentFloor < targetFloor) {
            currentFloor++;
            state = ElevatorState.MOVING_UP;
            direction = Direction.UP;
        } else if (currentFloor > targetFloor) {
            currentFloor--;
            state = ElevatorState.MOVING_DOWN;
            direction = Direction.DOWN;
        }

        if (currentFloor == targetFloor) {
            destinationFloors.remove(targetFloor);
            openDoor();
            closeDoor();
        }

        updateStateFromQueue();
    }

    private int determineTargetFloor() {
        Integer lower = destinationFloors.lower(currentFloor);
        Integer higher = destinationFloors.higher(currentFloor);

        if (lower == null && higher == null && destinationFloors.contains(currentFloor)) {
            return currentFloor;
        }

        if (direction == Direction.UP) {
            if (higher != null) {
                return higher;
            }
            if (lower != null) {
                return lower;
            }
        } else if (direction == Direction.DOWN) {
            if (lower != null) {
                return lower;
            }
            if (higher != null) {
                return higher;
            }
        }

        if (lower == null) {
            return higher;
        }
        if (higher == null) {
            return lower;
        }

        int distanceToLower = currentFloor - lower;
        int distanceToHigher = higher - currentFloor;
        if (distanceToLower <= distanceToHigher) {
            return lower;
        }
        return higher;
    }

    private void updateStateFromQueue() {
        if (isInMaintenance()) {
            direction = Direction.IDLE;
            return;
        }
        if (destinationFloors.isEmpty()) {
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
            return;
        }

        int targetFloor = determineTargetFloor();
        if (targetFloor > currentFloor) {
            state = ElevatorState.MOVING_UP;
            direction = Direction.UP;
        } else if (targetFloor < currentFloor) {
            state = ElevatorState.MOVING_DOWN;
            direction = Direction.DOWN;
        } else if (destinationFloors.contains(currentFloor)) {
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
        } else {
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
        }
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "id=" + id +
                ", currentFloor=" + currentFloor +
                ", state=" + state +
                ", direction=" + direction +
                ", destinations=" + destinationFloors +
                '}';
    }
}
