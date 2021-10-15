package edu.ecnu.dll.basic_struct.pack;

import java.util.List;

public class Candidate {
    public DistanceIDPair[] toTaskDistance = null;
    public List[] budgetList = null;

    public Candidate() {
    }

    public Candidate(DistanceIDPair[] toTaskDistance, List[] budgetList) {
        this.toTaskDistance = toTaskDistance;
        this.budgetList = budgetList;
    }



}
