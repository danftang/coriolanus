import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 02/11/17.
 *
 *
 *
 */
public class Firm implements Steppable {
    public String               program;
    public String               product;
    public List<Agent>          employees;
    public Map<Agent, Long>     wages;
//    public int                  wealth;
    public Map<String, Integer> stock;

    int    production;

    public Firm() {
        employees = new ArrayList<>();
        wages = new HashMap<>();
        stock = new HashMap<>();
    }

    public void step() {

    }

    public void bidForEmployment() {
        production = getExpectedDemand();
        MarketOrder bid = new MarketOrder();
        //bid.owner = this;
        bid.quantity = production;
        bid.unitPrice = ;
        //CoriolanusWorld.world.employmentMarket.bid(bid);
    }

    public void bidForRawMaterials() {

    }

    public void produceStuff() {


    }

    public long unitGrossProfit() {
        long gp = CoriolanusWorld.world.wordMarket.getPrice(product);
        String charAsString;
        for(char c : product.toCharArray()) {
            gp -= CoriolanusWorld.world.wordMarket.getPrice(new String(c));
        }
    }

    // returns expected demand in next timestep
    long getExpectedDemand() {
        return(0);
    }


}
