import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Sharif on 18/11/2016.
 */
public class Test {

    public static void testTimeSize() {
        System.out.println("Start running");
        String path = "/Users/Sharif/Documents/GitHub/Advanced Algorithms PE 1/instances/random_RDD=%1$.1f_TF=%2$.1f_#%3$d.dat";

        String folder = "/Users/Sharif/Documents/GitHub/Advanced Algorithms PE 1/results/";

        String greedyTimeSize = folder + "greedy_time_size_RDD=1.0_TF=0.2.txt";
        String bestFitTimeSize = folder + "best_fit_time_size_RDD=1.0_TF=0.2.txt";
        String exactTimeSize = folder + "exact_time_size_RDD=1.0_TF=0.2.txt";
        String approximationTimeSize = folder + "approximation_time_size_RDD=1.0_TF=0.2.txt";

        /*String greedyTimeRDD = folder + "greedy_time_rdd.txt";
        String bestFitTimeRDD = folder + "best_fit_time_rdd.txt";
        String exactTimeRDD = folder + "exact_time_rdd.txt";
        String approximationTimeRDD = folder + "approximation_time_rdd.txt";*/

        /*String greedyTimeTF = folder + "greedy_time_tf.txt";
        String bestFitTimeTF = folder + "best_fit_time_tf.txt";
        String exactTimeTF = folder + "exact_time_tf.txt";
        String approximationTimeTF = folder + "approximation_time_tf.txt";*/

        FileWriter greedyTimeSizeWriter = null;
        FileWriter bestFitTimeSizeWriter = null;
        FileWriter exactTimeSizeWriter = null;
        FileWriter approximationTimeSizeWriter = null;

        //FileWriter greedyTimeRDDWriter = null;
        //FileWriter bestFitTimeRDDWriter = null;
        //FileWriter exactTimeRDDWriter = null;
        //FileWriter approximationTimeRDDWriter = null;

        //FileWriter greedyTimeTFWriter = null;
        //FileWriter bestFitTimeTFWriter = null;
        //FileWriter exactTimeTFWriter = null;
        //FileWriter approximationTimeTFWriter = null;

        try {
            greedyTimeSizeWriter = new FileWriter(greedyTimeSize);
            bestFitTimeSizeWriter = new FileWriter(bestFitTimeSize);
            exactTimeSizeWriter = new FileWriter(exactTimeSize);
            approximationTimeSizeWriter = new FileWriter(approximationTimeSize);

            //greedyTimeRDDWriter = new FileWriter(greedyTimeRDD);
            //bestFitTimeRDDWriter = new FileWriter(bestFitTimeRDD);
            //exactTimeRDDWriter = new FileWriter(exactTimeRDD);
            //approximationTimeRDDWriter = new FileWriter(approximationTimeRDD);

            //greedyTimeTFWriter = new FileWriter(greedyTimeTF);
            //bestFitTimeTFWriter = new FileWriter(bestFitTimeTF);
            //exactTimeTFWriter = new FileWriter(exactTimeTF);
            //approximationTimeTFWriter = new FileWriter(approximationTimeTF);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean bf = true;
        boolean exact = true;
        boolean approx = true;

        double RDD = 1.0;
        double TF = 0.2;
        //int n = 10; //50;
        for (int n = 5; n <= 100; n += 5) {
            System.out.println("n = " + n);
            ProblemInstance problemInstance = ComputeTardiness.readInstance(String.format(path, RDD, TF, n));

            Long start;

            start = System.currentTimeMillis();
            Greedy greedy = new Greedy(problemInstance);
            greedy.getSchedule();
            long durationGreedy = System.currentTimeMillis() - start;

            Long durationBestFit = -1l;
            try {
                if (bf) {
                    start = System.currentTimeMillis();
                    BestFirst bestFirst = new BestFirst(problemInstance);
                    bestFirst.getSchedule();
                    durationBestFit = System.currentTimeMillis() - start;
                }
            } catch (OutOfMemoryError e) {
                System.out.println("BF Out of memory");
                bf = false;
            }

            long durationExact = -1l;
            try {
                if (exact) {
                    start = System.currentTimeMillis();
                    ExactAlgorithm exactAlgorithm = new ExactAlgorithm(problemInstance);
                    exactAlgorithm.getSchedule();
                    durationExact = System.currentTimeMillis() - start;
                }
            } catch (OutOfMemoryError e) {
                System.out.println("Exact Out of memory");
                exact = false;
            }

            long durationApproximation = -1l;
            try {
                if (exact) {
                    start = System.currentTimeMillis();
                    ApproximationAlgorithm approximationAlgorithm = new ApproximationAlgorithm(problemInstance);
                    approximationAlgorithm.getSchedule(3);
                    durationApproximation = System.currentTimeMillis() - start;
                }
            } catch (OutOfMemoryError e) {
                System.out.println("Approx Out of memory");
                exact = false;
            }

            try {
                greedyTimeSizeWriter.write(durationGreedy + " " + n + "\n");
                bestFitTimeSizeWriter.write(durationBestFit + " " + n + "\n");
                exactTimeSizeWriter.write(durationExact + " " + n + "\n");
                approximationTimeSizeWriter.write(durationApproximation + " " + n + "\n");

                //greedyTimeRDDWriter.write(durationGreedy + " " + RDD + "\n");
                //bestFitTimeRDDWriter.write(durationBestFit + " " + RDD + "\n");
                //exactTimeRDDWriter.write(durationExact + " " + RDD + "\n");
                //approximationTimeRDDWriter.write(durationApproximation + " " + RDD + "\n");

                //greedyTimeTFWriter.write(durationGreedy + " " + TF + "\n");
                //bestFitTimeTFWriter.write(durationBestFit + " " + TF + "\n");
                //exactTimeTFWriter.write(durationExact + " " + TF + "\n");
                //approximationTimeTFWriter.write(durationApproximation + " " + TF + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            greedyTimeSizeWriter.close();
            bestFitTimeSizeWriter.close();
            exactTimeSizeWriter.close();
            approximationTimeSizeWriter.close();

            //greedyTimeRDDWriter.close();
            //bestFitTimeRDDWriter.close();
            //exactTimeRDDWriter.close();
            //approximationTimeRDDWriter.close();

            //greedyTimeTFWriter.close();
            //bestFitTimeTFWriter.close();
            //exactTimeTFWriter.close();
            //approximationTimeTFWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done running!!!!");
    }

    public static void testAccuracy() {
        System.out.println("Start running");
        String path = "/Users/Sharif/Documents/GitHub/Advanced Algorithms PE 1/instances/random_RDD=%1$.1f_TF=%2$.1f_#%3$d.dat";

        String folder = "/Users/Sharif/Documents/GitHub/Advanced Algorithms PE 1/results/";

        String tardinessFile = folder + "tardiness_RDD=0.6_TF=0.6_all.txt";

        FileWriter tardinessWriter;
        try {
            tardinessWriter = new FileWriter(tardinessFile);
            double RDD = 0.6;
            double TF = 0.6;

            boolean exact = true;
            boolean approx1 = true;
            boolean approx5 = true;

            for (int n = 5; n <= 100; n += 5) {
                System.out.println("n: " + n);
                ProblemInstance problemInstance = ComputeTardiness.readInstance(String.format(path, RDD, TF, n));

                System.out.println("greedy");
                Greedy greedy = new Greedy(problemInstance);
                int greedyTardiness = (int) greedy.getSchedule().lastSchedule().getTardiness();


                int exactTardiness = -1;
                try {
                    if (exact) {
                        System.out.println("exact");
                        ExactAlgorithm exactAlgorithm = new ExactAlgorithm(problemInstance);
                        exactTardiness = (int) exactAlgorithm.getSchedule().lastSchedule().getTardiness();
                    }
                } catch (OutOfMemoryError e) {
                    System.out.println("Exact Out of memory");
                    exact = false;
                }

                int approx1Tardiness = -1;
                try {
                    if (approx1) {
                        System.out.println("approx 1");
                        ApproximationAlgorithm approximationAlgorithm = new ApproximationAlgorithm(problemInstance);
                        approx1Tardiness = (int) approximationAlgorithm.getSchedule(1).lastSchedule().getTardiness();
                    }
                } catch (OutOfMemoryError e) {
                    System.out.println("Approx 1 Out of memory");
                    approx1 = false;
                }

                int approx5Tardiness = -1;
                try {
                    if (approx5) {
                        System.out.println("approx 5");
                        ApproximationAlgorithm approximationAlgorithm = new ApproximationAlgorithm(problemInstance);
                        approx5Tardiness = (int) approximationAlgorithm.getSchedule(5).lastSchedule().getTardiness();
                    }
                } catch (OutOfMemoryError e) {
                    System.out.println("Approx 5 Out of memory");
                    approx5 = false;
                }

                tardinessWriter.write(n + " " + greedyTardiness + " " + exactTardiness + " " + approx1Tardiness + " " + approx5Tardiness + "\n");
            }

            tardinessWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done running!!!!");
    }
}
