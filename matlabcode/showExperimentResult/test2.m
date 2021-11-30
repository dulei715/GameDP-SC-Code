function y = test2(esx, esy, ka, kb)
x = [ka:0.01:kb];
y1 = esx*esy/4./(esx+esy).*(exp(-x.*esy)+exp(-x.*esx))+esx*esy/4./(esy-esx).*(exp(-x.*esx)-exp(-x.*esy));
y2 = esy/2.*exp(-x.*esy);
f = figure;
hold on;
plot(x,y1,x,y2);
