package uk.ac.aston.dc2300.gui.frames;

import uk.ac.aston.dc2300.model.entity.*;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by dan on 28/05/2017.
 */
public class SimulationCanvas extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    private static final int BORDER = 15;
    private static final int BORDER_Y = 25;

    private static final int SECTION_HEIGHT = 75;
    private static final int SECTION_WIDTH = 250;

    private static final int PERSON_RADIUS = 10;

    private Building building;

    public SimulationCanvas () {
        super();
        setOpaque ( true );
        building = null;
    }

    @Override
    public Dimension getPreferredSize () {
        return new Dimension ( WIDTH, HEIGHT );
    }

    @Override
    protected void paintComponent ( Graphics g ) {
        super.paintComponent ( g );
        drawTitles(g);

        // Check we have a building
        if (building != null) {

            // Get the list of floors
            java.util.List<Floor> floors = building.getFloors();
            int numFloors = floors.size();

            // Iterate through
            for (int f = 0; f < numFloors; f++) {
                // Get the current floor
                Floor currentFloor = floors.get(numFloors - 1 - f);
                // Draw the basic skeleton
                drawFloorSkeleton(currentFloor, g);
                // Populate with people
                populateFloor(currentFloor, g);
            }

            // Draw elevators
            Set<Elevator> elevators = building.getElevators();
            drawElevators(elevators, g);

        }
        // Reset color
        g.setColor(Color.BLACK);

    }

    private void drawFloorSkeleton(Floor currentFloor, Graphics g) {
        // Setup basic floor layout
        g.drawRect(BORDER, BORDER_Y + BORDER + (SECTION_HEIGHT * currentFloor.getFloorNumber()), SECTION_WIDTH, SECTION_HEIGHT);
        g.drawRect(BORDER + SECTION_WIDTH, BORDER_Y + BORDER + (SECTION_HEIGHT * currentFloor.getFloorNumber()), SECTION_WIDTH, SECTION_HEIGHT);
        g.drawRect(BORDER + (2 * SECTION_WIDTH), BORDER_Y + BORDER + (SECTION_HEIGHT * currentFloor.getFloorNumber()), SECTION_WIDTH, SECTION_HEIGHT);
        g.drawString(currentFloor.getFloorNumber() + "", 0, BORDER_Y + BORDER + (SECTION_HEIGHT * currentFloor.getFloorNumber()) + 10);
    }

    private void drawTitles(Graphics g) {
        g.drawString("Lift", BORDER, BORDER_Y);
        g.drawString("Queue", BORDER + SECTION_WIDTH, BORDER_Y);
        g.drawString("On-Floor", BORDER + (SECTION_WIDTH * 2), BORDER_Y);
    }

    private void drawElevators(Set<Elevator> elevators, Graphics g) {

        int elevatorCount = elevators.size();
        int elevatorId = 0;

        int sectionWidth = (SECTION_WIDTH - 10) / elevatorCount;
        int elevatorHeight = SECTION_HEIGHT - 10;

        for (Elevator elevator: elevators) {

            Set<BuildingOccupant> passengers = elevator.getPassengers();
            int currentFloor = elevator.getCurrentFloor().getFloorNumber();

            int elevatorX = BORDER + (elevatorId * sectionWidth) + 5;
            int elevatorY = BORDER_Y + BORDER + 5 + (SECTION_HEIGHT * currentFloor);

            g.drawRect(elevatorX, elevatorY, sectionWidth, elevatorHeight);

            // Draw occupants

            int occupantCount = 0;
            for (BuildingOccupant passenger: passengers) {
                g.setColor(getColorForOccupant(passenger));
                g.fillOval(elevatorX + (occupantCount * PERSON_RADIUS) + 2, elevatorY + PERSON_RADIUS, PERSON_RADIUS, PERSON_RADIUS);
                occupantCount++;
            }

            elevatorId++;
        }

    }

    private void populateFloor(Floor currentFloor, Graphics g) {

        // Get the elevator queue
        LinkedList<BuildingOccupant> queue = currentFloor.getElevatorQueue();
        int queueLength = queue.size();

        // Draw each person in the queue
        for (int position = 0; position < queueLength; position++) {
            g.setColor(getColorForOccupant(queue.get(position)));
            g.fillOval( (BORDER * 2) + SECTION_WIDTH + (position * PERSON_RADIUS) + 2, BORDER_Y + (BORDER * 2) + (SECTION_HEIGHT * currentFloor.getFloorNumber()), PERSON_RADIUS, PERSON_RADIUS);
        }

        // Get the floor occupants
        Set<BuildingOccupant> floorOccupants = currentFloor.getOccupants();

        // Draw each person on the floor
        int position = 0;
        for(BuildingOccupant occupant : floorOccupants) {
            position++;
            g.setColor(getColorForOccupant(occupant));
            g.fillOval( (BORDER * 2) + (2 * SECTION_WIDTH) + (position * PERSON_RADIUS) + 2, BORDER_Y + (BORDER * 2) + (SECTION_HEIGHT * currentFloor.getFloorNumber()), PERSON_RADIUS, PERSON_RADIUS);
        }

        g.setColor ( Color.BLACK );

    }

    private Color getColorForOccupant(BuildingOccupant buildingOccupant) {
        if (buildingOccupant instanceof Client) {
            return Color.BLUE;
        } else if (buildingOccupant instanceof MaintenanceCrew) {
            return Color.YELLOW;
        } else if (buildingOccupant instanceof Developer) {
            try {
                if (((Developer) buildingOccupant).getCompany() == DeveloperCompany.MUGTOME) {
                    return Color.MAGENTA;
                }
            } catch (Exception error) {

            }
            return Color.GREEN;
        }
        return Color.BLACK;
    }

    public void update(Building building) {
        this.building = building;
        invalidate();
        revalidate();
        repaint();
    }
}
