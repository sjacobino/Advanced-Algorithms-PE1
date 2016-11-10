
public class Schedule implements Comparable<Schedule> {
	// A linked-list is a reletively efficient representation of a schedule
	// Feel free to modify it if you feel there exists a better one
	// The main advantage is that in a search-tree there is a lot of overlap
	// between schedules, this implementation stores this overlap only once
	private Schedule previous;
	private Schedule next;
    private Job job;
	private int jobID;
	private int jobLength;
	
	// tardiness can be calculated instead of memorized
	// however, we need to calculate it a lot, so we momorize it
	// if memory is an issue, however, try calculating it
	private int tardiness;
	
	// add an additional job to the schedule
	public Schedule(Schedule s, int jobID, int jobLength, int jobDueTime){		
		this.previous = s;
		this.jobID = jobID;
		this.jobLength = jobLength;
		this.tardiness = Math.max(0, getTotalTime() - jobDueTime);
        this.job = new Job(jobLength, jobDueTime);
		
		if(previous != null) {
			this.tardiness += previous.getTardiness();
			previous.next = this;
		}
	}
	
	// used by the best-first search
	// currently, schedules are traversed in smallest total tardiness order
	public int compareTo(Schedule o){
		return getTardiness() - o.getTardiness();
		
		// replace with the following to get a depth-first search
		// return get_depth() - o.get_depth();
	}
	
	public int getDepth(){
		int depth = 1;
		if(previous != null) depth += previous.getDepth();
		return depth;
	}

	public int getHeight() {
        int height = 1;
        if (next != null ) height += next.getHeight();
        return height;
    }
	
	public int getTotalTime(){
		int time = jobLength;
		if(previous != null) time += previous.getTotalTime();
		return time;
	}

	public Job getJob() { return job; }

	public Schedule getNext() { return next; }
	
	public int getTardiness(){
		return tardiness;
	}
	
	public boolean containsJob(int job){
		return (jobID == job) || (previous != null && previous.containsJob(job));
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (jobID != schedule.jobID) return false;
        if (jobLength != schedule.jobLength) return false;
        if (previous != null ? !previous.equals(schedule.previous, true) : schedule.previous != null) return false;
        return next != null ? next.equals(schedule.next, false) : schedule.next == null;

    }

    public boolean equals(Object o, boolean backwards) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (jobID != schedule.jobID) return false;
        if (jobLength != schedule.jobLength) return false;
        if (backwards) {
            if (previous != null ? !previous.equals(schedule.previous, backwards) : schedule.previous != null) {
                return false;
            } else {
                return true;
            }
        } else {
            return next != null ? next.equals(schedule.next, backwards) : schedule.next == null;
        }

    }

    @Override
    public int hashCode() {
        int result = jobID;
        result = 31 * result + jobLength;
        return result;
    }
}
