package controllers;

import actors.*;
import akka.actor.*;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.Option;


/**
 * The main web controller that handles returning the index page, setting up a WebSocket, and watching a stock.
 */
public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render());
    }

    public static WebSocket<JsonNode> ws() {
        return new WebSocket<JsonNode>() {

            /** EP:
             * This probably gets called when the "protocol upgrade" (to a WebSocket) succeeds
             * It's probably a bit different to do this in Scala; for that, see the web-chat
               example for an idea of how that's done.
             */

            public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                // create a new UserActor and give it the default stocks to watch
                final ActorRef userActor = Akka.system().actorOf(Props.create(UserActor.class, out));

                //// EP: install handlers on our new WebSocket connection ////

                // send all WebSocket message to the UserActor
                /** EP:
                 * I guess since we can't just paste a block of code as a parameter in Java,
                 * we must pass the function on which to install a callback an instance
                 * of a "CallBack" object that, whose `invoke()` takes a JsonNode as a parameter
                 * (this is something we [must] happen to be aware of) and uses its contents to
                 * instantiate a message containing the stock-symbol the user has requested to
                 * follow to the "Acting Manager of Stocks we're Following", who will (if none exist)
                 * create a new "Acting Watcher of One Stock" who will create a string of random
                 * doubles and send it back [here (??)]
                 *
                 * note, we could probably pass the WebSocket.In to another object if we wanted.
                 */
                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        // parse the JSON into WatchStock
                        WatchStock watchStock = new WatchStock(jsonNode.get("symbol").textValue());
                        // send the watchStock message to the StocksActor
                        StocksActor.stocksActor().tell(watchStock, userActor);
                    }
                });

                /** EP on onClose:
                 * The existence of this method indicates to me that this server process is meant
                 * to service multiple clients at the same time by maintaining a representation
                 * of who is looking for responses, and what stocks in particular they are
                 * interested in.
                 */
                // on close, tell the userActor to shutdown
                in.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        final Option<String> none = Option.empty();
                        StocksActor.stocksActor().tell(new UnwatchStock(none), userActor);
                        Akka.system().stop(userActor);
                    }
                });
            }
        };
    }

}
