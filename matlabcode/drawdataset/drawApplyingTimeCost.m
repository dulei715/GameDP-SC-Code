function [] = drawApplyingTimeCost(path)
[num,txt,raw]=xlsread(path);
x = num(1,:);
%disp(x);
y1 = num(2,:);
%y2 = num(3,:);
%y3 = num(4,:);
%disp(y);
%plot(x,y1,'ro-',x,y2,'g*-',x,y3,'b+-')
plot(x,y1,'ro-')
