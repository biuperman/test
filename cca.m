clc,clear

A = xlsread();
B = xlsread();

R = corrcoef(A,B);

