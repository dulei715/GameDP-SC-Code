package tools.io.read;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BasicRead {
    public String INPUT_SPLIT_SYMBOL = " ";
    protected BufferedReader bufferedReader = null;

    public BasicRead() {
    }

    public BasicRead(String INPUT_SPLIT_SYMBOL) {
        this.INPUT_SPLIT_SYMBOL = INPUT_SPLIT_SYMBOL;
    }

    public void startReading(String inputPath) {
        File file = new File(inputPath);
        try {
            this.bufferedReader = new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readOneLine() {
        String line = null;
        try {
            line = this.bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public List<String> readDeclaredLines(Set<Integer> indexSet) {
        List<String> result = new ArrayList<>(indexSet.size());
        String line;
        int lineNum = -1;
        try {
            line = this.bufferedReader.readLine();
            while ((line = this.bufferedReader.readLine()) != null) {
                ++ lineNum;
                if (!indexSet.contains(lineNum)) {
                    continue;
                }
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public List<String> readAll() {
        List<String> result = null;
        String line;
        try {
            line = this.bufferedReader.readLine();
            result = new ArrayList<>(Integer.valueOf(line));
            while ((line = this.bufferedReader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public void mark(int size) {
        try {
            this.bufferedReader.mark(size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            this.bufferedReader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void endReading() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
