package elevator.dispatch;

import elevator.elevator.Elevator;
import elevator.request.FloorRequest;

import java.util.List;
import java.util.Optional;

public interface DispatchAlgorithm {
    Optional<Elevator> selectElevator(FloorRequest request, List<Elevator> elevators);
}
