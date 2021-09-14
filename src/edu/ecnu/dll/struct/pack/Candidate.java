package edu.ecnu.dll.struct.pack;

import java.util.List;
import java.util.Objects;

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
