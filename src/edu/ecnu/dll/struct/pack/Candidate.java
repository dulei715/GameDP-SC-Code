package edu.ecnu.dll.struct.pack;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Candidate {
    public Integer workerID = null;
    public Double[] toTaskDistanceMap = null;
    public List[] budgetList = null;

    public Candidate() {
    }

    public Candidate(Integer workerID, Double[] toTaskDistanceMap, List[] budgetList) {
        this.workerID = workerID;
        this.toTaskDistanceMap = toTaskDistanceMap;
        this.budgetList = budgetList;
    }

    public Integer getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Integer workerID) {
        this.workerID = workerID;
    }

    public Double[] getToTaskDistanceMap() {
        return toTaskDistanceMap;
    }

    public void setToTaskDistanceMap(Double[] toTaskDistanceMap) {
        this.toTaskDistanceMap = toTaskDistanceMap;
    }

    public List[] getBudgetList() {
        return budgetList;
    }

    public void setBudgetList(List[] budgetList) {
        this.budgetList = budgetList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return workerID.equals(candidate.workerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerID);
    }
}
