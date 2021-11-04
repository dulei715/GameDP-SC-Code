function [] = readPoint(path,scale, wtscale)

fid = fopen(path,'r');
line = str2num(fgetl(fid));
dataLen = floor(line/scale);
workerLen = floor(dataLen/wtscale);
taskLen = dataLen - workerLen;
workerX = zeros(1,workerLen);
workerY = zeros(1,workerLen);
taskX = zeros(1,taskLen);
taskY = zeros(1,taskLen);
i = 1;
while (feof(fid) == 0)
    line = fgetl(fid);
    linedata = strsplit(line);
    %disp(linedata);
    if (mod(i,scale) ~= 0)
         i = i + 1;
        continue;
    end
    j = i/scale;
    k = floor(j/wtscale);
    m = mod(j,wtscale);
    if (m == 0)
        workerX(k) = str2num(cell2mat(linedata(1)));
        workerY(k) = str2num(cell2mat(linedata(2)));
    else
        taskX(k*(wtscale-1)+m) = str2num(cell2mat(linedata(1)));
        taskY(k*(wtscale-1)+m) = str2num(cell2mat(linedata(2)));
    end
    
    i = i + 1;
end
%disp(x)
%disp(y)
scatter(workerX,workerY,5,'r','filled');
hold on;
scatter(taskX,taskY,5,'b','filled');


