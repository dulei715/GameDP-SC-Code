package edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy;

import edu.ecnu.dll.basic.basic_solution.NonPrivacySolution;
import edu.ecnu.dll.basic.basic_solution.Solution;
import edu.ecnu.dll.basic.basic_struct.pack.UtilityDistanceIDPair;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.run.run_main.AbstractRun;
import tools.basic.BasicCalculation;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.struct.Point;

import java.util.List;

@Deprecated
public class GreedyNonePrivacyBeforeSolution extends NonPrivacySolution {



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



    public UtilityDistanceIDPair[] compete() {
        UtilityDistanceIDPair[] result = new UtilityDistanceIDPair[this.tasks.length];
        UtilityDistanceIDPair tempPair;
        Integer tempWorkerID;
        for (int i = 0; i < this.tasks.length; i++) {
            tempPair = this.tasks[i].getFirstElement();
            if (tempPair == null) {
                continue;
            }
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
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\test";
        String basicDatasetPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_default";

//        double[] fixedTaskValueAndWorkerRange = new double[]{20.0, 2};
        Solution.alpha = 0.001;
        Solution.beta = 1;

        double[] fixedTaskValueAndWorkerRange = new double[]{40.0, 4000};
//        Integer dataType = AbstractRun.LONGITUDE_LATITUDE;
        Integer dataType = AbstractRun.COORDINATE;

        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";
//        String workerPrivacyBudgetPath = basicDatasetPath + "\\worker_budget.txt";
//        String workerNoiseDistancePath = basicDatasetPath + "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        List<Double> taskValueList = null;

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleWithFirstSizeLineToList(workerRangePath);
//        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath);
//        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(workerNoiseDistancePath);


        // 初始化 task 和 workers
        Double taskValue , workerRange;
        GreedyNonePrivacyBeforeSolution competitionSolution = new GreedyNonePrivacyBeforeSolution();
//        competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);

        if (fixedTaskValueAndWorkerRange == null) {
            taskValueList = DoubleRead.readDoubleWithFirstSizeLineToList(taskValuePath);
            workerRangeList = DoubleRead.readDoubleWithFirstSizeLineToList(workerRangePath);
            competitionSolution.initializeBasicInformation(taskPointList, taskValueList, workerPointList, workerRangeList);
        } else {
            taskValue = fixedTaskValueAndWorkerRange[0];
            workerRange = fixedTaskValueAndWorkerRange[1];
            competitionSolution.initializeBasicInformation(taskPointList, taskValue, workerPointList, workerRange);
        }


        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
//        Integer dataTypeValue = Integer.valueOf(dataType);
//        competitionSolution.initializeAgentsWithLatitudeLongitude();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            competitionSolution.initializeAgents();
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            competitionSolution.initializeAgentsWithLatitudeLongitude();
        } else {
            throw new RuntimeException("The type input is not right!");
        }

        long startCompetingTime = System.currentTimeMillis();
        UtilityDistanceIDPair[] winner = competitionSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        double totalUtility = CommonFunction.getResultUtilityData(winner);

        System.out.println(runningTime);

        CommonFunction.showResultB(winner);

        System.out.println("Total utility: " + totalUtility);



    }



}
