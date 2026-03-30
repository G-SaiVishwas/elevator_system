package elevator.request;

public class FloorRequest {
    private final int floorNumber;
    private final Direction direction;

    public FloorRequest(int floorNumber, Direction direction) {
        if (floorNumber < 1) {
            throw new IllegalArgumentException("floorNumber must be >= 1");
        }
        if (direction == null || direction == Direction.IDLE) {
            throw new IllegalArgumentException("direction must be UP or DOWN");
        }
        this.floorNumber = floorNumber;
        this.direction = direction;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "FloorRequest{floor=" + floorNumber + ", direction=" + direction + '}';
    }
}
