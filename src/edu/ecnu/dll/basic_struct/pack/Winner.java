package edu.ecnu.dll.basic_struct.pack;

public class Winner {
    private Integer[] winTaskWorkerIDArray;
    private Double[][] winTaskWorkerInfoArray;

    public Integer[] getWinTaskWorkerIDArray() {
        return winTaskWorkerIDArray;
    }

    public void setWinTaskWorkerIDArray(Integer[] winTaskWorkerIDArray) {
        this.winTaskWorkerIDArray = winTaskWorkerIDArray;
    }

    public Double[][] getWinTaskWorkerInfoArray() {
        return winTaskWorkerInfoArray;
    }

    public void setWinTaskWorkerInfoArray(Double[][] winTaskWorkerInfoArray) {
        this.winTaskWorkerInfoArray = winTaskWorkerInfoArray;
    }

    public Winner(Integer[] winTaskWorkerIDArray, Double[][] winTaskWorkerInfoArray) {
        this.winTaskWorkerIDArray = winTaskWorkerIDArray;
        this.winTaskWorkerInfoArray = winTaskWorkerInfoArray;
    }

    public Winner() {
    }
}
