import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderBookTest {

    @Test
    public void allOrdersMatchable() {
        MarketOrder<String> bid1 = new MarketOrder<>();
        bid1.unitPrice = 1;
        bid1.quantity = 1;

        MarketOrder<String> bid2 = new MarketOrder<>();
        bid2.unitPrice = 1;
        bid2.quantity = 1;

        MarketOrder<String> bid3 = new MarketOrder<>();
        bid3.unitPrice = 1;
        bid3.quantity = 1;

        MarketOrder<String> offer1 = new MarketOrder<>();
        offer1.unitPrice = 1;
        offer1.quantity = 1;

        MarketOrder<String> offer2 = new MarketOrder<>();
        offer2.unitPrice = 1;
        offer2.quantity = 1;

        MarketOrder<String> offer3 = new MarketOrder<>();
        offer3.unitPrice = 1;
        offer3.quantity = 1;

        OrderBook<String> orderBook = new OrderBook<>();
        orderBook.bids.add(bid1);
        orderBook.bids.add(bid2);
        orderBook.bids.add(bid3);
        orderBook.offers.add(offer1);
        orderBook.offers.add(offer2);
        orderBook.offers.add(offer3);

        OrderBook<String>.Report report = orderBook.clearBook();
        summariseReport(report);

        assertEquals(report.quantitySold, 3, 0);
    }

    @Test
    public void oneBidFulfilledByManyOffers() {
        MarketOrder<String> bid1 = new MarketOrder<>();
        bid1.unitPrice = 1;
        bid1.quantity = 3;

        MarketOrder<String> offer1 = new MarketOrder<>();
        offer1.unitPrice = 1;
        offer1.quantity = 1;

        MarketOrder<String> offer2 = new MarketOrder<>();
        offer2.unitPrice = 1;
        offer2.quantity = 1;

        MarketOrder<String> offer3 = new MarketOrder<>();
        offer3.unitPrice = 1;
        offer3.quantity = 1;

        OrderBook<String> orderBook = new OrderBook<>();
        orderBook.bids.add(bid1);
        orderBook.offers.add(offer1);
        orderBook.offers.add(offer2);
        orderBook.offers.add(offer3);

        OrderBook<String>.Report report = orderBook.clearBook();
        summariseReport(report);

        assertEquals(report.quantitySold, 3, 0);
    }

    @Test
    public void manyBidsFulfilledByOneOffer() {
        MarketOrder<String> bid1 = new MarketOrder<>();
        bid1.unitPrice = 1;
        bid1.quantity = 1;

        MarketOrder<String> bid2 = new MarketOrder<>();
        bid2.unitPrice = 1;
        bid2.quantity = 1;

        MarketOrder<String> bid3 = new MarketOrder<>();
        bid3.unitPrice = 1;
        bid3.quantity = 1;

        MarketOrder<String> offer1 = new MarketOrder<>();
        offer1.unitPrice = 1;
        offer1.quantity = 3;

        OrderBook<String> orderBook = new OrderBook<>();
        orderBook.bids.add(bid1);
        orderBook.bids.add(bid2);
        orderBook.bids.add(bid3);
        orderBook.offers.add(offer1);

        OrderBook<String>.Report report = orderBook.clearBook();
        summariseReport(report);

        assertEquals(report.quantitySold, 3, 0);
    }

    @Test
    public void oneOfferTooExpensive() {
        MarketOrder<String> bid1 = new MarketOrder<>();
        bid1.unitPrice = 1;
        bid1.quantity = 1;

        MarketOrder<String> bid2 = new MarketOrder<>();
        bid2.unitPrice = 1;
        bid2.quantity = 1;

        MarketOrder<String> bid3 = new MarketOrder<>();
        bid3.unitPrice = 1;
        bid3.quantity = 1;

        MarketOrder<String> offer1 = new MarketOrder<>();
        offer1.unitPrice = 2;
        offer1.quantity = 1;

        MarketOrder<String> offer2 = new MarketOrder<>();
        offer2.unitPrice = 1;
        offer2.quantity = 1;

        MarketOrder<String> offer3 = new MarketOrder<>();
        offer3.unitPrice = 1;
        offer3.quantity = 1;

        OrderBook<String> orderBook = new OrderBook<>();
        orderBook.bids.add(bid1);
        orderBook.bids.add(bid2);
        orderBook.bids.add(bid3);
        orderBook.offers.add(offer1);
        orderBook.offers.add(offer2);
        orderBook.offers.add(offer3);

        OrderBook<String>.Report report = orderBook.clearBook();
        summariseReport(report);

        assertEquals(report.quantitySold, 2, 0);
        assertEquals(report.toMap().get(offer1).quantityMatched, 0, 0);
    }

    @Test
    public void highestBidsHavePriority() {
        MarketOrder<String> bid1 = new MarketOrder<>();
        bid1.unitPrice = 3;
        bid1.quantity = 1;

        MarketOrder<String> bid2 = new MarketOrder<>();
        bid2.unitPrice = 2;
        bid2.quantity = 1;

        MarketOrder<String> bid3 = new MarketOrder<>();
        bid3.unitPrice = 1;
        bid3.quantity = 1;

        MarketOrder<String> offer1 = new MarketOrder<>();
        offer1.unitPrice = 1;
        offer1.quantity = 1;

        MarketOrder<String> offer2 = new MarketOrder<>();
        offer2.unitPrice = 1;
        offer2.quantity = 1;

        OrderBook<String> orderBook = new OrderBook<>();
        orderBook.bids.add(bid1);
        orderBook.bids.add(bid2);
        orderBook.bids.add(bid3);
        orderBook.offers.add(offer1);
        orderBook.offers.add(offer2);

        OrderBook<String>.Report report = orderBook.clearBook();
        summariseReport(report);

        assertEquals(report.toMap().get(bid1).quantityMatched, 1, 0);
        assertEquals(report.toMap().get(bid2).quantityMatched, 1, 0);
        assertEquals(report.toMap().get(bid3).quantityMatched, 0, 0);
    }

    @Test
    public void lowestOffersHavePriority() {
        MarketOrder<String> bid1 = new MarketOrder<>();
        bid1.unitPrice = 3;
        bid1.quantity = 1;

        MarketOrder<String> bid2 = new MarketOrder<>();
        bid2.unitPrice = 3;
        bid2.quantity = 1;

        MarketOrder<String> offer1 = new MarketOrder<>();
        offer1.unitPrice = 3;
        offer1.quantity = 1;

        MarketOrder<String> offer2 = new MarketOrder<>();
        offer2.unitPrice = 2;
        offer2.quantity = 1;

        MarketOrder<String> offer3 = new MarketOrder<>();
        offer3.unitPrice = 1;
        offer3.quantity = 1;

        OrderBook<String> orderBook = new OrderBook<>();
        orderBook.bids.add(bid1);
        orderBook.bids.add(bid2);
        orderBook.offers.add(offer1);
        orderBook.offers.add(offer2);
        orderBook.offers.add(offer3);

        OrderBook<String>.Report report = orderBook.clearBook();
        summariseReport(report);

        assertEquals(report.toMap().get(offer1).quantityMatched, 0, 0);
        assertEquals(report.toMap().get(offer2).quantityMatched, 1, 0);
        assertEquals(report.toMap().get(offer3).quantityMatched, 1, 0);
    }

    private void summariseReport(OrderBook<?>.Report report) {
        System.out.println("\nBid for: " + report.quantityBidFor +
                ", offered: " + report.quantityOffered +
                ", sold: " + report.quantitySold +
                ", total paid: " + report.totalValue +
                ", mean price: " + report.meanPrice);

        System.out.println("\nBIDS:");
        for (OrderBook<?>.OrderReport or : report.bidReports) {
            System.out.println("Bid for: " + or.order.quantity + " at " + or.order.unitPrice + ", won: " + or.quantityMatched + ", paid: " + or.totalAtOrderPrice());
        }

        System.out.println("\nOFFERS:");
        for (OrderBook<?>.OrderReport or : report.offerReports) {
            System.out.println("Offered: " + or.order.quantity + " at " + or.order.unitPrice + ", sold: " + or.quantityMatched + ", received: " + or.totalAtOrderPrice());
        }
    }
}
