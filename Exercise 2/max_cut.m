resultfile = fopen('results3.txt', 'w')
W = [1, 25, 50, 75, 100];
TRS = [1, 25, 50, 75, 100];
P = {'05', '10'};
for n = 10 : 15 : 70
   for w = 1 : 5
       for p = {'05', '10'}
           for T = 1 : 5
                TRIALS = TRS(T)
                weight = W(w);

                file = fopen(sprintf('graphs/maxcut_%d_%s_%d_instance_00', n, p{1}, weight), 'r');
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

                tic;
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
                upperbound = double(objective)
                solvertime = sol.solvertime

                [Q, A] = eig(value(Y));
                B = Q * sqrt(A);
                B = B';
                lowerbound = 0;

                for t = 1 : TRIALS
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

                    max_cut_weight = 0;

                    for i = 1 : nodes
                        for j = i : nodes
                           if (ismember(i, S) & ~ ismember(j, S)) | ismember(j, S) & ~ ismember(i, S)
                               max_cut_weight = max_cut_weight + weights(i, j);
                           end
                        end
                    end

                    if max_cut_weight > lowerbound
                       lowerbound = max_cut_weight; 
                    end
                end
                
                fprintf(resultfile, '%d; %s; %d; %d; %s; %d; %s\n', n, p{1}, weight, TRIALS, toc, lowerbound, upperbound);
           end
       end
   end
end
fclose(resultfile);