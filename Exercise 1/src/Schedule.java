
public class Schedule implements Comparable<Schedule> {
    // A linked-list is a reletively efficient representation of a schedule
    // Feel free to modify it if you feel there exists a better one
    // The main advantage is that in a search-tree there is a lot of overlap
    // between schedules, this implementation stores this overlap only once
    private Schedule previous;
    private Schedule next;
    private Job job;
    private int jobID;
    private int originalID;
    private double jobLength;

    // tardiness can be calculated instead of memorized
    // however, we need to calculate it a lot, so we momorize it
    // if memory is an issue, however, try calculating it
    private double tardiness;

    // add an additional job to the schedule
    public Schedule(Schedule s, int jobID, double jobLength, double jobDueTime) {
        this.previous = s;
        this.jobID = jobID;
        this.originalID = jobID;
        this.jobLength = jobLength;
        this.tardiness = Math.max(0, getTotalTime() - jobDueTime);
        this.job = new Job(jobLength, jobDueTime);

        if (previous != null) {
            this.tardiness += previous.getTardiness();
            previous.next = this;
        }
    }

    public Schedule(Schedule s, int jobID, int originalID, double jobLength, double jobDueTime) {
        this.previous = s;
        this.jobID = jobID;
        this.originalID = originalID;
        this.jobLength = jobLength;
        this.tardiness = Math.max(0, getTotalTime() - jobDueTime);
        this.job = new Job(jobLength, jobDueTime);

        if (previous != null) {
            this.tardiness += previous.getTardiness();
            previous.next = this;
        }
    }

    // used by the best-first search
    // currently, schedules are traversed in smallest total tardiness order
    public int compareTo(Schedule o) {
        return (int) (getTardiness() - o.getTardiness());

        // replace with the following to get a depth-first search
        // return get_depth() - o.get_depth();
    }

    public int getDepth() {
        int depth = 1;
        if (previous != null) depth += previous.getDepth();
        return depth;
    }

    public int getHeight() {
        int height = 1;
        if (next != null) height += next.getHeight();
        return height;
    }

    public double getTotalTime() {
        double time = jobLength;
        if (previous != null) time += previous.getTotalTime();
        return time;
    }

    public int getOriginalID() {
        return originalID;
    }

    public Job getJob() {
        return job;
    }

    public Schedule getNext() {
        return next;
    }

    public Schedule getPrevious() {
        return previous;
    }

    public double getTardiness() {
        return tardiness;
    }

    public double actualTardiness() {
        double tardiness = getTardiness();
        if (previous != null) {
            tardiness -= previous.getTardiness();
        }

        return tardiness;
    }

    public boolean containsJob(int job) {
        return (jobID == job) || (previous != null && previous.containsJob(job));
    }

    public static Schedule mergeSchedules(Schedule left, Job k, int kId, Schedule right) {
        if (right == null) {
            if (left == null) {
                if (k == null) {
                    return null;
                } else {
                    return new Schedule(null, 0, kId, k.getLength(), k.getDueTime());
                }
            } else {
                if (k == null) {
                    return left.lastSchedule();
                } else {
                    return new Schedule(left.clone().lastSchedule(), 0, kId, k.getLength(), k.getDueTime()).lastSchedule();
                }
            }

        }

        if (left == null) {
            if (k == null) {
                return right.lastSchedule();
            } else {
                Schedule kSchedule = new Schedule(null, 0, kId, k.getLength(), k.getDueTime());
                Schedule rightSchedule = right.clone();
                rightSchedule.firstSchedule().setPrevious(kSchedule);
                rightSchedule.recalculateTardiness();
                return rightSchedule.lastSchedule();
            }
        }

        if (k == null) {
            Schedule rightSchedule = right.clone();
            rightSchedule.firstSchedule().setPrevious(left.clone().lastSchedule());
            rightSchedule.recalculateTardiness();
            return rightSchedule.lastSchedule();
        } else {
            Schedule leftSchedule = new Schedule(left.clone().lastSchedule(), 0, kId, k.getLength(), k.getDueTime());
            Schedule rightSchedule = right.clone();
            rightSchedule.firstSchedule().setPrevious(leftSchedule.lastSchedule());
            rightSchedule.recalculateTardiness();
            return rightSchedule.lastSchedule();
        }
    }

    public Schedule clone() {
        Schedule current = this.firstSchedule();
        Schedule clone = new Schedule(null, current.jobID, current.originalID, current.job.getLength(), current.job.getDueTime());
        ;
        while (current.getNext() != null) {
            current = current.getNext();
            clone = new Schedule(clone, current.jobID, current.originalID, current.job.getLength(), current.job.getDueTime());
        }
        return clone;
    }

    public void recalculateTardiness() {
        Schedule current = this.firstSchedule();
        int i = 1;
        current.tardiness = Math.max(0, current.getTotalTime() - current.job.getDueTime());
        while (current.next != null) {
            current = current.next;
            i++;
            current.tardiness = Math.max(0, current.getTotalTime() - current.job.getDueTime()) + current.previous.tardiness;
        }
    }

    public void setPrevious(Schedule previous) {
        this.previous = previous;
        if (previous != null) {
            previous.next = this;
        }
    }

    public void setNext(Schedule next) {
        this.next = next;
        if (next != null) {
            next.previous = this;
        }
    }

    public Schedule lastSchedule() {
        if (next == null) {
            return this;
        } else {
            return next.lastSchedule();
        }
    }

    public Schedule firstSchedule() {
        if (previous == null) {
            return this;
        } else {
            return previous.firstSchedule();
        }
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
        String hashCode = jobID + jobLength + "";
        return hashCode.hashCode();
    }

    public void print() {
        Job j = getJob();
        System.out.println("Schedule(id: " + jobID + ", oid: " + originalID + ", p: " + j.getLength() + ", t: " + getTotalTime() + ", d: " + j.getDueTime() + ", T: " + tardiness + ")");
    }

    public void printComplete() {
        Schedule current = this.firstSchedule();
        current.print();
        while (current.getNext() != null) {
            current = current.getNext();
            current.print();
        }
    }

    public Schedule get(int i) {
        if (i < 1) {
            return null;
        }

        Schedule current = this.firstSchedule();
        try {
            while (current.getDepth() < i) {
                current = current.next;
            }
            return current;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
