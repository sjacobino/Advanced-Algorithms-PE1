import java.util.Comparator;

/**
 * Created by Sharif on 17/10/2016.
 */
public class Job implements Comparable<Job> {
    private int length;
    private int dueTime;

    public Job(int length, int dueTime){
        this.length = length;
        this.dueTime = dueTime;
    }

    /*
      Compare due times
    */
    @Override
    public int compareTo(Job o) {
        return this.dueTime - o.dueTime;
    }
}
