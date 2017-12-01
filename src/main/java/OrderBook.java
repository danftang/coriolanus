import java.util.ArrayList;

/**
 * Created by daniel on 02/11/17.
 */
public class OrderBook<COMMODITY> {
    public ArrayList<MarketOrder<COMMODITY>> bids;
    public ArrayList<MarketOrder<COMMODITY>> offers;

    public void clearBook() {

    }
}
