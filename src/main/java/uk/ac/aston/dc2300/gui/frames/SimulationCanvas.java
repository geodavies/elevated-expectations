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

    private static final int WIDTH = 975;
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
        paintKey(g);

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
                drawFloorSkeleton(currentFloor, g, numFloors);
                // Populate with people
                populateFloor(currentFloor, g, numFloors);
            }

            // Draw elevators
            Set<Elevator> elevators = building.getElevators();
            drawElevators(elevators, g, numFloors);

        }
        // Reset color
        g.setColor(Color.BLACK);

    }

    private void paintKey(Graphics context) {
        int KEY_LEFT = 775;
        int KEY_WIDTH = 150;
        int KEY_TOP = BORDER_Y + BORDER;
        int KEY_HEIGHT = KEY_WIDTH * 2;
        // Draw container
        context.drawRect(KEY_LEFT, KEY_TOP, KEY_WIDTH, KEY_HEIGHT);
        // Draw title
        context.drawString("Key: ", KEY_LEFT + (BORDER / 2), KEY_TOP + BORDER);

        // Draw client
        drawLabel("Client", Color.BLUE, context, KEY_LEFT + (BORDER / 2), KEY_TOP + BORDER * 2);
        drawLabel("Maintenance Crew", Color.YELLOW, context, KEY_LEFT + (BORDER / 2), KEY_TOP + BORDER * 3);
        drawLabel("Mugtome Dev", Color.MAGENTA, context, KEY_LEFT + (BORDER / 2), KEY_TOP + BORDER * 4);
        drawLabel("Goggles Dev", Color.GREEN, context, KEY_LEFT + (BORDER / 2), KEY_TOP + BORDER * 5);
        drawLabel("Employee", Color.BLACK, context, KEY_LEFT + (BORDER / 2), KEY_TOP + BORDER * 6);

    }

    private void drawLabel(String label, Color color, Graphics context, int x, int y) {
        context.setColor(color);
        context.fillOval(x, y, PERSON_RADIUS, PERSON_RADIUS);
        context.setColor(Color.BLACK);
        context.drawString(label, x + (PERSON_RADIUS * 2), y + PERSON_RADIUS);

    }

    private void drawFloorSkeleton(Floor currentFloor, Graphics g, int numFloors) {
        int floorPosition = numFloors - 1 - currentFloor.getFloorNumber();
        // Setup basic floor layout
        g.drawRect(BORDER, BORDER_Y + BORDER + (SECTION_HEIGHT * floorPosition), SECTION_WIDTH, SECTION_HEIGHT);
        g.drawRect(BORDER + SECTION_WIDTH, BORDER_Y + BORDER + (SECTION_HEIGHT * floorPosition), SECTION_WIDTH, SECTION_HEIGHT);
        g.drawRect(BORDER + (2 * SECTION_WIDTH), BORDER_Y + BORDER + (SECTION_HEIGHT * floorPosition), SECTION_WIDTH, SECTION_HEIGHT);
        g.drawString(currentFloor.getFloorNumber() + "", 0, BORDER_Y + BORDER + (SECTION_HEIGHT * floorPosition) + 10);
    }

    private void drawTitles(Graphics g) {
        g.drawString("Lift", BORDER, BORDER_Y);
        g.drawString("Queue", BORDER + SECTION_WIDTH, BORDER_Y);
        g.drawString("On-Floor", BORDER + (SECTION_WIDTH * 2), BORDER_Y);
    }

    private void drawElevators(Set<Elevator> elevators, Graphics g, int numFloors) {

        int elevatorCount = elevators.size();
        int elevatorId = 0;

        int sectionWidth = (SECTION_WIDTH - 10) / elevatorCount;
        int elevatorHeight = SECTION_HEIGHT - 10;

        for (Elevator elevator: elevators) {

            Set<BuildingOccupant> passengers = new HashSet<>();
            passengers.addAll(elevator.getPassengers());
            int floorPosition = numFloors - elevator.getCurrentFloor().getFloorNumber() - 1;

            int elevatorX = BORDER + (elevatorId * sectionWidth) + 5;
            int elevatorY = BORDER_Y + BORDER + 5 + (SECTION_HEIGHT * floorPosition);

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


    /**
     * Method populates a given floor on the UI both with the occupants
     * in the elevator queue and generally on the given floor.
     *
     * @param currentFloor The floor to populate
     * @param g the graphics context to use to draw the occupants
     */
    private void populateFloor(Floor currentFloor, Graphics g, int floorCount) {

        int floorPos = floorCount - currentFloor.getFloorNumber() - 1;

        // Get the elevator queue
        LinkedList<BuildingOccupant> queue =  new LinkedList<>();
        queue.addAll(currentFloor.getElevatorQueue());

        int queueLength = queue.size();

        // Draw each person in the queue
        for (int position = 0; position < queueLength; position++) {
            g.setColor(getColorForOccupant(queue.get(position)));
            g.fillOval( (BORDER * 2) + SECTION_WIDTH + (position * PERSON_RADIUS) + 2, BORDER_Y + (BORDER * 2) + (SECTION_HEIGHT * floorPos), PERSON_RADIUS, PERSON_RADIUS);
        }

        Set<BuildingOccupant> floorOccupants = new HashSet<>();
        // Get the floor occupants
        floorOccupants.addAll(currentFloor.getOccupants());

        // Draw each person on the floor
        int position = 0;
        for(BuildingOccupant occupant : floorOccupants) {
            position++;
            g.setColor(getColorForOccupant(occupant));
            g.fillOval( (BORDER * 2) + (2 * SECTION_WIDTH) + (position * PERSON_RADIUS) + 2, BORDER_Y + (BORDER * 2) + (SECTION_HEIGHT * floorPos), PERSON_RADIUS, PERSON_RADIUS);
        }

        g.setColor ( Color.BLACK );

    }

    /**
     * Method returns an appropriate fill color for the BuildingOccupant provided.
     *
     * @param buildingOccupant The occupant to find a color for
     *
     * @return The fill color for the building occupant provided
     */
    private Color getColorForOccupant(BuildingOccupant buildingOccupant) {
        if (buildingOccupant instanceof Client) {
            return Color.BLUE;
        } else if (buildingOccupant instanceof MaintenanceCrew) {
            return Color.YELLOW;
        } else if (buildingOccupant instanceof Developer) {
            Developer developer = (Developer) buildingOccupant;
            if (developer.getCompany() == DeveloperCompany.MUGTOME) {
                return Color.MAGENTA;
            } else {
                return Color.GREEN;
            }
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
