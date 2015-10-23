package ethanp.examples;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;

/**
 * Ethan Petuchowski 10/22/15
 *
 * Got this from:
 *      http://www.eclipse.org/jetty/documentation/current/embedding-jetty.html
 *
 * It seems to occasionally through NullPointerExceptions, but it keeps running...so IDK.
 */
public class FileServer {
    public static void main(String[] args) throws Exception
    {
        // If you set this to port 0, then a randomly available port will be assigned
        Server server = new Server(8080);

        // This is the object that will actually handle the request for a file.
        // Being a Jetty Handler, it is suitable for chaining with other handlers.
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{"docroot/index.html"});
        resource_handler.setResourceBase(".");

        // Dynamically GZIP compress responses asynchronously
        GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);

        HandlerList handlers = new HandlerList();

        /* This handle will deal with unhandled requests in the server.
         *  - For requests for favicon.ico, the Jetty icon is served.
         *  - For requests to '/' a 404 with a list of known contexts is served.
         *  - For all other requests a normal 404 is served.
         */
        DefaultHandler favicon404 = new DefaultHandler();
        handlers.setHandlers(new Handler[]{resource_handler, favicon404});
        gzip.setHandler(handlers);

        // start the server
        server.start();

        // run the server forever
        server.join();
    }
}
