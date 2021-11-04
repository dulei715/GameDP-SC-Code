package tools.io.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BasicWrite {
    public String OUTPUT_SPLIT_SYMBOL = " ";
    private BufferedWriter bufferedWriter = null;

    public BasicWrite() {
    }

    public BasicWrite(String OUTPUT_SPLIT_SYMBOL) {
        this.OUTPUT_SPLIT_SYMBOL = OUTPUT_SPLIT_SYMBOL;
    }

    public void startWriting(String outputPath) {
        File file = new File(outputPath);
        File parentFile = file.getParentFile();

        try {
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            this.bufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(List dataList) {
        int i = 0;
        Object obj;
        try {
            for (; i < dataList.size() - 1; i++) {
                obj = dataList.get(i);
                if (obj instanceof java.lang.Number) {
                    this.bufferedWriter.write(String.valueOf(dataList.get(i)));
                } else if (obj instanceof java.lang.String) {
                    this.bufferedWriter.write((String)obj);
                } else {
                    this.bufferedWriter.write(obj.toString());
                }
                this.bufferedWriter.write(OUTPUT_SPLIT_SYMBOL);
            }
            obj = dataList.get(i);
            if (obj instanceof java.lang.Number) {
                this.bufferedWriter.write(String.valueOf(dataList.get(i)));
            } else if (obj instanceof java.lang.String) {
                this.bufferedWriter.write((String)obj);
            } else {
                this.bufferedWriter.write(obj.toString());
            }
            this.bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endWriting() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
