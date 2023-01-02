function y = drawAllFigureAndPDFOfExperimentByDataset(input_parent_path, output_parent_path, output_dataset_name)
figure_MarkerSize = 15;
figure_FontSize = 30;
figure_FontSize_X = 30;
figure_FontSize_Y_A = 30;
figure_FontSize_Y_B = 22;
%% ����������ʱ���ͼ
dataFilePath = [input_parent_path, 'output_worker_ratio_change.csv'];
outputFileName = [output_parent_path, 'timecost_ratio_change_',output_dataset_name];
workerRatioLine = 3;
xName = 'worker ratio';
yName = 'running time (ms)';
drawTimeCostExperiment(dataFilePath, workerRatioLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);

%% ������utility�仯��ͼ 
% �Ա���Ϊ task value �����Ϊ average utility
dataFilePath = [input_parent_path, 'output_task_value_change.csv'];
outputFileName = [output_parent_path, 'utility_value_change_',output_dataset_name];
taskValueLine = 11;
UtilityLine = 6;
xName = 'task value';
yName = 'average utility';
drawAverageUtilityExperiment(dataFilePath, taskValueLine,UtilityLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);

% �Ա���Ϊ task value �����Ϊ relative deviation of utility
dataFilePath = [input_parent_path, 'output_task_value_change.csv'];
outputFileName = [output_parent_path, 'utilitydeviation_value_change_',output_dataset_name];
taskValueLine = 11;
UtilityLine = 6;
xName = 'task value';
yName = 'relative deviation of utility';
drawRelativeDeviationUtilityExperiment(dataFilePath, taskValueLine,UtilityLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_B, figure_FontSize);


% �Ա���Ϊ worker range �����Ϊ average utility
dataFilePath = [input_parent_path, 'output_worker_range_change.csv'];
outputFileName = [output_parent_path, 'utility_range_change_',output_dataset_name];
workerRangeLine = 12;
UtilityLine = 6;
xName = 'worker range';
yName = 'average utility';
drawAverageUtilityExperiment(dataFilePath, workerRangeLine,UtilityLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);

% �Ա���Ϊ worker range �����Ϊ relative deviation of utility
dataFilePath = [input_parent_path, 'output_worker_range_change.csv'];
outputFileName = [output_parent_path, 'utilitydeviation_range_change_',output_dataset_name];
workerRangeLine = 12;
UtilityLine = 6;
xName = 'worker range';
yName = 'relative deviation of utility';
drawRelativeDeviationUtilityExperiment(dataFilePath, workerRangeLine,UtilityLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_B, figure_FontSize);


% �Ա���Ϊ worker ratio �����Ϊ average utility
dataFilePath = [input_parent_path, 'output_worker_ratio_change.csv'];
outputFileName = [output_parent_path, 'utility_ratio_change_',output_dataset_name];
workerRatioLine = 3;
UtilityLine = 6;
xName = 'worker ratio';
yName = 'average utility';
drawAverageUtilityExperiment(dataFilePath, workerRatioLine, UtilityLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);


% �Ա���Ϊ worker ratio �����Ϊ relative deviation of utility
dataFilePath = [input_parent_path, 'output_worker_ratio_change.csv'];
outputFileName = [output_parent_path, 'utilitydeviation_ratio_change_',output_dataset_name];
workerRatioLine = 3;
UtilityLine = 6;
xName = 'worker ratio';
yName = 'relative deviation of utility';
drawRelativeDeviationUtilityExperiment(dataFilePath, workerRatioLine,UtilityLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_B, figure_FontSize);

%% ������distance�仯��ͼ 
% �Ա���Ϊ task value �����Ϊ average distance
dataFilePath = [input_parent_path, 'output_task_value_change.csv'];
outputFileName = [output_parent_path, 'distance_value_change_',output_dataset_name];
taskValueLine = 11;
DistanceLine = 9;
xName = 'task value';
yName = 'average distance (km)';
drawAverageDistanceExperiment(dataFilePath, taskValueLine,DistanceLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);

% �Ա���Ϊ task value �����Ϊ relative deviation of distance
dataFilePath = [input_parent_path, 'output_task_value_change.csv'];
outputFileName = [output_parent_path, 'distancedeviation_value_change_',output_dataset_name];
taskValueLine = 11;
DistanceLine = 9;
xName = 'task value';
yName = 'relative deviation of distance';
drawRelativeDeviationDistanceExperiment(dataFilePath, taskValueLine,DistanceLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_B, figure_FontSize);

% �Ա���Ϊ worker range �����Ϊ average distance
dataFilePath = [input_parent_path, 'output_worker_range_change.csv'];
outputFileName = [output_parent_path, 'distance_range_change_',output_dataset_name];
workerRangeLine = 12;
DistanceLine = 9;
xName = 'worker range';
yName = 'average distance (km)';
drawAverageDistanceExperiment(dataFilePath, workerRangeLine,DistanceLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);

% �Ա���Ϊ worker range �����Ϊ relative deviation of distance
dataFilePath = [input_parent_path, 'output_worker_range_change.csv'];
outputFileName = [output_parent_path, 'distancedeviation_range_change_',output_dataset_name];
workerRangeLine = 12;
DistanceLine = 9;
xName = 'worker range';
yName = 'relative deviation of distance';
drawRelativeDeviationDistanceExperiment(dataFilePath, workerRangeLine,DistanceLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_B, figure_FontSize);

% �Ա���Ϊ worker ratio �����Ϊ average distance
dataFilePath = [input_parent_path, 'output_worker_ratio_change.csv'];
outputFileName = [output_parent_path, 'distance_ratio_change_',output_dataset_name];
workerRatioLine = 3;
DistanceLine = 9;
xName = 'worker ratio';
yName = 'average distance (km)';
drawAverageDistanceExperiment(dataFilePath, workerRatioLine, DistanceLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_A, figure_FontSize);

% �Ա���Ϊ worker ratio �����Ϊ relative deviation of distance
dataFilePath = [input_parent_path, 'output_worker_ratio_change.csv'];
outputFileName = [output_parent_path, 'distancedeviation_ratio_change_',output_dataset_name];
workerRatioLine = 3;
DistanceLine = 9;
xName = 'worker ratio';
yName = 'relative deviation of distance';
drawRelativeDeviationDistanceExperiment(dataFilePath, workerRatioLine,DistanceLine, xName, yName, outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y_B, figure_FontSize);

