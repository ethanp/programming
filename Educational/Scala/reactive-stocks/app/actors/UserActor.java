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

public class UserActor extends UntypedActor {

    private final WebSocket.Out<JsonNode> out;

    public UserActor(WebSocket.Out<JsonNode> out) {
        this.out = out;

        // watch the default stocks
        // EP: these have been saved in the "conf/application.conf" file like "default.stocks=["AAPL", "GOOG", "ORCL"]"
        List<String> defaultStocks = Play.application().configuration().getStringList("default.stocks");

        for (String stockSymbol : defaultStocks) {

            // EP: we invoke `stocksActor()` on the StocksActor's "companion object".
            // --- This is the getter for a lazy val containing a single instance of the
            // --- StocksActor class.
            // EP: `WatchStock(stockSymbol)` is one of those typical case-classes you
            // --- send to actors.
            StocksActor.stocksActor().tell(new WatchStock(stockSymbol), getSelf());
        }
    }

    public void onReceive(Object message) {
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
