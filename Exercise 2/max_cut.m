%file = fopen('test', 'r');
file = fopen('maxcut_4_10_1_instance_02', 'r');
nodes = fscanf(file, '%d', 1);
edges = transp(fscanf(file, '%d %d %d', [3 Inf]));
fclose(file);

weights = zeros(nodes, nodes);
num_edges = size(edges);
num_edges = num_edges(1,1);

for i = 1 : num_edges
    weights(edges(i, 1) + 1, edges(i, 2) + 1) = edges(i, 3); % matlab arrays are 1-index
    weights(edges(i, 2) + 1, edges(i, 1) + 1) = edges(i, 3);
end

ops = sdpsettings('solver','sedumi');
Y = sdpvar(nodes, nodes);
objective = 0;
for i = 1 : nodes
    for j = i + 1 : nodes
        objective = objective + weights(i, j) * (1 - Y(i, j));
    end
end

objective = (1/2) * objective;

constraints = [diag(Y) == ones(nodes, 1), Y >= 0];
    
sol = solvesdp(constraints, -objective, ops);
double(Y)
double(objective)
sol.solvertime

[Q, A] = eig(value(Y));
B = Q * sqrt(A);
B = B';

r = rand(nodes, 1);
r = r / norm(r);
S = [];

check = r' * double(Y) * r > 0;

for i = 1 : nodes
    v = B(: , i);
    if v' * r > 0
        S = [S i];
    end
end