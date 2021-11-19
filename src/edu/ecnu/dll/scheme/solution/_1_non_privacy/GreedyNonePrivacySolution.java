package edu.ecnu.dll.scheme.solution._1_non_privacy;

import edu.ecnu.dll.basic_struct.pack.UtilityDistanceIDPair;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.scheme.run.common.CommonFunction;
import edu.ecnu.dll.scheme.run.main_run.AbstractRun;
import edu.ecnu.dll.scheme.run.target_tools.TargetTool;
import edu.ecnu.dll.scheme.solution._0_basic.NonPrivacySolution;
import edu.ecnu.dll.scheme.solution._3_multiple_task.ConflictEliminationBasedSolution;
import tools.basic.BasicCalculation;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.List;

public class GreedyNonePrivacySolution extends NonPrivacySolution {



    public void initializeAgents() {


        for (int j = 0; j < this.workers.length; j++) {
            double tempDistance;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                Double tempUtility;
                if (tempDistance <= this.workers[j].maxRange) {
                    tempUtility = getUtilityValue(this.tasks[i].valuation, tempDistance);
                    this.tasks[i].addElement(j, tempUtility, tempDistance);
                }
            }
        }
    }
//
    public void initializeAgentsWithLatitudeLongitude() {
        for (int j = 0; j < this.workers.length; j++) {
            double tempDistance;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.getDistanceFrom2LngLat(this.tasks[i].location[1], this.tasks[i].location[0], this.workers[j].location[1],this.workers[j].location[0]);
                Double tempUtility;
                if (tempDistance <= this.workers[j].maxRange) {
                    tempUtility = getUtilityValue(this.tasks[i].valuation, tempDistance);
                    this.tasks[i].addElement(j, tempUtility, tempDistance);
                }
            }
        }
    }



    public UtilityDistanceIDPair[] greedy() {
        UtilityDistanceIDPair[] result = new UtilityDistanceIDPair[this.tasks.length];
        UtilityDistanceIDPair tempPair;
        Integer tempWorkerID;
        for (int i = 0; i < this.tasks.length; i++) {
            tempPair = this.tasks[i].getFirstElement();
            if(tempPair.getUtility() <= 0) {
                continue;
            }
            result[i] = tempPair;
            tempWorkerID = tempPair.getId();
            for (int k = i + 1; k < this.tasks.length; k++) {
                this.tasks[k].removeFirstElementByID(tempWorkerID);
            }
        }
        return result;
    }



    public static void main(String[] args) {
        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";

        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";
//        String workerPrivacyBudgetPath = basicDatasetPath + "\\worker_budget.txt";
//        String workerNoiseDistancePath = basicDatasetPath + "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleToList(workerRangePath);
//        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath);
//        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(workerNoiseDistancePath);


        // 初始化 task 和 workers
        Double taskValue = 20.0, workerRange = 2.0;
        GreedyNonePrivacySolution competitionSolution = new GreedyNonePrivacySolution();
        competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);


        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
//        Integer dataTypeValue = Integer.valueOf(dataType);
        competitionSolution.initializeAgentsWithLatitudeLongitude();

        long startCompetingTime = System.currentTimeMillis();
        UtilityDistanceIDPair[] winner = competitionSolution.greedy();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        System.out.println(runningTime);

        CommonFunction.showResultB(winner);



    }



}
