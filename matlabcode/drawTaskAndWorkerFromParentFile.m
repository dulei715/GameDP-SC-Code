function y = drawTaskAndWorkerFromParentFile(fileParentPath)
taskPath = [fileParentPath, '\task_point.txt'];
workerPath = [fileParentPath, '\worker_point.txt'];
% disp(taskPath);
% disp(workerPath);

taskPoints = textread(taskPath);
taskSize = taskPoints(1);
% disp(taskSize);

workerPoints = textread(workerPath);
workerSize = workerPoints(1);

taskPoints = taskPoints(2:end,:);
workerPoints = workerPoints(2:end, :);
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