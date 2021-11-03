package edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.SingleInfoPack;

public class IDDistancePair extends SingleInfoPack implements Comparable<IDDistancePair> {
    private Integer agentID = null;
    private Double distance = null;

    public IDDistancePair() {
    }

    public IDDistancePair(Double distance, Integer agentID) {
        this.distance = distance;
        this.agentID = agentID;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getAgentID() {
        return agentID;
    }

    public void setAgentID(Integer agentID) {
        this.agentID = agentID;
    }

    @Override
    public int compareTo(IDDistancePair IDDistancePair) {
        if (this.distance < IDDistancePair.distance) {
            return -1;
        }
        if (this.distance > IDDistancePair.distance) {
            return 1;
        }
        return this.agentID - IDDistancePair.agentID;
    }
}
