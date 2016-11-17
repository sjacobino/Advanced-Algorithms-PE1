import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sharif on 17/10/2016.
 */
public class ExactAlgorithm {
    private int numJobs;
    private  ArrayList<Job> sortedJobs;
    private HashMap<SubProblem, TardinessAndSchedule> T;

    public ExactAlgorithm(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        sortedJobs = instance.getSortedJobs();
        T = new HashMap<>();
    }

    public Schedule getSchedule(){
        int kIndex = 1;
        double longestProcessingTime = 0;
        Schedule schedule = null;

        // Find job k with largest processing time
        for(int i = 0; i < numJobs; i++) {
            Job j = sortedJobs.get(i);

            if (j.getLength() >= longestProcessingTime){
                longestProcessingTime = j.getLength();
                kIndex = i + 1;
            }

            // Construct initial schedule
            schedule = new Schedule(schedule, i + 1, j.getLength(), j.getDueTime());
        }

        // tardiness of complete schedule
        TardinessAndSchedule tardinessAndSchedule = totalWeightedTardiness(schedule.firstSchedule(), kIndex, 0);
        return tardinessAndSchedule.getSchedule().lastSchedule();
    }

    /**
     * @param s first schedule object in schedule
     * @param k index of longest job in s
     * @param t start time for schedule
     */
    public TardinessAndSchedule totalWeightedTardiness(Schedule s, int k, int t){
        // Base case: s is empty
        if(s == null){
            return new TardinessAndSchedule(0, null);
        }

        SubProblem subProblem = new SubProblem(s, k, t);
        if (T.containsKey(subProblem)){
            return T.get(subProblem);
        }

        // Base case: 1 schedule
        if (s.getHeight() == 1) {
            Job j = s.getJob();
            double result = Math.max(0, t + j.getLength() - j.getDueTime());
            TardinessAndSchedule resultTardinessAndSchedule = new TardinessAndSchedule(result, s);
            T.put(subProblem, resultTardinessAndSchedule);
            return resultTardinessAndSchedule;
        }

        //
        Schedule scheduleK = s;
        int scheduleKIndex = k;
        while (scheduleKIndex > 1){
            scheduleK = scheduleK.getNext();
            scheduleKIndex--;
        }
        Job jobK = scheduleK.getJob();

        double smallestTardiness = -1;
        Schedule bestSchedule = null;

        // iterate over delta options to find smallest tardiness
        for (int delta = 0; delta <= s.getHeight() - k; delta++) {
            int completionK = t;
            int kId = 1;
            Schedule current = s;

            Schedule leftSchedule = null;

            double leftLongestProcessingTime = 0;
            int leftKIndex = 1;
            int leftCounter = 1;

            // Construct left schedule
            for (int i = 1; i <= k + delta; i ++) {
                Job j = current.getJob();
                completionK += j.getLength();

                // Left does not contain job K
                if (i == k){
                    kId = current.getOriginalID();
                    current = current.getNext();
                    continue;
                }

                leftSchedule = new Schedule(leftSchedule, leftCounter, current.getOriginalID(), j.getLength(), j.getDueTime());

                if (j.getLength() >= leftLongestProcessingTime) {
                    leftLongestProcessingTime = j.getLength();
                    leftKIndex = leftCounter;
                }

                current = current.getNext();
                leftCounter++;
            }

            Schedule rightSchedule = null;

            double rightLongestProcessingTime = 0;
            int rightKIndex = 1;
            int rightCounter = 1;

            // Construct right schedule
            for (int i = k + delta + 1; i <=  s.getHeight(); i++) {
                Job j = current.getJob();
                rightSchedule = new Schedule(rightSchedule, rightCounter, current.getOriginalID(), j.getLength(), j.getDueTime());

                if (j.getLength() >= rightLongestProcessingTime) {
                    rightLongestProcessingTime = j.getLength();
                    rightKIndex = rightCounter;
                }

                current = current.getNext();
                rightCounter++;
            }

            // Calculate tardiness of left and right schedules
            TardinessAndSchedule leftTardinessAndSchedule = totalWeightedTardiness((leftSchedule == null)? null: leftSchedule.firstSchedule(), leftKIndex, t);
            TardinessAndSchedule rightTardinessAndSchedule = totalWeightedTardiness((rightSchedule == null)? null: rightSchedule.firstSchedule(), rightKIndex, completionK);

            double tardinessK = Math.max(0, completionK - jobK.getDueTime());

            double totalTardiness = leftTardinessAndSchedule.getTardiness() + tardinessK + rightTardinessAndSchedule.getTardiness();

            if (smallestTardiness == -1){
                smallestTardiness = totalTardiness;
                // Merge left, k and right to retrieve the best schedule
                bestSchedule = Schedule.mergeSchedules(leftTardinessAndSchedule.getSchedule(), jobK, kId, rightTardinessAndSchedule.getSchedule());

            }else {
                if (totalTardiness < smallestTardiness) {
                    smallestTardiness = totalTardiness;
                    // Merge left, k and right to retrieve the best schedule
                    bestSchedule = Schedule.mergeSchedules(leftTardinessAndSchedule.getSchedule(), jobK, kId, rightTardinessAndSchedule.getSchedule());
                }
            }
        }

        T.put(subProblem, new TardinessAndSchedule(smallestTardiness, bestSchedule));
        return new TardinessAndSchedule(smallestTardiness, bestSchedule);
    }
}
