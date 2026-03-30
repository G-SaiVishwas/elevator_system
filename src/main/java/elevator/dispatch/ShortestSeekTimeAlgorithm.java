package elevator.dispatch;

import elevator.elevator.Elevator;
import elevator.request.FloorRequest;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ShortestSeekTimeAlgorithm implements DispatchAlgorithm {
    @Override
    public Optional<Elevator> selectElevator(FloorRequest request, List<Elevator> elevators) {
        return elevators.stream()
                .filter(elevator -> !elevator.isInMaintenance())
                .filter(elevator -> !elevator.isOverweight())
                .min(Comparator
                        .comparingInt((Elevator elevator) -> Math.abs(elevator.getCurrentFloor() - request.getFloorNumber()))
                        .thenComparingInt(Elevator::getId));
    }
}
