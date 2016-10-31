import com.sun.tools.internal.xjc.SchemaCache;
import com.sun.tools.javah.Util;

import java.util.ArrayList;

/**
 * Created by Sharif on 17/10/2016.
 */
public class ExactAlgorithm {
    private int numJobs;
    private  ArrayList<Job> sortedJobs;

    public ExactAlgorithm(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        sortedJobs = instance.getSortedJobs();
    }

    public Schedule getSchedule(){
        int kIndex = 0;
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

        /*Schedule current = firstSchedule;
        for (int i = 1; i < sortedJobs.size(); i++){
            Job j = current.getJob();
            System.out.println("job " + i + " length: " + j.getLength() + " due time: " + j.getDueTime());
            current = current.getNext();

        }*/


        // tardiness of complete schedule
        int tardiness = totalWeightedTardiness(firstSchedule, kIndex, 0);
        System.out.println("Total tardiness: " + tardiness);

        return null;
    }

    /**
     * @param s first schedule object in schedule
     * @param k index of longest job in s
     * @param t start time for schedule
     */
    public int totalWeightedTardiness(Schedule s, int k, int t){
        if(s == null){
            return 0;
        }

        //System.out.println("Schedule height: " + s.getHeight());
        //System.out.println("Index of longest job: " + k);
        //System.out.println("Starting time: " + t);

        //System.out.println("s height: " + s.getHeight() + " s depth: " +s.getDepth());

        // n = s.getHeight()
        if (s.getHeight() == 1) {
            Job j = s.getJob();
            int weight = 1;
            return weight * Math.max(0, t + j.getLength() - j.getDueTime());
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
            int leftKIndex = 0;

           // System.out.println("Left schedule");
            for (int i = 1; i <= k + delta; i ++) {
                if (i == k){
                    current = current.getNext();
                    continue;
                }

                Job j = current.getJob();

                //System.out.println("Job " + i + " length: " + j.getLength());
                leftSchedule = new Schedule(leftSchedule, i, j.getLength(), j.getDueTime());

                if (firstLeftSchedule == null) {
                    firstLeftSchedule = leftSchedule;
                }

                if (j.getLength() >= leftLongestProcessingTime) {
                    leftLongestProcessingTime = j.getLength();
                    leftKIndex = i;
                }

                completionK += j.getLength();

                current = current.getNext();

            }

           /* if (leftSchedule != null) {
                System.out.println("left schedule depth: " + leftSchedule.getDepth());
            }*/

            int rightLongestProcessingTime = 0;
            int rightKIndex = 0;
            //System.out.println("Right schedule");
            for (int i = k + delta + 1; i <=  s.getHeight(); i ++) {
                Job j = current.getJob();
                //System.out.println("Job " + i + " length: " + j.getLength());
                rightSchedule = new Schedule(rightSchedule, i, j.getLength(), j.getDueTime());

                if (firstRightSchedule == null) {
                    firstRightSchedule = rightSchedule;
                }

                if (j.getLength() >= rightLongestProcessingTime) {
                    rightLongestProcessingTime = j.getLength();
                    rightKIndex = i;
                }

                current = current.getNext();
            }

            /*if (rightSchedule != null) {
                System.out.println("right schedule depth: " + rightSchedule.getDepth());
            }*/

            int tardinessLeft = totalWeightedTardiness(firstLeftSchedule, leftKIndex, t);
            System.out.println("Tardiness left " + tardinessLeft);
            int tardinessRight = totalWeightedTardiness(firstRightSchedule, rightKIndex, completionK);
            System.out.println("Tardiness right " + tardinessRight);

            int tardinessK = Math.max(0, completionK - dueTimeK);

            int totalTardiness = tardinessLeft + (weightK * tardinessK) + tardinessRight;
            System.out.println("Total tardiness " + totalTardiness);

            if (smallestTardiness == -1){
                smallestTardiness = totalTardiness;
            }else {
                smallestTardiness = Math.min(smallestTardiness, totalTardiness);
            }
        }

        return smallestTardiness;
    }
}
