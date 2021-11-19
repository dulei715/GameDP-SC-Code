package edu.ecnu.dll.scheme.run.common;

import edu.ecnu.dll.basic_struct.agent.Worker;
import edu.ecnu.dll.basic_struct.comparator.TaskWorkerIDComparator;
import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;
import edu.ecnu.dll.basic_struct.pack.UtilityDistanceIDPair;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import tools.io.print.MyPrint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonFunction {

    // for privacy
    public static BasicExperimentResult getResultData(WorkerIDNoiseDistanceBudgetPair[] winnerTaskWorkerPackedArray, Worker[] workers) {
        TaskWorkerIDPair[] taskWorkerIDPairArray;
        List<TaskWorkerIDPair> taskWorkerIDPairList = new ArrayList<>();

        List<Integer> nonCompeteTaskIDList = new ArrayList<>();
        List<Integer> competeFailureTaskIDList = new ArrayList<>();

        Integer totalTaskSize = winnerTaskWorkerPackedArray.length;
        Integer totalWorkerSize = workers.length;
        Double totalTravelingDistance = 0.0;
        Integer servedTaskSize = 0;
        Integer allocatedWorkerSize = 0;

//        Double totalUtility = 0.0;
        Double totalWinnerUtility = 0.0;
        Double totalFailureUtility = 0.0;

//        Integer totalFailureNumber = 0;

//        LinkedList workerIDList = new LinkedList();
//        BasicArray.setIntegerListToContinuousNaturalNumber(workerIDList, workers.length - 1);

//        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {
//
//            if (winnerTaskWorkerPackedArray[i] == null || winnerTaskWorkerPackedArray[i].getWorkerID().equals(-1)) {
//                ++ unServedTaskSize;
//                continue;
//            }
//            ++ allocatedWorkerSize;
//            Integer workerID = winnerTaskWorkerPackedArray[i].getWorkerID();
//            workerIDList.remove(workerID);
//            totalTravelingDistance += workers[workerID].getToTaskDistance(i);
//            totalWinnerUtility += workers[workerID].get
//        }
        Double tempUtility;
        for (int workerID = 0; workerID < workers.length; workerID++) {

            for (Integer taskID : workers[workerID].reverseIndex) {
                tempUtility = workers[workerID].getFinalUtility(taskID);
                if (taskID.equals(workers[workerID].getCurrentWinningState())) {
                    ++ servedTaskSize;
                    ++ allocatedWorkerSize;
                    totalWinnerUtility += tempUtility;
                    totalTravelingDistance += workers[workerID].getToTaskDistance(taskID);
                } else {
                    totalFailureUtility += tempUtility;
                }
            }
        }

        return new BasicExperimentResult(totalTaskSize, totalWorkerSize, totalTaskSize - servedTaskSize, allocatedWorkerSize, totalWinnerUtility, totalFailureUtility, totalTravelingDistance);

    }

    // for non privacy
    public static BasicExperimentResult getResultData(WorkerIDDistancePair[] winnerTaskWorkerPackedArray, Worker[] workers) {
        TaskWorkerIDPair[] taskWorkerIDPairArray;
        List<TaskWorkerIDPair> taskWorkerIDPairList = new ArrayList<>();

        List<Integer> nonCompeteTaskIDList = new ArrayList<>();
        List<Integer> competeFailureTaskIDList = new ArrayList<>();

        Integer totalTaskSize = winnerTaskWorkerPackedArray.length;
        Integer totalWorkerSize = workers.length;
        Double totalTravelingDistance = 0.0;
        Integer servedTaskSize = 0;
        Integer allocatedWorkerSize = 0;

//        Double totalUtility = 0.0;
        Double totalWinnerUtility = 0.0;
        Double totalFailureUtility = 0.0;

        Double tempUtility;
        for (int workerID = 0; workerID < workers.length; workerID++) {

            for (Integer taskID : workers[workerID].reverseIndex) {
                tempUtility = workers[workerID].getFinalUtility(taskID);
                if (taskID.equals(workers[workerID].getCurrentWinningState())) {
                    ++ servedTaskSize;
                    ++ allocatedWorkerSize;
                    totalWinnerUtility += tempUtility;
                    totalTravelingDistance += workers[workerID].getToTaskDistance(taskID);
                } else {
                    totalFailureUtility += tempUtility;
                }
            }
        }

        return new BasicExperimentResult(totalTaskSize, totalWorkerSize, totalTaskSize - servedTaskSize, allocatedWorkerSize, totalWinnerUtility, totalFailureUtility, totalTravelingDistance);

    }

    public static void showResultA(WorkerIDNoiseDistanceBudgetPair[] winnerTaskWorkerPackedArray) {
        int serveredTaskSize = 0;
        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {
//        for (int i = 0; i < 10; i++) {
            if (winnerTaskWorkerPackedArray[i].getWorkerID().equals(-1)) {
                continue;
            }
            ++ serveredTaskSize;
            System.out.println("Task ID: " + i + "; Winner worker ID: " + winnerTaskWorkerPackedArray[i].getWorkerID() + "; Effective noise distance: "
                    + winnerTaskWorkerPackedArray[i].getEffectiveNoiseDistance() + "; Effective privacy budget: "
                    + winnerTaskWorkerPackedArray[i].getEffectivePrivacyBudget());
        }

        System.out.println(serveredTaskSize);
    }

    public static void showResultB(WorkerIDNoiseDistanceBudgetPair[] winnerTaskWorkerPackedArray) {
        int serveredTaskSize = 0;
        TaskWorkerIDPair[] taskWorkerIDPairArray;
        List<TaskWorkerIDPair> taskWorkerIDPairList = new ArrayList<>();

        List<Integer> nonCompeteTaskIDList = new ArrayList<>();
        List<Integer> competeFailureTaskIDList = new ArrayList<>();

        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {

            if (winnerTaskWorkerPackedArray[i] == null) {
                competeFailureTaskIDList.add(i);
                continue;
            }
            if (winnerTaskWorkerPackedArray[i].getWorkerID().equals(-1)) {
                nonCompeteTaskIDList.add(i);
                continue;
            }
            taskWorkerIDPairList.add(new TaskWorkerIDPair(i, winnerTaskWorkerPackedArray[i].getWorkerID()));
        }
        serveredTaskSize = taskWorkerIDPairList.size();
        taskWorkerIDPairArray = taskWorkerIDPairList.toArray(new TaskWorkerIDPair[0]);
        Arrays.sort(taskWorkerIDPairArray, new TaskWorkerIDComparator(TaskWorkerIDComparator.WORKER_FIRST, TaskWorkerIDComparator.TASK_ASCENDING, TaskWorkerIDComparator.WORKER_ASCENDING));
        int tempTaskID, tempWorkerID;
        for (int i = 0; i < serveredTaskSize; i++) {
            tempTaskID = taskWorkerIDPairArray[i].getTaskID();
            tempWorkerID = taskWorkerIDPairArray[i].getWorkerID();
            System.out.println("Winner worker ID: " + tempWorkerID + "; Task ID: " + tempTaskID + "; Effective noise distance: "
                    + winnerTaskWorkerPackedArray[tempTaskID].getEffectiveNoiseDistance() + "; Effective privacy budget: "
                    + winnerTaskWorkerPackedArray[tempTaskID].getEffectivePrivacyBudget());
        }

        System.out.println(serveredTaskSize);
        System.out.println("Non competing tasks are:");
        MyPrint.showList(nonCompeteTaskIDList);
        System.out.println("Failure competing tasks are:");
        MyPrint.showList(competeFailureTaskIDList);

    }

    public static void showResultB(UtilityDistanceIDPair[] winnerTaskWorkerPackedArray) {
        int serveredTaskSize = 0;
        TaskWorkerIDPair[] taskWorkerIDPairArray;
        List<TaskWorkerIDPair> taskWorkerIDPairList = new ArrayList<>();

        List<Integer> nonCompeteTaskIDList = new ArrayList<>();
        List<Integer> competeFailureTaskIDList = new ArrayList<>();

        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {

            if (winnerTaskWorkerPackedArray[i] == null) {
                competeFailureTaskIDList.add(i);
                continue;
            }
            if (winnerTaskWorkerPackedArray[i].getId().equals(-1)) {
                nonCompeteTaskIDList.add(i);
                continue;
            }
            taskWorkerIDPairList.add(new TaskWorkerIDPair(i, winnerTaskWorkerPackedArray[i].getId()));
        }
        serveredTaskSize = taskWorkerIDPairList.size();
        taskWorkerIDPairArray = taskWorkerIDPairList.toArray(new TaskWorkerIDPair[0]);
        Arrays.sort(taskWorkerIDPairArray, new TaskWorkerIDComparator(TaskWorkerIDComparator.WORKER_FIRST, TaskWorkerIDComparator.TASK_ASCENDING, TaskWorkerIDComparator.WORKER_ASCENDING));
        int tempTaskID, tempWorkerID;
        for (int i = 0; i < serveredTaskSize; i++) {
            tempTaskID = taskWorkerIDPairArray[i].getTaskID();
            tempWorkerID = taskWorkerIDPairArray[i].getWorkerID();
            System.out.println("Winner worker ID: " + tempWorkerID + "; Task ID: " + tempTaskID + "; Distance: "
                    + winnerTaskWorkerPackedArray[tempTaskID].getDistance() + "; Utility: "
                    + winnerTaskWorkerPackedArray[tempTaskID].getUtility());
        }

        System.out.println(serveredTaskSize);
        System.out.println("Non competing tasks are:");
        MyPrint.showList(nonCompeteTaskIDList);
        System.out.println("Failure competing tasks are:");
        MyPrint.showList(competeFailureTaskIDList);

    }

}
