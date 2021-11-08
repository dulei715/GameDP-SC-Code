package tools.struct;

import java.util.Objects;

public class Taxi {
    private Integer pointID = null;
    private Integer capacity = null;

    public Taxi() {
    }

    public Taxi(Integer pointID, Integer capacity) {
        this.pointID = pointID;
        this.capacity = capacity;
    }

    public Integer getPointID() {
        return pointID;
    }

    public void setPointID(Integer pointID) {
        this.pointID = pointID;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Taxi taxi = (Taxi) o;
        return Objects.equals(pointID, taxi.pointID) &&
                Objects.equals(capacity, taxi.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointID, capacity);
    }

    @Override
    public String toString() {
        return "Taxi{" +
                "pointID=" + pointID +
                ", capacity=" + capacity +
                '}';
    }
}
