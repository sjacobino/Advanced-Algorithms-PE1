import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sharif on 17/10/2016.
 */
public class ExactAlgorithm {
    private int numJobs;
    private  ArrayList<Job> sortedJobs;
    private HashMap<SubProblem, Integer> T;

    public ExactAlgorithm(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        sortedJobs = instance.getSortedJobs();
        T = new HashMap<>();
    }

    public Schedule getSchedule(){
        int kIndex = 1;
        int longestProcessingTime = 0;
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

        Schedule current = firstSchedule;
        for (int i = 1; i <= numJobs; i++){
            Job j = current.getJob();
            //System.out.println("job " + i + " length: " + j.getLength() + " due time: " + j.getDueTime());
            current = current.getNext();

        }

        //System.out.println("Ordered tardiness: " + schedule.getTardiness());

        // tardiness of complete schedule
        int tardiness = totalWeightedTardiness(firstSchedule, kIndex, 0, "");
        System.out.println("Total tardiness: " + tardiness);

        return null;
    }

    /**
     * @param s first schedule object in schedule
     * @param k index of longest job in s
     * @param t start time for schedule
     */
    public int totalWeightedTardiness(Schedule s, int k, int t, String x){
        SubProblem subProblem = new SubProblem(s, k, t);
        if (T.containsKey(subProblem)){
            //System.out.println("Reusing solution");
            return T.get(subProblem);
        }

        if(s == null){
            return 0;
        }

        //System.out.println("s height: " + s.getHeight() + " s depth: " +s.getDepth() + " kIndex: " + k + " t: "+ t + " x: " + x);

        if (s.getHeight() == 1) {
            Job j = s.getJob();
            int weight = 1;
            int result = weight * Math.max(0, t + j.getLength() - j.getDueTime());
            T.put(subProblem, result);
            return result;
        }

        Job jobK = sortedJobs.get(k);
        int smallestTardiness = -1;

        // iterate over delta options to find smallest tardiness
        for (int delta = 0; delta <= s.getHeight() - k; delta++) {
            // System.out.println("delta: " + delta);
            Schedule leftSchedule = null;
            Schedule firstLeftSchedule = null;
            Schedule rightSchedule = null;
            Schedule firstRightSchedule = null;

            int weightK = 1;
            int dueTimeK = jobK.getDueTime();
            int completionK = t;

            Schedule current = s;
            int leftLongestProcessingTime = 0;
            int leftKIndex = 1;

           //System.out.println("Left schedule");
            int leftCounter = 1;
            for (int i = 1; i <= k + delta; i ++) {
                Job j = current.getJob();
                completionK += j.getLength();

                if (i == k){
                    current = current.getNext();
                    continue;
                }

                //System.out.println("Job " + leftCounter + " length: " + j.getLength());
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

           /* if (leftSchedule != null) {
                System.out.println("left schedule depth: " + leftSchedule.getDepth());
            }*/

            int rightLongestProcessingTime = 0;
            int rightKIndex = 0;
            //System.out.println("Right schedule");
            int rightCounter = 1;
            for (int i = k + delta + 1; i <=  s.getHeight(); i++) {
                Job j = current.getJob();
                //System.out.println("Job " + rightCounter + " length: " + j.getLength());
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

            /*if (rightSchedule != null) {
                System.out.println("right schedule depth: " + rightSchedule.getDepth());
            }*/

            int tardinessLeft = totalWeightedTardiness(firstLeftSchedule, leftKIndex, t, x + "l" + delta);
            //System.out.println("Tardiness left " + tardinessLeft);
            int tardinessRight = totalWeightedTardiness(firstRightSchedule, rightKIndex, completionK, x + "r" + delta);
            //System.out.println("Tardiness right " + tardinessRight);

            int tardinessK = Math.max(0, completionK - dueTimeK);

            int totalTardiness = tardinessLeft + (weightK * tardinessK) + tardinessRight;
            //System.out.println("Total tardiness " + totalTardiness);

            if (smallestTardiness == -1){
                smallestTardiness = totalTardiness;
            }else {
                smallestTardiness = Math.min(smallestTardiness, totalTardiness);
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
