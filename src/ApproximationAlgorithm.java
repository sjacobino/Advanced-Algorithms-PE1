import java.util.ArrayList;

/**
 * Created by Sharif on 15/11/2016.
 */
public class ApproximationAlgorithm {
    private int numJobs;
    private ArrayList<Job> sortedJobs;

    public ApproximationAlgorithm(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        sortedJobs = instance.getSortedJobs();
    }

    public Schedule getSchedule(){

        return null;
    }
}
