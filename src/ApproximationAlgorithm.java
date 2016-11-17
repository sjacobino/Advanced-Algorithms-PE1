import java.util.ArrayList;

/**
 * Created by Sharif on 15/11/2016.
 */
public class ApproximationAlgorithm {
    private ProblemInstance problemInstance;

    public ApproximationAlgorithm(ProblemInstance instance) {
        this.problemInstance = instance;
    }

    public Schedule getSchedule(double epsilon){
        // Get EDD schedule from greedy algorithm
        Greedy greedy = new Greedy(problemInstance);
        Schedule greedySchedule = greedy.getSchedule();

        // Find t max
        double Tmax = 0;
        Schedule current = greedySchedule.lastSchedule();
        while (current.getDepth() > 1) {
            if (current.actualTardiness() > Tmax) {
                Tmax = current.actualTardiness();
            }
            current = current.getPrevious();
        }

        if (Tmax == 0) {
            // In this case greedy schedule is optimal
            return greedySchedule;
        }

        // Scale the instance using epsion and Tmax
        ProblemInstance scaledInstance = problemInstance.scale(epsilon, Tmax);

        // Find optimal schedule for the scaled instance
        ExactAlgorithm scaledExactAlgorithm = new ExactAlgorithm(scaledInstance);
        Schedule scaledSchedule = scaledExactAlgorithm.getSchedule();

        // Reconstruct the schedule using the original jobs
        ArrayList<Job> sortedJobs = problemInstance.getSortedJobs();
        current = scaledSchedule.firstSchedule();
        Job firstJob = sortedJobs.get(current.getOriginalID() - 1);
        Schedule descaledSchedule = new Schedule(null, 0, current.getOriginalID(), firstJob.getLength(), firstJob.getDueTime());

        while (current.getNext() != null) {
            current = current.getNext();
            Job j = sortedJobs.get(current.getOriginalID() - 1);
            descaledSchedule = new Schedule(descaledSchedule, 0, current.getOriginalID(), j.getLength(), j.getDueTime());
        }

        return descaledSchedule.lastSchedule();
    }
}
