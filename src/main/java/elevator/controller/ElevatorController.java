package elevator.controller;

import elevator.dispatch.DispatchAlgorithm;
import elevator.elevator.Elevator;
import elevator.elevator.ElevatorState;
import elevator.request.ElevatorRequest;
import elevator.request.FloorRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class ElevatorController {
    private final List<Elevator> elevators;
    private DispatchAlgorithm dispatchAlgorithm;
    private final Queue<FloorRequest> pendingFloorRequests;

    public ElevatorController(List<Elevator> elevators, DispatchAlgorithm dispatchAlgorithm) {
        this.elevators = new ArrayList<>(elevators);
        this.dispatchAlgorithm = dispatchAlgorithm;
        this.pendingFloorRequests = new LinkedList<>();
    }

    public void setDispatchAlgorithm(DispatchAlgorithm dispatchAlgorithm) {
        this.dispatchAlgorithm = dispatchAlgorithm;
    }

    public void submitFloorRequest(FloorRequest request) {
        if (!assignElevator(request)) {
            pendingFloorRequests.offer(request);
        }
    }

    public void submitElevatorRequest(ElevatorRequest request) {
        Elevator elevator = findElevatorById(request.getElevatorId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown elevatorId: " + request.getElevatorId()));

        if (!elevator.addDestinationFloor(request.getTargetFloor())) {
            System.out.println("Elevator " + elevator.getId() + " cannot accept destination floor " + request.getTargetFloor());
        }
    }

    public void tick() {
        processPendingFloorRequests();
        for (Elevator elevator : elevators) {
            if (elevator.getState() != ElevatorState.IDLE && elevator.getState() != ElevatorState.MAINTENANCE) {
                elevator.step();
            }
        }
    }

    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }

    private void processPendingFloorRequests() {
        int requestsToProcess = pendingFloorRequests.size();
        for (int i = 0; i < requestsToProcess; i++) {
            FloorRequest request = pendingFloorRequests.poll();
            if (request == null) {
                continue;
            }
            if (!assignElevator(request)) {
                pendingFloorRequests.offer(request);
            }
        }
    }

    private boolean assignElevator(FloorRequest request) {
        Optional<Elevator> selected = dispatchAlgorithm.selectElevator(request, elevators);
        if (selected.isEmpty()) {
            return false;
        }
        Elevator elevator = selected.get();
        return elevator.addDestinationFloor(request.getFloorNumber());
    }

    private Optional<Elevator> findElevatorById(int elevatorId) {
        for (Elevator elevator : elevators) {
            if (elevator.getId() == elevatorId) {
                return Optional.of(elevator);
            }
        }
        return Optional.empty();
    }
}
