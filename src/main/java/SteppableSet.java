import java.util.ArrayList;

/**
 * Created by daniel on 21/11/17.
 */
public class SteppableSet<T extends Steppable> extends ArrayList<T> implements Steppable {
    public void step() {
        for(T member : this) {
            member.step();
        }
    }
}
