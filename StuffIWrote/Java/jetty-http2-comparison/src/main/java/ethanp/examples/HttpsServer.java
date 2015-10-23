package ethanp.examples;

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Ethan Petuchowski 10/22/15
 * <p>
 * This is based on an example from here: http://www.eclipse.org/jetty/documentation/current/embedded-examples.html
 */
public class HttpsServer {

    public static final String PASSWORD = "password";
    public static final File keystoreFile = new File("keystore");

    public static void main(String[] args) throws Exception {

        // bomb out if keystore doesn't exist
        if (!keystoreFile.exists()) throw new FileNotFoundException(keystoreFile.getAbsolutePath());

        Server server = new Server();
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setOutputBufferSize(32768);

        // make the regular (unencrypted) HTTP server connector
        ServerConnector plainHttpConn = new ServerConnector(server, new HttpConnectionFactory(http_config));
        plainHttpConn.setPort(8080);

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
        sslContextFactory.setKeyStorePassword(PASSWORD);
        sslContextFactory.setKeyManagerPassword(PASSWORD);

        sslContextFactory.setExcludeCipherSuites(
            "SSL_RSA_WITH_DES_CBC_SHA",
            "SSL_DHE_RSA_WITH_DES_CBC_SHA",
            "SSL_DHE_DSS_WITH_DES_CBC_SHA",
            "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
            "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");


        // Resolve the HTTPS connection before passing request to Jetty
        HttpConfiguration httpsConfig = new HttpConfiguration(http_config);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        HTTP2ServerConnectionFactory http2Factory = new HTTP2ServerConnectionFactory(httpsConfig);

        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol("h2");

        SslConnectionFactory sslConnFactory = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

        // HTTP/2 Connector (no idea if this is right)
        ServerConnector https2Conn = new ServerConnector(
            server,
            sslConnFactory,
            alpn,
            http2Factory,
            new HttpConnectionFactory(httpsConfig)
        );
        https2Conn.setPort(8443);

        // connect the different ports to the different protocols
        server.setConnectors(new Connector[]{plainHttpConn, https2Conn});
        server.setHandler(new AbstractHandler() {
            @Override public void handle(
                String target,
                Request baseRequest,
                HttpServletRequest request,
                HttpServletResponse response
            ) throws IOException, ServletException {
                response.setContentType("text/html; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("<h1>Hello World</h1>");
                baseRequest.setHandled(true);
                System.out.println("received request for: "+target);
            }
        });
        server.start();
        server.join();
    }
}
