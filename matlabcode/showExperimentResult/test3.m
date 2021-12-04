function y = test3()
a=[1,2,3,4,5];
b=[4,5,6,7,8];
plot(a,b,'ro-','LineWidth',2)
set(gca,'xtick',1:0.5:3.5);
% for i=1:length(a)
%     cstr{i}=sprintf('(%g,%g)',a(i),b(i)); 
% end
% set(gca,'xticklabel',cstr);