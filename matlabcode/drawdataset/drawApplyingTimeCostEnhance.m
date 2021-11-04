function [] = drawApplyingTimeCostEnhance(path,sheet,xlabelValue)
[num,txt,raw]=xlsread(path,sheet);
x = num(1,:);
%disp(x);
t_noP = num(2,:);
t_bas = num(3,:);
t_adv = num(4,:);

w_noP = num(5,:);
w_bas = num(6,:);
w_adv = num(7,:);
%disp(y);
plot(x,t_noP,'ro-',x,t_bas,'g*-',x,t_adv,'b+-');
hold on;
plot(x,w_noP,'cs:',x,w_bas,'mx:',x,w_adv,'kd:');
%plot(x,y1,'ro-')
xlabel(xlabelValue);
ylabel('Average accuracy score');

legend('no privacy task score','basic task score', 'advanced task score', 'non-privacy worker score','basic worker score', 'advanced worker score', 'Location', 'Best');

