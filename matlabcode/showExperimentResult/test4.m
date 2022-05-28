function y = test4()
a=[1,2,3,4,5];
b=[4,5,6,7,8];
fig = figure;
hold on;
plot(a,b);
outputFileName='E:\test';
%saveas(fig,outputFileName,'fig');
export_fig(fig , '-pdf' , '-r256' , '-transparent' , outputFileName);