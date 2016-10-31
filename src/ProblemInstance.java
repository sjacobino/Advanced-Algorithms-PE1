import java.util.ArrayList;
import java.util.Collections;

public class ProblemInstance {
	private int numJobs;
	private int[][] jobs; //size = [num_jobs][2], for every job [0] is the length, [1] is the due time
	private ArrayList<Job> sortedJobs;
	
	public ProblemInstance(int numJobs, int[][] jobs) {
		this.numJobs = numJobs;
		this.jobs = jobs;

		this.sortedJobs = new ArrayList<Job>();
		this.sortedJobs.add(new Job(0, 0));
        for (int i = 1; i <= jobs.length; i++) {
            this.sortedJobs.add(new Job(jobs[i - 1][0], jobs[i - 1][1]));
        }
        Collections.sort(this.sortedJobs);
	}
	
	public int getNumJobs() {
		return numJobs;
	}
	
	public int[][] getJobs() {
		return jobs;
	}

	public ArrayList<Job> getSortedJobs() { return sortedJobs; }
}
