function y = drawPointDatasetsOfTwoSets(pathA, pathB, factorK, constA)
% taskPath = [fileParentPath, '\task_point.txt'];
% workerPath = [fileParentPath, '\worker_point.txt'];
% disp(taskPath);
% disp(workerPath);

pointsA = textread(pathA);
pointsB = textread(pathB);
%size = points(1);

pointsA = pointsA(2:end,:).*factorK+constA;
pointsB = pointsB(2:end,:).*factorK+constA;

fig = figure;
hold on;
plot(pointsA(:,1),pointsA(:,2),'.r');
plot(pointsB(:,1),pointsB(:,2),'.b');
% set(gca,'FontSize',20);
figure_FontSize = 18;
set(get(gca,'XLabel'),'FontSize',figure_FontSize,'FontName','Times New Roman');
set(get(gca,'YLabel'),'FontSize',figure_FontSize,'FontName','Times New Roman');
set(gca,'FontName','Times New Roman' ,'FontSize',figure_FontSize);
set(findobj('FontSize',10),'FontSize',figure_FontSize);
xlabel('x (km)');
ylabel('y (km)');
%axis equal;
%hold off;