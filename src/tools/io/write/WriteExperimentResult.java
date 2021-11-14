package tools.io.write;

import edu.ecnu.dll.basic_struct.pack.experiment_result_info.NormalExperimentResult;

import java.util.List;

public class WriteExperimentResult extends BasicWrite {
    public static final String SPLIT_SYMBOL = ",";
    public void writeResultList(String outputPath, List<String> titles, List<NormalExperimentResult> data) {
        startWriting(outputPath);
        writeOneLine(titles, SPLIT_SYMBOL);
        writeListDataWithNewLineSplit(data);
        endWriting();
    }

    public void writeResultList(String outputPath, String titles, List<String> data) {
        startWriting(outputPath);
        writeOneLine(titles);
        writeListDataWithNewLineSplit(data);
        endWriting();
    }
}
