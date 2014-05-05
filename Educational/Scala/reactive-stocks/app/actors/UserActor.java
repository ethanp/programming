package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Play;
import play.libs.Json;
import play.mvc.WebSocket;

import java.util.List;

/**
 * The broker between the WebSocket and the StockActor(s).  The UserActor holds the connection and sends serialized
 * JSON data to the client.
 */

/** EP:
 * The UserActor is not involved in directly asking for anything.  The Application Controller
 * tells the StocksActor to register the User for updates from the desired StockActors.  Then
 * the StockActors remember to send first the current state of the stock History on connect,
 * then an Update whenever one is received (every 75 ms).
 */

public class UserActor extends UntypedActor {

    private final WebSocket.Out<JsonNode> out;

    public UserActor(WebSocket.Out<JsonNode> out) {
        this.out = out;

        // watch the default stocks
        // EP: these have been saved in the "conf/application.conf" file as "default.stocks=["AAPL", "GOOG", "ORCL"]"
        List<String> defaultStocks = Play.application().configuration().getStringList("default.stocks");

        for (String stockSymbol : defaultStocks) {
            /** EP: scala version

            stocksActor ! WatchStock(stockSymbol)       */

            StocksActor.stocksActor().tell(new WatchStock(stockSymbol), getSelf());
        }
    }

    // EP: Json -> Client. Update: send new price for symbol; History: send list of prices for symbol
    // EP: A glimpse of the Java version of Actors.
    // --- We use "onReceive(Obj msg)" for "receive", and "instanceof" for "case".
    public void onReceive(Object message) {

        /** EP: scala version
            doesn't use Enumerators like it probably would in real life, but still nice idnit

        case StockUpdate(symbol, price) =>
          out.write(
            Json.obj(
              "type"   -> "stockupdate",
              "symbol" ->  symbol,
              "price"  ->  price.doubleValue
            )
          )

        case StockHistory(symbol, history) =>
          out.write(
            Json.obj(
              "type"    -> "stockhistory",
              "symbol"  ->  symbol,
              "history" ->  Json.arr(history)
            )
          )

         */
        if (message instanceof StockUpdate) {
            // push the stock to the client
            StockUpdate stockUpdate = (StockUpdate)message;
            ObjectNode stockUpdateMessage = Json.newObject();
            stockUpdateMessage.put("type", "stockupdate");
            stockUpdateMessage.put("symbol", stockUpdate.symbol());
            stockUpdateMessage.put("price", stockUpdate.price().doubleValue());
            out.write(stockUpdateMessage);
        }
        else if (message instanceof StockHistory) {
            // push the history to the client
            StockHistory stockHistory = (StockHistory)message;

            ObjectNode stockUpdateMessage = Json.newObject();
            stockUpdateMessage.put("type", "stockhistory");
            stockUpdateMessage.put("symbol", stockHistory.symbol());

            ArrayNode historyJson = stockUpdateMessage.putArray("history");
            for (Object price : stockHistory.history()) {
                historyJson.add(((Number)price).doubleValue());
            }

            out.write(stockUpdateMessage);
        }
    }
}
