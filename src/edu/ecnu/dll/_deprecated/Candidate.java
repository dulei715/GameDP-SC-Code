package edu.ecnu.dll._deprecated;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.IDDistancePair;

import java.util.List;

@Deprecated
public class Candidate {
    public IDDistancePair[] toTaskDistance = null;
    public List[] budgetList = null;

    public Candidate() {
    }

    public Candidate(IDDistancePair[] toTaskDistance, List[] budgetList) {
        this.toTaskDistance = toTaskDistance;
        this.budgetList = budgetList;
    }



}
