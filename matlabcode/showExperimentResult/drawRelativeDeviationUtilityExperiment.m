function y = drawRelativeDeviationUtilityExperiment(filename, xCol, yCol, xLabelName, yLabelName)
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

uConfNPSolution = matrix.data(6:9:end,yCol).*yfactor;
dConfNPSolution = matrix.data(7:9:end,yCol).*yfactor;
gNPSolution = matrix.data(8:9:end,yCol).*yfactor;
%greedyNPSolution = matrix.data(9:9:end,yCol).*yfactor;

uConfPSolution_ppcfFalse = matrix.data(1:9:end,yCol).*yfactor;
uConfPSolution_ppcfTrue = matrix.data(2:9:end,yCol).*yfactor;
dConfPSolution_ppcfFalse = matrix.data(3:9:end,yCol).*yfactor;
dConfPSolution_ppcfTrue = matrix.data(4:9:end,yCol).*yfactor;
gPSolution = matrix.data(5:9:end,yCol).*yfactor;

ucps_pf = (uConfNPSolution - uConfPSolution_ppcfFalse) ./ uConfNPSolution;
ucps_pt = (uConfNPSolution - uConfPSolution_ppcfTrue) ./ uConfNPSolution;
dcps_pf = (dConfNPSolution - dConfPSolution_ppcfFalse) ./ dConfNPSolution;
dcps_pt = (dConfNPSolution - dConfPSolution_ppcfTrue) ./ dConfNPSolution;
gps_pf = (gNPSolution - gPSolution) ./ gNPSolution;

%size(uConfPSolution_ppcfTrue)
fig = figure;
hold on;
%plot(x,uConfPSolution_ppcfFalse, x,uConfPSolution_ppcfTrue, x, dConfPSolution_ppcfFalse, x, dConfPSolution_ppcfTrue, x, gPSolution);
%plot(x,uConfPSolution_ppcfTrue, 'ro-','LineWidth',4, x, dConfPSolution_ppcfTrue, 'g*-','LineWidth',4, x, gPSolution, 'b+-','LineWidth',4);
plot(x,ucps_pt, 'ro-','LineWidth',2);
plot(x, dcps_pt, 'g*-','LineWidth',2);
plot(x, gps_pf, 'b+-','LineWidth',2);


figure_FontSize = 18;
set(get(gca,'XLabel'),'FontSize',figure_FontSize,'FontName','Times New Roman');
set(get(gca,'YLabel'),'FontSize',figure_FontSize,'FontName','Times New Roman');
set(gca,'FontName','Times New Roman' ,'FontSize',figure_FontSize);
set(findobj('FontSize',10),'FontSize',figure_FontSize);

xlabel(xLabelName);
ylabel(yLabelName);
h = legend('ucs-p', 'dcs-p','gts-p','Location','Best');
set(h,'FontName','Times New Roman','FontSize',14,'FontWeight','normal');

%frame = getframe(fig);
%img = frame2im(frame);
%for filename = output_filenames
%    imwrite(img,[output_basic_dir, filename]);
%end



