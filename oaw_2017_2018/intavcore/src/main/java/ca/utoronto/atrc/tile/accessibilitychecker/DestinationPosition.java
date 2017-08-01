package ca.utoronto.atrc.tile.accessibilitychecker;

public class DestinationPosition {

    private String destination;
    private int position;

    public DestinationPosition(String destination, int position) {
        this.destination = destination;
        this.position = position;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        return destination != null ? destination.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DestinationPosition) {
            if (this.getDestination().equals(((DestinationPosition) obj).getDestination())) {
                return true;
            }
        }

        return false;
    }
}
