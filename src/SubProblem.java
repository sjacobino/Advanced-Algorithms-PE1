import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Sharif on 09/11/2016.
 */
public class SubProblem {
    private Object[] objects;

    public SubProblem(Object... objects) {
        this.objects = new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            this.objects[i] = objects[i];
        }
    }

    public Object get(int i) {
        return this.objects[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubProblem that = (SubProblem) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(objects, that.objects);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(objects);
    }
}
