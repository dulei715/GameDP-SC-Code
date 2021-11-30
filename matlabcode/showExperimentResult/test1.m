function y = test1(value)
x1 = [0.01:0.01:value-0.01];
x2 = [value+0.01:0.01:10];
y1 = 1./(x1-value).*log(x1./value);
y2 = 1./(x2-value).*log(x2./value);
f = figure;
hold on;
plot(x1,y1,x2,y2);
