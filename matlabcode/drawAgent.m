function y = drawAgent(tasks, workers, workerRanges)
taskColor = 'black';
workerColor = 'red';
%disp(tasks(:,1))
%disp(tasks(:,2))
%for i = [0:length(tasks)]
scatter(tasks(:,1), tasks(:,2), 'filled');
hold on;
%scatter(workers(:,1), workers(:,2), 'filled');
%for i = 1:length(tasks)
    
%end
for j = 1:length(workers)
    drawCircle(workers(j,:),workerRanges(j));
end
axis equal;
hold off;