import java.util.Comparator;

/**
 * Created by Sharif on 17/10/2016.
 */
public class Job implements Comparable<Job> {
    private double length;
    private double dueTime;

    public Job(double length, double dueTime) {
        this.length = length;
        this.dueTime = dueTime;
    }

    /*
      Compare due times
    */
    @Override
    public int compareTo(Job o) {
        return (int) (this.dueTime - o.dueTime);
    }

    public double getLength() {
        return length;
    }

    public double getDueTime() {
        return dueTime;
    }

    public void print() {
        System.out.println("Job(p: " + length + ", d: " + dueTime + ")");
    }
}
