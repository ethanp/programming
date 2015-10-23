package ethanp.examples;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NegotiatingServerConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Ethan Petuchowski 10/23/15
 * <p>
 * Special thanks to fstab for: github.com/fstab/http2-examples/blob/master/jetty-http2-echo-server/src/main/java/de/consol/labs/h2c/Http2EchoServer.java
 * except updated to work with Jetty 9.3 or whatever this is.
 */
public class Http2EchoServer {

    static class PushEchoServlet extends HttpServlet {
        private String receivedData = ""; // POST data that we got from the client.

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
            switch (request.getMethod()) {
                case "GET":
                    if (request.getServletPath().contains("/data")) {

                        // This request does not necessarily come from the client.
                        // It may be triggered by the PUSH_PROMISE.
                        //
                        // However, when responding it does not make a difference if
                        // we respond to a client request or to a push promise.
                        //
                        response.setContentType("text/plain");
                        response.getWriter().write("I received the following data: "+receivedData);
                    }
                    else {
                        showIndexHtml(response);
                    }
                    break;
                case "POST":
                    handlePost(request, response);
                    push(request);
                    break;
                default:
                    showIndexHtml(response);
            }
        }

        private void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            receivedData = IOUtils
                .readLines(request.getReader())
                .stream()
                .reduce("", (a, b) -> a+" "+b);

            response.setContentType("text/plain");
            response.getWriter().write("Data received successfully. Retrieve data with GET /data");
        }

        private void push(HttpServletRequest req) {
            Request baseRequest = Request.getBaseRequest(req);
            if (baseRequest.isPushSupported()) {
                baseRequest
                    .getPushBuilder()
                    .method("GET")
                    .path("/data")
                    .push();
            }
        }

        private void showIndexHtml(HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            try (InputStream in = this.getClass()
                .getClassLoader()
                .getResourceAsStream("docroot/index.html")) {
                IOUtils.copy(in, response.getWriter());
            }

        }
    }

    public static void main(String... args) throws Exception {
        Server server = new Server();

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new PushEchoServlet()), "/");
        server.setHandler(context);

        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);

        /* make the regular (unencrypted) HTTP server connector */
        ServerConnector http1Connector = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http1Connector.setPort(8080);
        http1Connector.setIdleTimeout(30000);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(HttpsServer.keystoreFile.getAbsolutePath());
        sslContextFactory.setKeyStorePassword(HttpsServer.PASSWORD);
        sslContextFactory.setKeyManagerPassword(HttpsServer.PASSWORD);

        // HTTPS Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration(http_config);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory http2Factory = new HTTP2ServerConnectionFactory(httpsConfig);

        // makes program bomb out if ALPN is not available in boot classpath
        NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();

        // SSL Connection Factory
        SslConnectionFactory sslFactory = new SslConnectionFactory(
            sslContextFactory,
            HttpVersion.HTTP_2.asString()
        );

        HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpsConfig);

        // HTTP/2 Connector (no idea if this is right)
        ServerConnector https2Connector = new ServerConnector(server,
//            http2Factory,
            sslFactory//,
//            httpFactory
        );
        https2Connector.setPort(8443);

        /* Now we can create the HTTPS ServerConnector */
        ServerConnector https1Connector = new ServerConnector(
            server,
            new SslConnectionFactory(
                sslContextFactory,
                HttpVersion.HTTP_1_1.asString()
            ),
            new HttpConnectionFactory(httpsConfig)
        );
        https1Connector.setPort(8444);
        https1Connector.setIdleTimeout(500000);

        /* Now requests can flow into the server from both HTTP and HTTPS URLs to
         * their respective ports and be processed accordingly by Jetty.
         */
        server.setConnectors(new Connector[]{http1Connector, https2Connector, https1Connector});

        ALPN.debug = true; // wtf?

        server.start();
        server.join();
    }
}
