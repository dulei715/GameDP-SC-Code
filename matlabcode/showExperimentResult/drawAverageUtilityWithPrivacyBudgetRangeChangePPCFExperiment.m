function y = drawAverageUtilityWithPrivacyBudgetRangeChangePPCFExperiment(filename, xLabelName, yLabelName,outputFileName)
%function y = drawExperiment(filename, xCol, yCol, xLabelName, yLabelName, output_basic_dir, output_filenames)
lineStart = 2;
lineEnd = 4;
colStart = 5;
colEnd = 6;
%data = readmatrix(path,lineStart-1,colStart-1 ,[lineStart-1,lineEnd-1,colStart-1,colEnd-1]);
%data(lineStart:end,colStart:end)
%readmatrix(path)
matrix = importdata(filename, ',',1);
x1 = matrix.data(1:9:end,13);
x2 = matrix.data(1:9:end,14);
x = (x1+x2)./2;
yCol = 6; % TotalUtilityËùÔÚÁÐ

%yfactor = 0.0001;
yfactor = 1;
%yLabelName = 'competing time cost (ms)';
totalAllocatedWorker = matrix.data(:,5);
uConfPSolution_ppcfFalse = matrix.data(1:9:end,yCol).*yfactor ./ totalAllocatedWorker;
uConfPSolution_ppcfTrue = matrix.data(2:9:end,yCol).*yfactor ./ totalAllocatedWorker;
dConfPSolution_ppcfFalse = matrix.data(3:9:end,yCol).*yfactor ./ totalAllocatedWorker;
dConfPSolution_ppcfTrue = matrix.data(4:9:end,yCol).*yfactor ./ totalAllocatedWorker;

%size(uConfPSolution_ppcfTrue)
fig = figure;
hold on;
%plot(x,uConfPSolution_ppcfFalse, x,uConfPSolution_ppcfTrue, x, dConfPSolution_ppcfFalse, x, dConfPSolution_ppcfTrue, x, gPSolution);
%plot(x,uConfPSolution_ppcfTrue, 'ro-','LineWidth',4, x, dConfPSolution_ppcfTrue, 'g*-','LineWidth',4, x, gPSolution, 'b+-','LineWidth',4);
plot(x,uConfPSolution_ppcfTrue, 'ro-','LineWidth',2);
plot(x, dConfPSolution_ppcfTrue, 'g*-','LineWidth',2);

plot(x, uConfPSolution_ppcfFalse, 'rs','LineWidth',2);
plot(x, dConfPSolution_ppcfFalse, 'gx', 'LineWidth', 2);

figure_FontSize = 18;
set(get(gca,'XLabel'),'FontSize',figure_FontSize,'FontName','Times New Roman');
set(get(gca,'YLabel'),'FontSize',figure_FontSize,'FontName','Times New Roman');

set(gca,'FontName','Times New Roman' ,'FontSize',figure_FontSize);
set(findobj('FontSize',10),'FontSize',figure_FontSize);

xlabel(xLabelName);
ylabel(yLabelName);
h = legend('ucs-ppcf', 'dcs-ppcf','ucs-nppcf','dcs-nppcf','Location','Best');
set(h,'FontName','Times New Roman','FontSize',14,'FontWeight','normal');

%frame = getframe(fig);
%img = frame2im(frame);
%for filename = output_filenames
%    imwrite(img,[output_basic_dir, filename]);
%end
saveas(fig,outputFileName,'fig');
%saveas(fig,[outputFileName,'.eps'],'psc2');
export_fig(fig , '-pdf' , '-r256' , '-transparent' , outputFileName);


