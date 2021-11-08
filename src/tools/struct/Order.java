package tools.struct;

import java.util.Objects;

public class Order {
    private Long timestamp = null;
    private Integer beginPointID = null;
    private Integer endPointID = null;
    private Integer passengerSize = null;

    public Order() {
    }

    public Order(Long timestamp, Integer beginPointID, Integer endPointID, Integer passengerSize) {
        this.timestamp = timestamp;
        this.beginPointID = beginPointID;
        this.endPointID = endPointID;
        this.passengerSize = passengerSize;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getBeginPointID() {
        return beginPointID;
    }

    public void setBeginPointID(Integer beginPointID) {
        this.beginPointID = beginPointID;
    }

    public Integer getEndPointID() {
        return endPointID;
    }

    public void setEndPointID(Integer endPointID) {
        this.endPointID = endPointID;
    }

    public Integer getPassengerSize() {
        return passengerSize;
    }

    public void setPassengerSize(Integer passengerSize) {
        this.passengerSize = passengerSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(timestamp, order.timestamp) &&
                Objects.equals(beginPointID, order.beginPointID) &&
                Objects.equals(endPointID, order.endPointID) &&
                Objects.equals(passengerSize, order.passengerSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, beginPointID, endPointID, passengerSize);
    }

    @Override
    public String toString() {
        return "Order{" +
                "timestamp=" + timestamp +
                ", beginPointID=" + beginPointID +
                ", endPointID=" + endPointID +
                ", passengerSize=" + passengerSize +
                '}';
    }
}
