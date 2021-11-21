package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.sub_class;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.TaskIDDistancePair;

public class TaskTargetNonPrivacyInfo extends TaskIDDistancePair {
    protected Double target;
    protected Double newUtilityValue;

    public TaskTargetNonPrivacyInfo() {
    }

    public Double getTarget() {
        return target;
    }


    public void setTarget(Double target) {
        this.target = target;
    }


    public Double getNewUtilityValue() {
        return newUtilityValue;
    }

    public void setNewUtilityValue(Double newUtilityValue) {
        this.newUtilityValue = newUtilityValue;
    }


    public TaskTargetNonPrivacyInfo(Integer taskID, Double distance, Double target, Double newUtilityValue) {
        super(taskID, distance);
        this.target = target;
        this.newUtilityValue = newUtilityValue;
    }



}
