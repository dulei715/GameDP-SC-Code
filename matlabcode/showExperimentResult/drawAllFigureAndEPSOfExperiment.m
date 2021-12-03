function y = drawAllFigureAndEPSOfExperiment(outputBasicParentPath)
inputParentArray = [
"E:\1.ѧϰ\4.���ݼ�\dataset\original\chengdu_total_dataset_km\outputPath\",
"E:\1.ѧϰ\4.���ݼ�\dataset\original\nyc_total_dataset_ll\outputPath\",
"E:\1.ѧϰ\4.���ݼ�\dataset\original\tky_total_dataset_ll\outputPath\",
"E:\1.ѧϰ\4.���ݼ�\dataset\original\uniform_total_dataset_km\outputPath\",
"E:\1.ѧϰ\4.���ݼ�\dataset\original\normal_total_dataset_km\outputPath\"
];

inputFileNameArray = [
"chengdu", "nyc", "tky", "uniform", "normal"
];

%outputParentArray = [strcat(outputBasicParentPath, "chengdu\"), strcat(outputBasicParentPath, "nyc\"), strcat(outputBasicParentPath, "tky"), strcat(outputBasicParentPath, "uniform"), strcat(outputBasicParentPath, "normal")];
outputParentArray = [];
for name = inputFileNameArray
    newOutputParentPath = strcat(outputBasicParentPath, name, "\");
    %disp(ppath)
    if ~exist(newOutputParentPath,'dir')
        mkdir(char(newOutputParentPath));
    end
    outputParentArray = [outputParentArray, newOutputParentPath];
end

for i = 1:length(inputParentArray)
    %disp(i);
    drawAllFigureAndEPSOfExperimentByDataset(char(inputParentArray(i)),char(outputParentArray(i)),char(inputFileNameArray(i)));
end
