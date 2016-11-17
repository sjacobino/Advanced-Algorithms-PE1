import java.util.ArrayList;

/**
 * Created by Sharif on 16/11/2016.
 */
public class TardinessAndSchedule {
    private double tardiness;
    private Schedule schedule;

    public TardinessAndSchedule(double tardiness, Schedule schedule){
        this.tardiness = tardiness;
        this.schedule = schedule;
    }

    public double getTardiness() {
        return tardiness;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
