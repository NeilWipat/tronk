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

/* very simple single bot trader */
/* Neil Wipat using Keith's code */

public class Trader {

    public static void main(String[] args) throws SSLException, ParseException, InterruptedException {
        System.out.println("Running \n");


        DSL dsl = new DSL();
        dsl.marketData.grpcConnect();
        dsl.setWorkingCurrency(ExchangeType.KRAKEN, Currency.XBT, Currency.EUR);
        //Create an exchange
        LocalExchange lexchange = new LocalExchange(dsl);
        //Launch a bot
        SimpleBot bot = new SimpleBot(lexchange);

        //Event based stuff here to turn bot.activate into an event handler.
        //Each time there is a new trade detected, call activate on the bot
        dsl.marketData.addMarketTradeHandler(
                mt -> System.out.println("A market trade happened: " + mt.getPriceInAlt()));
        dsl.marketData.addMarketTradeHandler(
                mt -> bot.activate());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dsl.marketData.subscribeToMarketTradeStream(sdf.parse("2021-08-01"));

        // Blocking here because above event registration occurs in the background
        while (true) {
            Thread.sleep(1000L);
        }
    }
}
