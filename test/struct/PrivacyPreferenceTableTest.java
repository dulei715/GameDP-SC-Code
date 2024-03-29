package struct;

import edu.ecnu.dll.basic.basic_struct.comparator.WorkerIDNoiseDistanceBudgetPairComparator;
import edu.ecnu.dll.basic.basic_struct.data_structure.PreferenceTable;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import org.junit.Test;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.print.MyPrint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrivacyPreferenceTableTest {
    @Test
    public void fun1() {
        List<WorkerIDNoiseDistanceBudgetPair>[] listArray = new List[2];
        listArray[0] = new ArrayList();
        listArray[1] = new ArrayList<>();
//        List<WorkerIDDistanceBudgetPair> dataList = new ArrayList<>();
        List<WorkerIDNoiseDistanceBudgetPair> dataList = listArray[0];
        int size = 3;
        double budget;
        double distance = 10;
//        double[] tempNoiseDistance = new double[]{9.12, 21.97, 0.65};
        Double[] tempNoiseDistance = new Double[]{90000.12, 210000.97, 0.65};
        WorkerIDNoiseDistanceBudgetPair workerIDNoiseDistanceBudgetPair;
        for (int i = 0; i < size; i++) {
//            budget = Math.random();
            budget = 10;
//            workerIDDistanceBudgetPair = new WorkerIDDistanceBudgetPair(i, distance*((i+1)%3) + LaplaceUtils.getLaplaceNoise(1,budget), budget);
            workerIDNoiseDistanceBudgetPair = new WorkerIDNoiseDistanceBudgetPair(i, tempNoiseDistance[i], budget);
//            workerIDDistanceBudgetPair = new WorkerIDDistanceBudgetPair(i, 0.0, 0.0);
            dataList.add(workerIDNoiseDistanceBudgetPair);
        }
        MyPrint.showList(dataList, "\r\n");
        Collections.sort(dataList, new WorkerIDNoiseDistanceBudgetPairComparator());
        MyPrint.showSplitLine("*", 100);
        MyPrint.showList(dataList, "\r\n");
    }

    @Test
    public void fun2() {




        int taskSize = 5;
        int workerSize = 3;
        double budget;
        double distance = 10;
        List<WorkerIDNoiseDistanceBudgetPair>[] listArray = new List[taskSize];
        for (int i = 0; i < taskSize; i++) {
            listArray[i] = new ArrayList<>();
        }
//        List<WorkerIDDistanceBudgetPair> dataList = new ArrayList<>();
        List<WorkerIDNoiseDistanceBudgetPair> dataList = listArray[0];

        Double[] tempNoiseDistance = new Double[]{9.12, 21.97, 0.65};
        WorkerIDNoiseDistanceBudgetPair workerIDNoiseDistanceBudgetPair;
        for (int i = 0; i < workerSize; i++) {
            budget = Math.random();
//            budget = 1;
            workerIDNoiseDistanceBudgetPair = new WorkerIDNoiseDistanceBudgetPair(i, distance*((i+1)%3) + LaplaceUtils.getLaplaceNoise(1,budget), budget);
            dataList.add(workerIDNoiseDistanceBudgetPair);
        }


        WorkerIDNoiseDistanceBudgetPairComparator comparator = new WorkerIDNoiseDistanceBudgetPairComparator();




        PreferenceTable<WorkerIDNoiseDistanceBudgetPair, WorkerIDNoiseDistanceBudgetPairComparator> privacyPreferenceTable = new PreferenceTable(taskSize, comparator);
        privacyPreferenceTable.setPreferenceTable(listArray);



//        double[] tempNoiseDistance = new double[]{9.12, 21.97, 0.65};

        MyPrint.showList(dataList, "\r\n");
        Collections.sort(dataList, new WorkerIDNoiseDistanceBudgetPairComparator());
        MyPrint.showSplitLine("*", 100);
        MyPrint.showList(dataList, "\r\n");
    }

    @Test
    public void fun3() {
        List<WorkerIDNoiseDistanceBudgetPair>[] listArray = new List[2];
        listArray[0] = new ArrayList();
        listArray[1] = new ArrayList<>();
//        List<WorkerIDDistanceBudgetPair> dataList = new ArrayList<>();
        List<WorkerIDNoiseDistanceBudgetPair> dataList = listArray[0];
        int size = 3;
        double[] budgets = new double[]{0.0, 9.6, 7.48};
        double distance = 10;
//        double[] tempNoiseDistance = new double[]{9.12, 21.97, 0.65};
        Double[] tempNoiseDistance = new Double[]{0.0, 77148.43268322185, 46139.93483654874};
        WorkerIDNoiseDistanceBudgetPair workerIDNoiseDistanceBudgetPair;
        for (int i = 0; i < size; i++) {
//            budget = Math.random();
//            workerIDDistanceBudgetPair = new WorkerIDDistanceBudgetPair(i, distance*((i+1)%3) + LaplaceUtils.getLaplaceNoise(1,budget), budget);
            workerIDNoiseDistanceBudgetPair = new WorkerIDNoiseDistanceBudgetPair(i, tempNoiseDistance[i], budgets[i]);
//            workerIDDistanceBudgetPair = new WorkerIDDistanceBudgetPair(i, 0.0, 0.0);
            dataList.add(workerIDNoiseDistanceBudgetPair);
        }
        MyPrint.showList(dataList, "\r\n");
        Collections.sort(dataList, new WorkerIDNoiseDistanceBudgetPairComparator());
        MyPrint.showSplitLine("*", 100);
        MyPrint.showList(dataList, "\r\n");
    }

}
