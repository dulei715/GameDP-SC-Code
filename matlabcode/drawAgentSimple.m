function y = drawAgentSimpleFromFile(filePath)

taskPoints = [];
workerPoints = [];
taskColor = 'black';
workerColor = 'red';
%disp(tasks(:,1))
%disp(tasks(:,2))
%for i = [0:length(tasks)]
scatter(taskPoints(:,1), taskPoints(:,2), 'filled');
hold on;
scatter(workerPoints(:,1), workerPoints(:,2), 'filled');
axis equal;
hold off;