import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class ComputeTardiness {
    public static ProblemInstance readInstance(String filename) {
        ProblemInstance instance = null;

        try {
            int numJobs = 0;
            double[][] jobs = null;

            Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
            if (sc.hasNextInt()) {
                numJobs = sc.nextInt();
                jobs = new double[numJobs][2];
                int nextJobID = 0;

                while (sc.hasNextInt() && nextJobID < numJobs) {
                    jobs[nextJobID][0] = sc.nextInt();
                    jobs[nextJobID][1] = sc.nextInt();
                    nextJobID++;
                }
            }
            sc.close();

            instance = new ProblemInstance(numJobs, jobs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return instance;
    }

    // reads a problem, and outputs the result of both greedy and best-first
    public static void main(String args[]) {
        Test.testTimeSize();
        if(true)
            return;


        int epsilon = Integer.parseInt(args[0]);
        ProblemInstance instance = readInstance(args[1]);

        Greedy greedy = new Greedy(instance);
        Schedule greedySchedule = greedy.getSchedule();
        //System.out.println("Greedy tardiness: " + (int) greedySchedule.getTardiness());

        //BestFirst bestFirst = new BestFirst(instance);
        //Schedule bestFirstSchedule = bestFirst.getSchedule();
        //System.out.println("Best first tardiness: " + bestFirstSchedule.getTardiness());

        ExactAlgorithm exactAlgorithm = new ExactAlgorithm(instance);
        Schedule exactSchedule = exactAlgorithm.getSchedule();
        //System.out.println("Exact tardiness: " + (int) exactSchedule.getTardiness());
        //exactSchedule.printComplete();

        ApproximationAlgorithm approximationAlgorithm = new ApproximationAlgorithm(instance);
        Schedule approximationSchedule = approximationAlgorithm.getSchedule(epsilon);
        //System.out.println("Approximated tardiness: " + (int) approximationSchedule.getTardiness());
        //approximationSchedule.printComplete();

        System.out.println((int) exactSchedule.getTardiness() + " " + (int) approximationSchedule.getTardiness());

    }
}
