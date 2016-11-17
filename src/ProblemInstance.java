import java.util.ArrayList;
import java.util.Collections;

public class ProblemInstance {
    private int numJobs;
    private double[][] jobs; //size = [num_jobs][2], for every job [0] is the length, [1] is the due time
    private ArrayList<Job> sortedJobs;


    public ProblemInstance(int numJobs, double[][] jobs) {
        this.numJobs = numJobs;
        this.jobs = jobs;

        this.sortedJobs = new ArrayList<Job>();
        //this.sortedJobs.add(new Job(-1, -1));
        for (int i = 0; i < jobs.length; i++) {
            this.sortedJobs.add(new Job(jobs[i ][0], jobs[i][1]));
        }
        Collections.sort(this.sortedJobs);
    }

    public int getNumJobs() {
        return numJobs;
    }

    public double[][] getJobs() {
        return jobs;
    }

    public ArrayList<Job> getSortedJobs() {
        return sortedJobs;
    }

    public ProblemInstance scale(double epsilon, double Tmax) {
        double K = ((2 * epsilon) / (numJobs * (numJobs + 1))) * Tmax;
        double[][] scaledJobs = new double[numJobs][2];
        for (int i = 0; i < numJobs; i++) {
            double q = (Math.floor((jobs[i][0]) / K));
            double d = (jobs[i][1]) / K;
            scaledJobs[i][0] = q;
            scaledJobs[i][1] = d;
        }

        return new ProblemInstance(numJobs, scaledJobs);
    }

}
