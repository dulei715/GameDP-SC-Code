package edu.ecnu.dll.scheme.solution;

import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.basic_struct.agent.Worker;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.SingleInfoPack;
@Deprecated
public abstract class Solution {
    protected Task[] tasks;
    protected Worker[] workers;

    protected abstract SingleInfoPack chooseByUtilityFunction();
    protected abstract SingleInfoPack[] serverExecute();

    public abstract SingleInfoPack[] compete();

}
