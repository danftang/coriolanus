import java.util.ArrayList;
import java.util.Random;

/**
 * Created by daniel on 02/11/17.
 */
public class CoriolanusWorld {
    public SteppableSet<Agent>  agents;
    public SteppableSet<Firm>   firms;
    public Random               rand;
    public EmploymentMarket     employmentMarket;
    public WordMarket           wordMarket;
    static public CoriolanusWorld world = new CoriolanusWorld();

    public CoriolanusWorld() {
        agents = new SteppableSet<Agent>();
        firms = new SteppableSet<Firm>();
        rand = new Random();
        employmentMarket = new EmploymentMarket();
        wordMarket = new WordMarket();
    }

    public void step() {
        agents.step();
        firms.step();
        wordMarket.clearMarket();
        employmentMarket.clearMarket();
    }

    public static void main(String [] args) {
        for(int i=0; i<100; ++i) {
            world.step();
        }
    }
}
