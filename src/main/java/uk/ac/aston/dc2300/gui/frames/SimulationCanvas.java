package uk.ac.aston.dc2300.gui.frames;

import uk.ac.aston.dc2300.model.entity.*;
import uk.ac.aston.dc2300.model.status.DeveloperCompany;
import uk.ac.aston.dc2300.model.status.SimulationStatistics;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by dan on 28/05/2017.
 */
public class SimulationCanvas extends JPanel {

    private static final int HEIGHT = 750;
    private static final int WIDTH = 975;

    private static final int BORDER = 15;
    private static final int BORDER_Y = 25;

    private static final int SECTION_HEIGHT = 100;
    private static final int SECTION_WIDTH = 250;

    private static final int PERSON_RADIUS = 10;

    private Building building;

    private SimulationStatistics statistics;

    public SimulationCanvas () {
        super();
        setOpaque ( true );
        building = null;
        statistics = null;
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
        paintStats(g);

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
            List<Elevator> elevators = building.getElevators();
            drawElevators(elevators, g, numFloors);

        }
        // Reset color
        g.setColor(Color.BLACK);

    }

    private void paintStats(Graphics context) {
        if (statistics != null) {
            int STATS_LEFT = 775;
            int STATS_WIDTH = 150;
            int STATS_TOP = BORDER_Y + ((BORDER + STATS_WIDTH) * 2);
            int STATS_HEIGHT = STATS_WIDTH;
            // Draw container
            context.drawRect(STATS_LEFT, STATS_TOP, STATS_WIDTH, STATS_HEIGHT);
            // Draw title
            context.drawString("Statistics: ", STATS_LEFT + (BORDER / 2), STATS_TOP + BORDER);

            // Draw Wait time
            context.drawString("Avg Wait Time: ", STATS_LEFT + (BORDER / 2), STATS_TOP + BORDER * 3);
            context.drawString(statistics.getAverageTime() + "s", STATS_LEFT + (BORDER), STATS_TOP + BORDER * 4);

            // Draw Number of Complaints
            context.drawString("# Of Complaints: ", STATS_LEFT + (BORDER / 2), STATS_TOP + BORDER * 6);
            context.drawString(statistics.getNumberOfComplaints() + "", STATS_LEFT + (BORDER), STATS_TOP + BORDER * 7);
        }
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

    private void drawElevators(List<Elevator> elevators, Graphics g, int numFloors) {

        int elevatorCount = elevators.size();
        int elevatorId = 0;

        int sectionWidth = (SECTION_WIDTH - 10) / elevatorCount;
        int elevatorHeight = SECTION_HEIGHT - 10;

        for (Elevator elevator: elevators) {

            List<BuildingOccupant> passengers = new ArrayList<>();
            passengers.addAll(elevator.getPassengers());
            int floorPosition = numFloors - elevator.getCurrentFloor().getFloorNumber() - 1;

            int elevatorX = BORDER + (elevatorId * sectionWidth) + 5;
            int elevatorY = BORDER_Y + BORDER + 5 + (SECTION_HEIGHT * floorPosition);

            g.drawRect(elevatorX, elevatorY, sectionWidth, elevatorHeight);

            // Draw occupants

            int occupantCount = 0;
            for (BuildingOccupant passenger: passengers) {
                g.setColor(getColorForOccupant(passenger));

                // Work out how many we can fit
                int horizontalCapacity = (SECTION_WIDTH / PERSON_RADIUS) - 4;
                // Set x-offset
                int offset = ((occupantCount % horizontalCapacity) * PERSON_RADIUS) + 2;
                // Y offset should begin at zero
                int offsetY = 0;
                if (occupantCount > 0) {
                    // Work out if we're over the capacity
                    // and how many times over
                    // use that to work out vertical offset
                    int offsetCount = (int) Math.floor(occupantCount / horizontalCapacity);
                    offsetY = offsetCount * PERSON_RADIUS;
                }
                g.fillOval(offset + elevatorX + 2, elevatorY + offsetY + PERSON_RADIUS, PERSON_RADIUS, PERSON_RADIUS);
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

            // Work out how many we can fit
            int horizontalCapacity = (SECTION_WIDTH / PERSON_RADIUS) - 1;
            // Set x-offset
            int offset = BORDER + ((position % horizontalCapacity) * PERSON_RADIUS) + 2;
            // Y offset should begin at zero
            int offsetY = 0;
            if (position > 0) {
                // Work out if we're over the capacity
                // and how many times over
                // use that to work out vertical offset
                int offsetCount = (int) Math.floor(position / horizontalCapacity);
                offsetY = offsetCount * PERSON_RADIUS;
            }
            g.fillOval( SECTION_WIDTH + offset + 2, (int) (offsetY + BORDER_Y + BORDER * 1.25 + (SECTION_HEIGHT * floorPos)), PERSON_RADIUS, PERSON_RADIUS);
        }

        List<BuildingOccupant> floorOccupants = new ArrayList<>();
        // Get the floor occupants
        for (BuildingOccupant occupant : currentFloor.getOccupants()) {
            if (!currentFloor.getElevatorQueue().contains(occupant)) floorOccupants.add(occupant);
        }

        // Draw each person on the floor
        int position = 0;
        for(BuildingOccupant occupant : floorOccupants) {
            g.setColor(getColorForOccupant(occupant));
            // Work out how many we can fit
            int horizontalCapacity = (SECTION_WIDTH / PERSON_RADIUS) - 1;
            // Set x-offset
            int offset = BORDER + ((position % horizontalCapacity) * PERSON_RADIUS) + 2;
            // Y offset should begin at zero
            int offsetY = 0;
            if (position > 0) {
                // Work out if we're over the capacity
                // and how many times over
                // use that to work out vertical offset
                int offsetCount = (int) Math.floor(position / horizontalCapacity);
                offsetY = offsetCount * PERSON_RADIUS;
            }
            g.fillOval( offset + (2 * SECTION_WIDTH), (int) (offsetY + BORDER_Y + BORDER * 1.25 + (SECTION_HEIGHT * floorPos)), PERSON_RADIUS, PERSON_RADIUS);
            position++;
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

    public void drawStats(SimulationStatistics stats) {
        this.statistics = stats;
        invalidate();
        revalidate();
        repaint();
    }
}
