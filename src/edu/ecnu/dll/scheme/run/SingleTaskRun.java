package edu.ecnu.dll.scheme.run;

import tools.io.read.PointRead;

public class SingleTaskRun {
    public static void main(String[] args) {
        // 从数据库读数据
        String dataPath = System.getProperty("user.dir") + "\\dataset\\real_dataset\\chengdu.node";
        PointRead pointRead = new PointRead(dataPath);
        pointRead.readPoint(50000);
        System.out.println(pointRead.getTotalDataSize());
        System.out.println(pointRead.getRealDataSize());
        // 初始化 task 和 workers

        // 执行竞争过程
//        SingleTaskSolution singleTaskSolution = new SingleTaskSolution();
    }
}
