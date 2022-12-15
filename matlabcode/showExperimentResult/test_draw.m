function test_draw()
x=[1:1:10];
y1 = x*2;
y2 = x.*x;
fig = figure;
hold on;
plot(x,y1,'ro-');
plot(x,y2,'g*-');
xlabel('x');
ylabel('y');
%h = legend('ucs-p', 'dcs-p','gts-p','ucs-np','dcs-np', 'gts-np', 'grs-np','Location','Best');
%legend('linear', 'square','Location','Best');
%legend('linear', 'square','Location','NorthOutside');
legend('linear', 'square','Location','BestOutside');
