function y = drawAllFigureAndPDFOfExperimentByDatasetOnlyForPrivacyBudget(input_parent_path, output_parent_path, output_dataset_name)
figure_MarkerSize = 15;
figure_FontSize = 30;
figure_FontSize_X = 30;
figure_FontSize_Y_A = 30;
%% 画关于utility变化的图 

% 自变量为 worker ratio 因变量为 average utility
dataFilePath = [input_parent_path, 'output_worker_budget_change.csv'];
outputFileName = [output_parent_path, 'utility_budget_change_',output_dataset_name];
xName = 'privacy budget';
yName = 'average utility';
drawAverageUtilityWithPrivacyBudgetRangeChangePPCFExperiment(dataFilePath, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);



