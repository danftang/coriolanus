import java.util.Random;

/**
 * Created by daniel on 02/11/17.
 */
public class Agent {

    // Coriolanan's expected vitality...
    private double hungerRelatedHealthDecrement = 0.1;
    private double meanLifeExpectancy = 365 * 80.;
    private double sdLifeExpectancy = 365 * 5;
    private Random random = new Random();

    public Factory   factoryOfEmployment = null;

    public EmploymentMarket employmentMarket;
    public WordMarket wordMarket;

    public double currentAccount;
    public double currentHealth;

    public double age;


    public Agent (EmploymentMarket employmentMarket, WordMarket wordMarket) {
        this.employmentMarket = employmentMarket;
    }


    public void step() {

        // Every morning mama wakes up hungry
        boolean hungry = true;

        // look for work
        factoryOfEmployment = employmentMarket.applyForJob();

        // if successful set employed true and go to work at t'factory and get paid
        if (factoryOfEmployment != null) {

        }
        // else got panning for letters and get paid for those
        else {

        }

        // buy words
        double previousPriceOfWords = wordMarket.getYesterdaysWordPrice();
        double healthResponseBidPrice = (1./currentHealth) * previousPriceOfWords;
        double bidPrice;
        if (healthResponseBidPrice < currentAccount) {
            bidPrice = healthResponseBidPrice;
        } else {
            bidPrice = currentAccount;
        }
        boolean wonWordBid = wordMarket.submitWordBid(bidPrice);

        // pay and eat if you win
        if (wonWordBid) {
            currentAccount -= bidPrice;
            hungry = false;
        }

        // increment age
        age += 1.0;

        // decrement health if old TODO don't have internet so can't check what functionality is in random - age related
        // TODO health decrement should increase when approaching and exceeding avg life expectancy
        double ageRelatedHealthDecrement = random.nextGaussian();

        // set health based on nutrition
        currentHealth -= (ageRelatedHealthDecrement + hungerRelatedHealthDecrement);

        // check if dead
        if (currentHealth < 0.) {
            // TODO how do we kill someone???
        }





    }
}
