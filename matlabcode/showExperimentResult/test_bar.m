function y = test_bar()
fig = figure;
hold on;

figure_MarkerSize = 10;
x = [1:0.1:10];
uConfPSolution_ppcfTrue = x.*x;
dConfPSolution_ppcfTrue = 2*x;
gPSolution = 2*x.*x;
uConfNPSolution = 2*x;
dConfNPSolution = 2*x;
gNPSolution = 2*x;
greedyNPSolution = 2*x;

a = plot(x,uConfPSolution_ppcfTrue, 'ro-','LineWidth',2, 'MarkerSize',figure_MarkerSize);
b = plot(x, dConfPSolution_ppcfTrue, 'g*-','LineWidth',2, 'MarkerSize',figure_MarkerSize);
c = plot(x, gPSolution, 'b+-','LineWidth',2, 'MarkerSize',figure_MarkerSize);

xlim([x(1) x(length(x))]);
set(gca,'XTick',x);


%a.EraseMode='none';
%b.EraseMode='none';
%c.EraseMode='none';


%d = plot(x, uConfNPSolution, 'rs:','LineWidth',2, 'MarkerSize',figure_MarkerSize);
%e = plot(x, dConfNPSolution, 'gx:', 'LineWidth', 2, 'MarkerSize',figure_MarkerSize);
%f = plot(x, gNPSolution, 'bd:', 'LineWidth',2, 'MarkerSize',figure_MarkerSize);
%g = plot(x, greedyNPSolution, 'm^:','LineWidth',2, 'MarkerSize',figure_MarkerSize);


%ax = gca;
%ax.Visible = 'off';

%a.Visible='off';
%b.Visible='off';
%c.Visible='off';
%d.Visible='off';
%e.Visible='off';
%f.Visible='off';
g.Visible='off';

legend_FontSize = 20;
locationType = 'northoutside';
orientationType = 'horizontal';
textColor = 'black';
%h = legend('ucs-p', 'dcs-p','gts-p','ucs-np','dcs-np', 'gts-np', 'grs-np','Location','Best');
h = legend('PUCE', 'PDCE','PGT','UCE','DCE', 'GT', 'GRD','Location',locationType,'Orientation',orientationType, 'TextColor', textColor);
set(h,'FontName','Times New Roman','FontSize',legend_FontSize,'FontWeight','normal');
%plotbrowser (h, 'on');
%legend('off')
%fig.Visible = 'off';
%saveas(fig,'bar','fig');
%export_fig(fig , '-eps' , '-r256' , '-transparent' , 'bar');
