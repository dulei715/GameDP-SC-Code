function y = drawAverageUtilityExperiment(filename, xCol, yCol, xLabelName, yLabelName,outputFileName, figure_MarkerSize, figure_FontSize_X, figure_FontSize_Y, figure_FontSize)
%function y = drawExperiment(filename, xCol, yCol, xLabelName, yLabelName, output_basic_dir, output_filenames)
%data = readmatrix(path,lineStart-1,colStart-1 ,[lineStart-1,lineEnd-1,colStart-1,colEnd-1]);
%data(lineStart:end,colStart:end)
%readmatrix(path)
matrix = importdata(filename, ',',1);
x = matrix.data(1:9:end,xCol);
if xCol == 3
    taskNumber = matrix.data(1:9:end,2);
    x = x./taskNumber;
end
%yfactor = 0.0001;
yfactor = 1;
totalAllocatedWorker = matrix.data(:,5);
uConfPSolution_ppcfFalse = matrix.data(1:9:end,yCol).*yfactor./totalAllocatedWorker(1:9:end,1);
uConfPSolution_ppcfTrue = matrix.data(2:9:end,yCol).*yfactor./totalAllocatedWorker(2:9:end,1);
dConfPSolution_ppcfFalse = matrix.data(3:9:end,yCol).*yfactor./totalAllocatedWorker(3:9:end,1);
dConfPSolution_ppcfTrue = matrix.data(4:9:end,yCol).*yfactor./totalAllocatedWorker(4:9:end,1);
gPSolution = matrix.data(5:9:end,yCol).*yfactor./totalAllocatedWorker(5:9:end,1);
uConfNPSolution = matrix.data(6:9:end,yCol).*yfactor./totalAllocatedWorker(6:9:end,1);
dConfNPSolution = matrix.data(7:9:end,yCol).*yfactor./totalAllocatedWorker(7:9:end,1);
gNPSolution = matrix.data(8:9:end,yCol).*yfactor./totalAllocatedWorker(8:9:end,1);
greedyNPSolution = matrix.data(9:9:end,yCol).*yfactor./totalAllocatedWorker(9:9:end,1);
%size(uConfPSolution_ppcfTrue)
fig = figure;
hold on;
%plot(x,uConfPSolution_ppcfFalse, x,uConfPSolution_ppcfTrue, x, dConfPSolution_ppcfFalse, x, dConfPSolution_ppcfTrue, x, gPSolution);
%plot(x,uConfPSolution_ppcfTrue, 'ro-','LineWidth',4, x, dConfPSolution_ppcfTrue, 'g*-','LineWidth',4, x, gPSolution, 'b+-','LineWidth',4);

%figure_MarkerSize = 10;
plot(x,uConfPSolution_ppcfTrue, 'ro-','LineWidth',2, 'MarkerSize',figure_MarkerSize);
plot(x, dConfPSolution_ppcfTrue, 'g*-','LineWidth',2, 'MarkerSize',figure_MarkerSize);
plot(x, gPSolution, 'b+-','LineWidth',2, 'MarkerSize',figure_MarkerSize);

plot(x, uConfNPSolution, 'rs:','LineWidth',2, 'MarkerSize',figure_MarkerSize);
plot(x, dConfNPSolution, 'gx:', 'LineWidth', 2, 'MarkerSize',figure_MarkerSize);
plot(x, gNPSolution, 'bd:', 'LineWidth',2, 'MarkerSize',figure_MarkerSize);
plot(x, greedyNPSolution, 'm^:','LineWidth',2, 'MarkerSize',figure_MarkerSize);

%xlim([x(1) x(length(x))]);
xlim([roundn(x(1),-1) x(length(x))]);
set(gca,'XTick',roundn(x,-1));

%figure_FontSize = 22;
set(gca,'FontName','Times New Roman' ,'FontSize',figure_FontSize);
%set(findobj('FontSize',figure_MarkerSize),'FontSize',figure_FontSize);
xlabel(xLabelName);
ylabel(yLabelName);
set(get(gca,'XLabel'),'FontSize',figure_FontSize_X,'FontName','Times New Roman');
set(get(gca,'YLabel'),'FontSize',figure_FontSize_Y,'FontName','Times New Roman');

%legend_FontSize = 14;
legend_FontSize = 20;
locationType = 'BestOutside';

%h = legend('ucs-p', 'dcs-p','gts-p','ucs-np','dcs-np', 'gts-np', 'grs-np','Location','Best');
h = legend('PUCE', 'PDCE','PGT','UCE','DCE', 'GT', 'GRD','Location',locationType);
set(h,'FontName','Times New Roman','FontSize',legend_FontSize,'FontWeight','normal');

%frame = getframe(fig);
%img = frame2im(frame);
%for filename = output_filenames
%    imwrite(img,[output_basic_dir, filename]);
%end
legend('off');
saveas(fig,outputFileName,'fig');
%saveas(fig,[outputFileName,'.eps'],'psc2');
%saveas(fig,outputFileName,'eps');
export_fig(fig , '-pdf' , '-r256' , '-transparent' , outputFileName);


