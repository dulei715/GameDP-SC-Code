function y = drawPointDataset(path)
% taskPath = [fileParentPath, '\task_point.txt'];
% workerPath = [fileParentPath, '\worker_point.txt'];
% disp(taskPath);
% disp(workerPath);

points = textread(path);
size = points(1);

points = points(2:end,:);
%taskColor = 'black';
%workerColor = 'red';
%disp(tasks(:,1))
%disp(tasks(:,2))
%for i = [0:length(tasks)]
%scatter(points(:,1), points(:,2), 'filled');
%scatter(points(:,1), points(:,2));
plot(points(:,1),points(:,2),'.');
set(gca,'FontSize',20);
%axis equal;
%hold off;