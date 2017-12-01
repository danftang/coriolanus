import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderBook<COMMODITY> {

    public enum PricingMethod {
        BID_PRICE,
        OFFER_PRICE,
        MARKET_TAKES_DIFFERENCE
    }

    public List<MarketOrder<COMMODITY>> bids = new ArrayList<>();
    public List<MarketOrder<COMMODITY>> offers = new ArrayList<>();
    public Report report;

    private PricingMethod pricingMethod = PricingMethod.MARKET_TAKES_DIFFERENCE;

    public OrderBook() {

    }

    public OrderBook(PricingMethod pricingMethod) {
        this.pricingMethod = pricingMethod;
    }

    public Report clearBook() {
        bids.sort(MarketOrder::sortDescending);
        offers.sort(MarketOrder::sortDescending);
        report = matchBidsAndOffers();
        return report;
    }

    public Report getReport() {
        return report;
    }

    private Report matchBidsAndOffers() {

        int worstOfferToIncludeIdx = offers.size() - 1;
        int bestQuantityMatched = calculateQuantityMatched(worstOfferToIncludeIdx);

        while (true) {
            if (worstOfferToIncludeIdx == 0) {
                break;
            }

            worstOfferToIncludeIdx--;

            int quantityMatched = calculateQuantityMatched(worstOfferToIncludeIdx);

            if (quantityMatched <= bestQuantityMatched) {
                worstOfferToIncludeIdx++;
                break;
            } else {
                bestQuantityMatched = quantityMatched;
            }
        }

        return matchBidsAndOffers(worstOfferToIncludeIdx);
    }

    private int calculateQuantityMatched(int worstOfferToIncludeIdx) {

        Report report = matchBidsAndOffers(worstOfferToIncludeIdx);

        int quantityMatched = 0;
        for (OrderReport offerReport : report.offerReports) {
            quantityMatched += offerReport.order.quantity - offerReport.quantityRemaining;
            if (offerReport.quantityRemaining > 0) {
                break;
            }
        }

        return quantityMatched;
    }

    private Report matchBidsAndOffers(int worstOfferToIncludeIdx) {

        List<MarketOrder<COMMODITY>> includedOffers = offers.subList(worstOfferToIncludeIdx, offers.size());

        List<OrderReport> bidReports = makeOrderReports(bids);
        List<OrderReport> offerReports = makeOrderReports(includedOffers);

        for (OrderReport bid : bidReports) {
            for (OrderReport offer : offerReports) {
                if (offer.quantityRemaining == 0) {
                    continue;
                }

                double bidPrice = bid.order.unitPrice;
                double offerPrice = offer.order.unitPrice;

                if (offerPrice <= bidPrice) {
                    int quantityMatched = Math.min(bid.quantityRemaining, offer.quantityRemaining);

                    double bidPricePaid = pricingMethod == PricingMethod.OFFER_PRICE ? offerPrice : bidPrice;
                    bid.addMatchedOrder(offer.order, quantityMatched, bidPricePaid);

                    double offerPricePaid = pricingMethod == PricingMethod.BID_PRICE ? bidPrice : offerPrice;
                    offer.addMatchedOrder(bid.order, quantityMatched, offerPricePaid);
                }

                if (bid.quantityRemaining == 0) {
                    break;
                }
            }
        }

        List<MarketOrder<COMMODITY>> excludedOffers = offers.subList(0, worstOfferToIncludeIdx);
        offerReports.addAll(makeOrderReports(excludedOffers));
        return new Report(bidReports, offerReports);
    }

    private List<OrderReport> makeOrderReports(List<MarketOrder<COMMODITY>> orders) {
        return orders.stream().map(OrderReport::new).collect(Collectors.toList());
    }

    public class OrderReport {

        public MarketOrder<COMMODITY> order;
        public Map<MarketOrder<COMMODITY>, Integer> matchedOrdersAndQuantities = new HashMap<>();
        public int quantityRemaining = 0;
        public int quantityMatched = 0;
        public double paid;

        public OrderReport(MarketOrder<COMMODITY> order) {
            this.order = order;
            quantityRemaining = order.quantity;
        }

        public void addMatchedOrder(MarketOrder<COMMODITY> matchedOrder, int quantity, double unitPricePaid) {
            matchedOrdersAndQuantities.put(matchedOrder, quantity);
            quantityRemaining -= quantity;
            quantityMatched += quantity;
            this.paid += unitPricePaid * quantity;
        }

        public double totalAtOrderPrice() {
            return quantityMatched * order.unitPrice;
        }

        public double totalAtMatchedOrderPrices() {
            double total = 0.0;

            for (Map.Entry<MarketOrder<COMMODITY>, Integer> matchedOrderEntry : matchedOrdersAndQuantities.entrySet()) {
                MarketOrder<COMMODITY> matchedOrder = matchedOrderEntry.getKey();
                int quantity = matchedOrderEntry.getValue();
                total += matchedOrder.unitPrice * quantity;
            }

            return total;
        }
    }

    public class Report {

        public List<OrderReport> bidReports;
        public List<OrderReport> offerReports;
        public double totalPaidByBidders;
        public double totalPaidToOfferers;
        public double quantityBidFor;
        public double quantityOffered;
        public double quantitySold;

        public Report(List<OrderReport> bidReports, List<OrderReport> offerReports) {
            this.bidReports = bidReports;
            this.offerReports = offerReports;
            this.totalPaidByBidders = sumValuePaid(bidReports);
            this.totalPaidToOfferers = sumValuePaid(offerReports);
            this.quantityBidFor = sumQuantity(bidReports);
            this.quantityOffered = sumQuantity(offerReports);
            this.quantitySold = totalQuantitySold();
        }

        public Map<MarketOrder<COMMODITY>, OrderReport> toMap() {
            List<OrderReport> allOrderReports = new ArrayList<>();
            allOrderReports.addAll(bidReports);
            allOrderReports.addAll(offerReports);
            return allOrderReports.stream().collect(Collectors.toMap(r -> r.order, r -> r));
        }

        private double sumValuePaid(List<OrderReport> reports) {
            return reports.stream().mapToDouble(r -> r.paid).sum();
        }

        private int sumQuantity(List<OrderReport> reports) {
            return reports.stream().mapToInt(r -> r.order.quantity).sum();
        }

        private int totalQuantitySold() {
            return bidReports.stream().mapToInt(r -> r.quantityMatched).sum();
        }
    }
}
