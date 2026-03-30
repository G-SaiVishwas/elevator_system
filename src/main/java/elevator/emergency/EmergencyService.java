package elevator.emergency;

import elevator.elevator.Elevator;

import java.util.List;

public class EmergencyService {
    public void triggerAlarm(List<Elevator> elevators) {
        System.out.println("ALARM TRIGGERED: stopping all non-maintenance elevators immediately.");
        for (Elevator elevator : elevators) {
            if (!elevator.isInMaintenance()) {
                elevator.forceIdleAndClearDestinations();
            }
        }
    }
}
