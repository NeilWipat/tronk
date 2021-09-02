//Simple exchange class to interface with Keith's code
//Call this first to update exchange variables from the event handler

import com.scalesinformatics.prosperity.data.exchange.Currency;
import com.scalesinformatics.prosperity.data.exchange.Direction;
import com.scalesinformatics.prosperity.data.exchange.ExchangeType;
import com.scalesinformatics.prosperity.dsl2.DSL;
import com.scalesinformatics.prosperity.dsl2.marketdata.TradeStreamWorker;
import com.scalesinformatics.prosperity.dsl2.simulated_exchange.SimulatedExchange;

import javax.net.ssl.SSLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LocalExchange {

    double dollars;
    double xbt;

    SimulatedExchange dummyExchange;

    public LocalExchange(DSL dsl)
    {
        dummyExchange = new SimulatedExchange(dsl, Currency.XBT, Currency.USD);
        dollars = 100.00;
        xbt = 0.00;
    }

    // Contact the exchange and return our current account balance
    public double getBalances(){
        return cash;
    } //keep cash local for now.

    //Get the price of BTC
    public double getMarketPrice() {
        return dummyExchange.getLastPrice();
    }

    public double placeSellOrder() {
        dsl.orders.placeOrder(Direction.SELL, 1, cash);
        double price = dummyExchange.getLastSellPrice(); //This should be the price we got for the trade(s) in practise.
        //Convert our xbt into dollars
        dollars = xbt * price;
        xbt =0;
        return price;
    }

    public double placeBuyOrder() {
        dsl.orders.placeOrder(Direction.BUY, 1, cash);
        double price = dummyExchange.getLastBuyPrice(); //This should be the price we got for the trade(s) in practise.
        //Convert our dollars into xbt
        xbt = dollars/price;
        dollars =0.00;
        return price;
    }

}
