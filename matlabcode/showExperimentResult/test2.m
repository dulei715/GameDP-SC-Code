function y = test2(esx, esy, ka, kb)
x = [ka:0.01:kb];
y1 = esx*esy/4./(esx+esy).*(exp(-x.*esy)+exp(-x.*esx))+esx*esy/4./(esy-esx).*(exp(-x.*esx)-exp(-x.*esy));
y2 = esy/2.*exp(-x.*esy);
f = figure;
hold on;
plot(x,y1,x,y2);
%imshow(f,'border','tight','initialmagnification','fit');
%saveas(f,'E:\test2\hh','pdf');
%export_fig(f,'-eps','E:\test2\hh.eps');
export_fig(f , '-pdf' , '-r256' , '-transparent' , 'E:\test2\Test3')