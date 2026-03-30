package elevator.request;

public class ElevatorRequest {
    private final int targetFloor;
    private final int elevatorId;

    public ElevatorRequest(int targetFloor, int elevatorId) {
        if (targetFloor < 1) {
            throw new IllegalArgumentException("targetFloor must be >= 1");
        }
        if (elevatorId < 1) {
            throw new IllegalArgumentException("elevatorId must be >= 1");
        }
        this.targetFloor = targetFloor;
        this.elevatorId = elevatorId;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    @Override
    public String toString() {
        return "ElevatorRequest{targetFloor=" + targetFloor + ", elevatorId=" + elevatorId + '}';
    }
}
