package edu.ecnu.dll.dataset.dataset_generating.sample.impl;

import edu.ecnu.dll.dataset.dataset_generating.sample.SamplingFunction;
import tools.io.print.MyPrint;

import java.util.ArrayList;
import java.util.List;

/**
 * groupSize表示将数据每groupSize大小分成一份
 * shareSize代表取其中的连续shareSize份
 * bias代表从每份的第几个开始取
 */
public class MeanSamplingFunction extends SamplingFunction {
    public int groupMemberSize;
    public int shareSize;
    public int bias;

    public MeanSamplingFunction(int groupMemberSize, int shareSize, int bias) {
        if (shareSize > groupMemberSize) {
            throw new RuntimeException("Share size is larger than group member size!");
        }
        bias %= groupMemberSize;
        if (bias < 0) {
            bias = groupMemberSize + bias;
        }
        if (shareSize + bias > groupMemberSize) {
            throw new RuntimeException("The sum of share size and bias is larger than group member size!");
        }
        this.groupMemberSize = groupMemberSize;
        this.shareSize = shareSize;
        this.bias = bias;
    }

    @Override
    public List<Integer> sample(int size) {
        List<Integer> result = new ArrayList<>();
        int lowerBound = this.bias;
        int upperBound = this.bias + this.shareSize;
        int groupInnerIndex;
        for (int i = 0; i < size; i++) {
            groupInnerIndex = i % this.groupMemberSize;
            if (groupInnerIndex >= lowerBound && groupInnerIndex < upperBound) {
                result.add(i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
//        SamplingFunction samplingFunction = new MeanSamplingFunction(7, 3, 1);
        SamplingFunction samplingFunction = new MeanSamplingFunction(3, 1, 0);
        List<Integer> result = samplingFunction.sample(100);
        MyPrint.showList(result);
    }











}
