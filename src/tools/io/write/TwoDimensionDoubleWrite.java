package tools.io.write;

import tools.basic.BasicCalculation;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.struct.Point;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TwoDimensionDoubleWrite extends BasicWrite {

    public TwoDimensionDoubleWrite() {
    }

    public TwoDimensionDoubleWrite(String OUTPUT_SPLIT_SYMBOL) {
        super(OUTPUT_SPLIT_SYMBOL);
    }

    public void write(List<Double[]>[] twoDimensionDoubleData) {
        int workerSize = twoDimensionDoubleData.length;
        int taskSize = twoDimensionDoubleData[0].size();
        int budgetGroupSize = twoDimensionDoubleData[0].get(0).length;

        Double[] arrayLineData;
        try {
            super.bufferedWriter.write(workerSize + OUTPUT_SPLIT_SYMBOL + taskSize + OUTPUT_SPLIT_SYMBOL + budgetGroupSize);
            bufferedWriter.newLine();
            for (int i = 0, j, k; i < workerSize; i++) {
//                taskValue = BasicCalculation.getSortedRandomStringValueTwoDimensionArrayInRange(lowerBound, upperBound, precision, taskSize, budgetGroupSize);
                for (j = 0; j < taskSize; j++) {
                    arrayLineData = twoDimensionDoubleData[i].get(j);

                    for (k = 0; k < arrayLineData.length - 1; k++) {
                        bufferedWriter.write(String.valueOf(arrayLineData[k]));
                        bufferedWriter.write(OUTPUT_SPLIT_SYMBOL);
                    }
                    bufferedWriter.write(String.valueOf(arrayLineData[k]));
                    bufferedWriter.newLine();
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
