import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sharif on 17/10/2016.
 */
public class ExactAlgorithm {
    private int numJobs;
    private  ArrayList<Job> sortedJobs;
    private HashMap<SubProblem, Double> T;

    public ExactAlgorithm(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        sortedJobs = instance.getSortedJobs();
        T = new HashMap<>();
    }

    public Schedule getSchedule(){
        int kIndex = 1;
        double longestProcessingTime = 0;
        Schedule schedule = null;
        Schedule firstSchedule = null;

        // Find job k with largest processing time
        for(int i = 1; i <= numJobs; i++) {
            Job j = sortedJobs.get(i);
            if (j.getLength() >= longestProcessingTime){
                longestProcessingTime = j.getLength();
                kIndex = i;
            }

            // Construct initial schedule
            schedule = new Schedule(schedule, i, j.getLength(), j.getDueTime());

            if(firstSchedule == null) {
                // first schedule for forward iteration
                firstSchedule = schedule;
            }

        }

        // tardiness of complete schedule
        double tardiness = totalWeightedTardiness(firstSchedule, kIndex, 0);
        System.out.println("Total tardiness: " + tardiness);

        return null;
    }

    /**
     * @param s first schedule object in schedule
     * @param k index of longest job in s
     * @param t start time for schedule
     */
    public double totalWeightedTardiness(Schedule s, int k, int t){
        SubProblem subProblem = new SubProblem(s, k, t);
        if (T.containsKey(subProblem)){
            return T.get(subProblem);
        }

        if(s == null){
            return 0;
        }

        if (s.getHeight() == 1) {
            Job j = s.getJob();
            int weight = 1;
            double result = weight * Math.max(0, t + j.getLength() - j.getDueTime());
            T.put(subProblem, result);
            return result;
        }

        Schedule scheduleK = s;
        int scheduleKIndex = k;
        while (scheduleKIndex > 1){
            scheduleK = scheduleK.getNext();
            scheduleKIndex--;
        }
        Job jobK = scheduleK.getJob();
        int weightK = 1;
        double dueTimeK = jobK.getDueTime();

        double smallestTardiness = -1;
        Schedule bestSchedule = null;

        // iterate over delta options to find smallest tardiness
        for (int delta = 0; delta <= s.getHeight() - k; delta++) {
            int completionK = t;
            Schedule current = s;

            Schedule leftSchedule = null;
            Schedule firstLeftSchedule = null;

            double leftLongestProcessingTime = 0;
            int leftKIndex = 1;
            int leftCounter = 1;

            for (int i = 1; i <= k + delta; i ++) {
                Job j = current.getJob();
                completionK += j.getLength();

                if (i == k){
                    current = current.getNext();
                    continue;
                }

                leftSchedule = new Schedule(leftSchedule, leftCounter, j.getLength(), j.getDueTime());

                if (firstLeftSchedule == null) {
                    firstLeftSchedule = leftSchedule;
                }

                if (j.getLength() >= leftLongestProcessingTime) {
                    leftLongestProcessingTime = j.getLength();
                    leftKIndex = leftCounter;
                }

                current = current.getNext();
                leftCounter++;
            }

            Schedule rightSchedule = null;
            Schedule firstRightSchedule = null;

            double rightLongestProcessingTime = 0;
            int rightKIndex = 1;
            int rightCounter = 1;

            for (int i = k + delta + 1; i <=  s.getHeight(); i++) {
                Job j = current.getJob();
                rightSchedule = new Schedule(rightSchedule, rightCounter, j.getLength(), j.getDueTime());

                if (firstRightSchedule == null) {
                    firstRightSchedule = rightSchedule;
                }

                if (j.getLength() >= rightLongestProcessingTime) {
                    rightLongestProcessingTime = j.getLength();
                    rightKIndex = rightCounter;
                }

                current = current.getNext();
                rightCounter++;
            }

            double tardinessLeft = totalWeightedTardiness(firstLeftSchedule, leftKIndex, t);
            double tardinessRight = totalWeightedTardiness(firstRightSchedule, rightKIndex, completionK);

            double tardinessK = Math.max(0, completionK - dueTimeK);

            double totalTardiness = tardinessLeft + (weightK * tardinessK) + tardinessRight;

            if (smallestTardiness == -1){
                smallestTardiness = totalTardiness;
                bestSchedule = Schedule.mergeSchedules(leftSchedule, jobK, rightSchedule);

            }else {
                if (totalTardiness < smallestTardiness) {
                    smallestTardiness = totalTardiness;
                    bestSchedule = Schedule.mergeSchedules(leftSchedule, jobK, rightSchedule);
                }
            }
        }

        T.put(subProblem, smallestTardiness);
        return smallestTardiness;
    }

    public void nullPointer(){
        String a = null;
        a.length();
    }
}
