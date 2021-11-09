function y = drawRelation(m, n, p)
k = 1:m;
%xx = (n.*k-p.*k.*k)/m;
y = zeros(size(k));
for i = 1:m
    upper = floor(n/k(i)/p);
    for j = 1:upper
        y(i) = y(i) + rTime((n-k(i)*floor(p*j))*k(i),m);   
    end
end
plot(k, y);