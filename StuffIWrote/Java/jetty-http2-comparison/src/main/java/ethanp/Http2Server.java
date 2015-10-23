package ethanp;

import ethanp.examples.HelloWorld;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlets.PushCacheFilter;
import org.eclipse.jetty.servlets.PushSessionCacheFilter;

/**
 * Ethan Petuchowski 10/21/15
 */
public class Http2Server {
    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);
        HttpConfiguration httpConfig = new HttpConfiguration();

        HttpConnectionFactory http1 = new HttpConnectionFactory(httpConfig);
        ServerConnector connector = new ServerConnector(server, http1);

        /* browsers don't implement clearTextUpgrade!! */
//        HTTP2CServerConnectionFactory clearTextUpgrade = new HTTP2CServerConnectionFactory(httpConfig);
//        ServerConnector directUpgradeConnector = new ServerConnector(server, http1, clearTextUpgrade);

        /* this ALPN is provided by Jetty's ALPN boot JAR file hack, but will come in Java 9 */
        HTTP2ServerConnectionFactory alpnUpgrade = new HTTP2ServerConnectionFactory(httpConfig);
        ServerConnector alpnConnector = new ServerConnector(server, alpnUpgrade);

        server.addConnector(alpnConnector);
        server.setHandler(new HelloWorld());
        server.start();
        server.join();

        // `initialStreamSendWindow` defaults to 65535.
        //      Larger values may allow greater throughput
        //      but also risk head of line blocking
        //      if TCP/IP flow control is triggered.
//        alpnUpgrade.setInitialStreamSendWindow();
//        alpnUpgrade.setInputBufferSize();

        // The maximum number of concurrently open streams allowed on a single HTTP/2 connection
        // (default 1024). Larger values increase parallelism but cost a memory commitment.
//        alpnUpgrade.setMaxConcurrentStreams();
//        alpnConnector.setAcceptQueueSize();
//        alpnConnector.setInheritChannel();
//        alpnConnector.setReuseAddress();
//        alpnConnector.setSelectorPriorityDelta();
//        alpnConnector.setSoLingerTime();
//        alpnConnector.setAcceptorPriorityDelta();


        /*
        HTTP/2 Push can be automated in your application by simply configuring a PushCacheFilter

        PushCacheFilter analyzes the HTTP requests for resources that arrive to your web application.

        Some of these requests contain the HTTP Referer header that points to a resource that
        has been requested previously.

        This allows the PushCacheFilter to organize resources in primary resources (those
        referenced by the Referer header) and secondary resources (those that have the
        Referer header).

        PushCacheFilter associates secondary resources to primary resources.

        Only secondary resources that have been requested within a time window from the request
        of the primary resource are associated with the primary resource.
        */
        PushCacheFilter pushFilter = new PushCacheFilter();

        /* implements a more sophisticated approach using a builder to actually push resources. */
        PushSessionCacheFilter pushSessionFilter = new PushSessionCacheFilter();

        /* In order to implement your own strategy to push resources, you can write a simple
           Servlet Filter, and in there use the Jetty specific API (i.e. Dispatcher.push(...)
           or the PushBuilder APIs) to push your own resources, similarly to what the above
           referenced filters do.
         */
    }
}
