clc,clear

%10个人，形成28*10矩阵，excel处理成28*1矩阵
M = 10;
J = 28;% 红葡萄酒的J = 27;白葡萄酒的J = 28
num = xlsread(filename);%---
Rj = num;
Ra = average(Rj);
Q = sum[(Rj - Ra).*(Rj - Ra)];
W = Q/[1/12*M*M(J*J*J-J)]

