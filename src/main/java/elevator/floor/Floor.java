package elevator.floor;

public class Floor {
    private final int floorNumber;
    private final OutsidePanel outsidePanel;

    public Floor(int floorNumber) {
        if (floorNumber < 1) {
            throw new IllegalArgumentException("floorNumber must be >= 1");
        }
        this.floorNumber = floorNumber;
        this.outsidePanel = new OutsidePanel(floorNumber);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public OutsidePanel getOutsidePanel() {
        return outsidePanel;
    }
}
