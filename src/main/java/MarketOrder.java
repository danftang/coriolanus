import static java.lang.Long.signum;

/**
 * Created by daniel on 02/11/17.
 */
public class MarketOrder<COMMODITY> {
    public Agent       owner;
    public COMMODITY   thing;
    public long        unitPrice;
    public int         quantity;

    public static int sortAscending(MarketOrder<?> a, MarketOrder<?> b) {
        return signum(a.unitPrice - b.unitPrice);
    }

    public static int sortDescending(MarketOrder<?> a, MarketOrder<?> b) {
        return signum(b.unitPrice - a.unitPrice);
    }
}
