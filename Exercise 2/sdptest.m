ops = sdpsettings('solver','sedumi');
A = sdpvar(4,4);
h = -(1/4) * (15 + A(4,1) - A(4,3) + A(1,3));
c = [diag(A) == ones(4,1), A >= 0];
sol = solvesdp(c, h, ops);
double(A)
double(-h)
sol.solvertime