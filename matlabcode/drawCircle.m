function drawCircle(point, r)
theta = 0:0.01:2*pi;
plot(point(1)+r*cos(theta), point(2)+r*sin(theta));
hold on;
scatter(point(1), point(2), 'filled');
