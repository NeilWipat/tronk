import com.scalesinformatics.prosperity.helloworld.LocalExchange;

public class SimpleBot {
    /* Buys the asset if its price decreased by more than the threshold. */
    double DIP_THRESHOLD = -2.25;

    /* Buys the asset if its price increased by more than the threshold. */
    double UPWARD_TREND_THRESHOLD = 1.5;

    /* Sells the asset if its price has increased above the threshold since we bought it. */
    double PROFIT_THRESHOLD = 1.25;

    /* Ideally, we would only want our bot to sell when it makes a profit. */
    double STOP_LOSS_THRESHOLD = -2.0;

    double lastObPrice = 100.0;

    double XBTprice;
    double percentDiff;

    // 0 is sell, 1 buy
    boolean isNextOpBuy = true;

    LocalExchange lexchange;

    public SimpleBot(LocalExchange lexchange) {
        this.lexchange = lexchange;
    }

    public void activate() {
        //Get current market price for XBT
        XBTprice = lexchange.getMarketPrice();

        //See what the % change in XBT price is
        percentDiff = (XBTprice - lastObPrice) / lastObPrice * 100;

        // Check buy or sell state
        if (isNextOpBuy) {
            //If we are buying then try to buy
            tryToBuy(percentDiff);
        } else
        {
            tryToSell(percentDiff);
        }
    }

    public void tryToBuy(double percentDiff)
    {
        // If the price increase is above our threshold then buy before it gets more expensive OR buy whilst it is undervalued
        if (percentDiff >= UPWARD_TREND_THRESHOLD || percentDiff <=DIP_THRESHOLD)
        {
            lexchange.placeBuyOrder();
            isNextOpBuy = false;
        }
    }
    public void tryToSell(double percentDiff)
    {
        // If the price increase is above our profit threshold then sell XBT to make a profit OR sell before it crashes further
        if (percentDiff >= PROFIT_THRESHOLD || percentDiff <=STOP_LOSS_THRESHOLD)
        {
            lexchange.placeSellOrder();
            isNextOpBuy = true;
        }
    }

}
