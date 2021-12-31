function y = drawAllFigureAndPDFOfExperimentOnlyForPrivacyBudget(outputBasicParentPath)
inputParentArray = [
"E:\1.学习\4.数据集\total_result\1.chengdu\",
%"E:\1.学习\4.数据集\dataset\original\nyc_total_dataset_ll\outputPath\",
%"E:\1.学习\4.数据集\dataset\original\tky_total_dataset_ll\outputPath\",
"E:\1.学习\4.数据集\total_result\2.uniform\",
"E:\1.学习\4.数据集\total_result\3.normal\"
];

%inputFileNameArray = ["chengdu", "nyc", "tky", "uniform", "normal"];
inputFileNameArray = ["chengdu", "uniform", "normal"];
%inputFileNameArray = ["chengdu", "normal"];

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
    drawAllFigureAndPDFOfExperimentByDatasetOnlyForPrivacyBudget(char(inputParentArray(i)),char(outputParentArray(i)),char(inputFileNameArray(i)));
end

