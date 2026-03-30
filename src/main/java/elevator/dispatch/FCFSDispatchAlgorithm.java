package elevator.dispatch;

import elevator.elevator.Elevator;
import elevator.elevator.ElevatorState;
import elevator.request.Direction;
import elevator.request.FloorRequest;

import java.util.List;
import java.util.Optional;

public class FCFSDispatchAlgorithm implements DispatchAlgorithm {
    @Override
    public Optional<Elevator> selectElevator(FloorRequest request, List<Elevator> elevators) {
        for (Elevator elevator : elevators) {
            if (!isEligible(elevator)) {
                continue;
            }
            if (elevator.getState() == ElevatorState.IDLE || isSameDirectionAndReachable(elevator, request)) {
                return Optional.of(elevator);
            }
        }
        return Optional.empty();
    }

    private boolean isEligible(Elevator elevator) {
        return !elevator.isInMaintenance() && !elevator.isOverweight();
    }

    private boolean isSameDirectionAndReachable(Elevator elevator, FloorRequest request) {
        Direction direction = request.getDirection();
        if (direction == Direction.UP) {
            return elevator.getDirection() == Direction.UP && elevator.getCurrentFloor() <= request.getFloorNumber();
        }
        if (direction == Direction.DOWN) {
            return elevator.getDirection() == Direction.DOWN && elevator.getCurrentFloor() >= request.getFloorNumber();
        }
        return false;
    }
}
