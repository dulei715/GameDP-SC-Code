function [cost,CMatrix]=Assignment(C,ismin) 
% Assignment problem solved by hungarian method. 
% 
% input: 
% C         - ϵ�����󣬿�����Ӧworkers��tasks��Ŀ��ͬ������ 
% ismin     - 1��ʾ��С�����⣬0��ʾ������� 
% ouput: 
% cost      - ���ջ��Ѵ��� 
% CMatrix   - ��Ӧ��ƥ�����Ԫ��1����λ��c_{ij}��ʾj task����� i worker�� 
% 
  
[m,n]=size(C); 
maxC = max(C(:));
if ismin==0 
    C=maxC-C; 
end 
 
%workes ��tasks��Ŀ����ͬ 
if m<n 
    C=[C;zeros(n-m,n)]; 
elseif m>n 
    C=[C zeros(m,m-n)]; 
end 
copyC=C; 
d=max(m,n);% ����ϵ�������ά�� 
C=C-repmat(min(C,[],2),1,d); 
C=C-repmat(min(C,[],1),d,1); 
 
%% ����һ 
% while 1 
%     A=int8((C==0)); 
%     nIZeros=0;  % ����0Ԫ�صĸ��� 
%     while 1 
%         r=sum(A==1,2); % ÿһ��0Ԫ�صĸ��� 
%         [~,idr]=find(r'==1);%�ҵ�ֻ��һ��0Ԫ�ص��� 
%         if ~isempty(idr) % ����ҵ��������� 
%             tr=A(idr(1),:); 
%             [~,idc]=find(tr==1);%�ҵ�0Ԫ�������� 
%             A(idr(1),idc)=2;%��ע����Ԫ�� 
%             tc=A(:,idc); 
%             tc(idr(1))=2; 
%             [~,idr]=find(tc'==1);%�ҵ�����0Ԫ�������е�����0Ԫ�� 
%             A(idr,idc)=-2;%��������0Ԫ�������е�����0Ԫ�� 
%             nIZeros=nIZeros+1; 
%         else 
%             c=sum(A==1,1); % ÿһ��0Ԫ�صĸ��� 
%             [~,idc]=find(c==1);%�ҵ�ֻ����һ��0Ԫ�ص��� 
%             if ~isempty(idc)% �ҵ��������� 
%                 tc=A(:,idc(1)); 
%                 [~,idr]=find(tc'==1);%0Ԫ�����ڵ��� 
%                 A(idr,idc(1))=2;%��ע����0Ԫ�� 
%                 tr=A(idr,:); 
%                 tr(idc(1))=2; 
%                 [~,idc]=find(tr==1);%����0Ԫ�������е�����0Ԫ�� 
%                 A(idr,idc)=-2;%��������0Ԫ�������е�����0Ԫ�� 
%                 nIZeros=nIZeros+1; 
%             else 
%                 break; 
%             end 
%         end 
%     end 
%  
%     if nIZeros==d 
%         %�������Ž� 
%         CMatrix=(A==2); 
%          
%         if ismin==1 
%             cost=sum(copyC(:).*CMatrix(:)); 
%         else 
%             cost = sum((maxC-copyC(:)).*CMatrix(:)); 
%         end 
%         CMatrix=CMatrix(1:m,1:n); 
%         break;%�ҵ�d������0Ԫ�أ�������ѭ�� 
%     else% ����0Ԫ�ظ������㣬��Ҫ�Ҹ�0���� 
%         r=sum(A==2,2); 
%         [~,idr]=find(r'==0);%�����ж���0Ԫ�ص��� 
%         idrr=idr; 
%         idcc=[]; 
%         while 1 
%             tr=A(idrr,:); 
%             [~,idc]=find(tr==-2);%��������0Ԫ�ص����л�����0Ԫ�������� 
%             if isempty(idc) 
%                 break;
%             end 
%             tc=A(:,unique(idc)); 
%             [idrr,~]=find(tc==2);%��Щ���б�ע��0Ԫ�������� 
%             idr=[idr,idrr']; 
%             sizeidc_xy = size(idc);
%             sizeidc = sizeidc_xy(1) * sizeidc_xy(2);
%             idc = reshape(idc,1,sizeidc);
%             idcc=[idcc,idc]; 
%         end 
%         idry=1:d; 
%         idry(idr)=[];%��0�����ڵ������� 
%         TempC=C;%�洢�Ǹ���Ԫ�� 
%         TempC(idry,:)=[]; 
%         TempC(:,idcc)=[]; 
%         minUnOverlap=min(TempC(:)); 
%         %����ϵ������ 
%         C=C-minUnOverlap; 
%         C(idry,:)=C(idry,:)+minUnOverlap; 
%         C(:,idcc)=C(:,idcc)+minUnOverlap; 
%     end 
% end 
%% ������ 
while 1 
    CMatrix=zeros(d); 
    nLines=0; 
    A=(C==0); 
    idx=[]; 
    idy=[]; 
    sr=[]; 
    sc=[]; 
    while 1 
        r=sum(A,2); 
        c=sum(A,1); 
        r(sr)=0; 
        c(sc)=0; 
        trc=[r(:);c(:)]; 
        [trc,idtrc]=sort(trc,1,'ascend'); 
        [~,idn0]=find(trc'>0); 
        if ~isempty(idn0) 
            id=idtrc(idn0(1)); 
            if id>d 
                tc=A(:,id-d); 
                [~,idr]=find(tc'==1); 
                A(idr(1),:)=0;  
                nLines=nLines+1; 
                idy=[idy,idr(1)]; 
                CMatrix(idr(1),id-d)=1; 
                sc=[sc,id-d]; 
            else 
                tr=A(id,:); 
                [~,idc]=find(tr==1); 
                A(:,idc(1))=0; 
                nLines=nLines+1; 
                idx=[idx,idc(1)]; 
                CMatrix(id,idc(1))=1; 
                sr=[sr,id]; 
            end  
        else 
            break; 
        end 
    end 
    if nLines==d 
        if ismin 
            cost=sum(copyC(:).*CMatrix(:)); 
        else 
            cost=sum((maxC-copyC(:)).*CMatrix(:)); 
        end 
        CMatrix=CMatrix(1:m,1:n); 
        break; 
    else 
        tempC=C; 
        tempC(idy,:)=[]; 
        tempC(:,idx)=[]; 
        minUnOverlap=min(tempC(:)); 
        C=C-minUnOverlap; 
        C(idy,:)=C(idy,:)+minUnOverlap; 
        C(:,idx)=C(:,idx)+minUnOverlap; 
    end 
end 
 
end 