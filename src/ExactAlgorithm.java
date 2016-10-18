import java.util.ArrayList;

/**
 * Created by Sharif on 17/10/2016.
 */
public class ExactAlgorithm {
    private int numJobs;
    private int[][] jobs;
    private  ArrayList<Job> sortedJobs;

    public ExactAlgorithm(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        jobs = instance.getJobs();
        sortedJobs = instance.getSortedJobs();
    }

    public Schedule getSchedule(){
        int kIndex = 0;
        int longestProcessingTime = 0;
        Schedule schedule = null;
        Schedule firstSchedule = null;
        for(int i = 0; i < sortedJobs.size(); i++) {
            Job j = sortedJobs.get(i);
            if (j.getLength() >= longestProcessingTime){
                kIndex = i;
            }

            schedule = new Schedule(schedule, i, j.getLength(), j.getDueTime());

            if(firstSchedule == null) {
                firstSchedule = schedule;
            }

        }

        int tardiness = totalWeightedTardiness(firstSchedule, kIndex, 0);
        System.out.println("Total tardiness: " + tardiness);

        return null;
    }

    public int totalWeightedTardiness(Schedule s, int k, int t){
        Job jobK = sortedJobs.get(k);
        int smallestTardiness = Integer.MAX_VALUE;
        for (int delta = 0; delta <= s.getDepth() - k; delta++) {
            Schedule leftSchedule = new Schedule();
            Schedule rightSchedule = new Schedule();

            int weightK = 1;
            int dueTimeK = jobK.getDueTime();
            int completionK = t;

            Schedule current = s;
            int leftLongestProcessingTime = 0;
            int leftKIndex = 0;
            for (int i = 0; i <= k + delta; i ++) {
                Job j = current.getJob();
                if (j.getLength() < jobK.getLength()) {
                    leftSchedule = new Schedule(leftSchedule, i, j.getLength(), j.getDueTime());

                    if (j.getLength() >= leftLongestProcessingTime) {
                        leftLongestProcessingTime = j.getLength();
                        leftKIndex = i;
                    }

                }

                completionK += j.getLength();

                current = s.getNext();

            }

            int rightLongestProcessingTime = 0;
            int rightKIndex = 0;
            for (int i = k + delta + 1; i <=  s.getDepth(); i ++) {
                Job j = current.getJob();
                rightSchedule = new Schedule(rightSchedule, i, j.getLength(), j.getDueTime());

                if (j.getLength() >= rightLongestProcessingTime) {
                    rightLongestProcessingTime = j.getLength();
                    rightKIndex = i;
                }

                current = s.getNext();
            }

            int tardinessLeft = totalWeightedTardiness(leftSchedule, leftKIndex, t);
            int tardinessRight = totalWeightedTardiness(rightSchedule, rightKIndex, completionK);

            int tardinessK = Math.max(0, completionK - dueTimeK);

            int totalTardiness = tardinessLeft + weightK * tardinessK + tardinessRight;

            smallestTardiness = Math.min(smallestTardiness, totalTardiness);
        }

        return smallestTardiness;
    }
}
