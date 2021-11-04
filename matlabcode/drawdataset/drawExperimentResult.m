function [] = drawExperimentResult(path,xlabelValue,tag)
data= importdata(path);
x = data(1,:);
if(tag == 0)
    t_noP = data(2,:);
    t_bas = data(3,:);
    t_adv = data(4,:);
    w_noP = data(5,:);
    w_bas = data(6,:);
    w_adv = data(7,:);
    %size(a)

    plot(x,t_noP,'ro-',x,t_bas,'g*-',x,t_adv,'b+-');
    hold on;
    plot(x,w_noP,'cs:',x,w_bas,'mx:',x,w_adv,'kd:');

    %%plot(x,y1,'ro-')
    xlabel(xlabelValue);
    ylabel('Average accuracy score');

    legend('no privacy task score','basic task score', 'advanced task score', 'no privacy worker score','basic worker score', 'advanced worker score', 'Location', 'Best');
else
    tc_noP = data(8,:);
    tc_bas = data(9,:);
    tc_adv = data(10,:);
    plot(x,tc_noP,'ro-',x,tc_bas,'g*-',x,tc_adv,'b+-');

    %%plot(x,y1,'ro-')
    xlabel(xlabelValue);
    ylabel('Applying time cost');

    legend('no privacy time cost','basic time cost', 'advanced time cost', 'Location', 'Best');
end


