package elevator.floor;

import elevator.request.Direction;
import elevator.request.FloorRequest;

public class OutsidePanel {
    private final int floorNumber;

    public OutsidePanel(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public FloorRequest pressUpButton() {
        return new FloorRequest(floorNumber, Direction.UP);
    }

    public FloorRequest pressDownButton() {
        return new FloorRequest(floorNumber, Direction.DOWN);
    }
}
