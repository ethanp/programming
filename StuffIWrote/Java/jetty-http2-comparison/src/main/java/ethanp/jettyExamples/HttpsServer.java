package ethanp.jettyExamples;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Ethan Petuchowski 10/22/15
 *
 * This is an example from here:
 *
 *      http://www.eclipse.org/jetty/documentation/current/embedded-examples.html
 */
public class HttpsServer {

    private static final String PASSWORD = "password";
    private static final File keystoreFile = new File("keystore");

    public static void main( String[] args ) throws Exception {

        if (!keystoreFile.exists())
            throw new FileNotFoundException(keystoreFile.getAbsolutePath());

        /* we'll set ports on each of the connectors rather than here on the server */
        Server server = new Server();

        /* make a base HTTP Config that has a reasonable output buffer size */
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);

        /* make the regular (unencrypted) HTTP server connector */
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(8080);
        http.setIdleTimeout(30000);

        /* This factory creates SSL contexts.
         * It uses the certificate in our little keystore to do so.
         */
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
        sslContextFactory.setKeyStorePassword(PASSWORD);
        sslContextFactory.setKeyManagerPassword(PASSWORD);

        /* First we clone the old http_config */
        HttpConfiguration https_config = new HttpConfiguration(http_config);

        /* Add a `SecureRequestCustomizer`, which enables a new Connector to resolve
         * the HTTPS connection before handing control over to the Jetty server.
         */
        https_config.addCustomizer(new SecureRequestCustomizer());

        /* Now we can create the HTTPS ServerConnector */
        ServerConnector https = new ServerConnector(
            server,
            new SslConnectionFactory(
                sslContextFactory,
                HttpVersion.HTTP_1_1.asString()
            ),
            new HttpConnectionFactory(https_config)
        );
        https.setPort(8443);
        https.setIdleTimeout(500000);

        /* Now requests can flow into the server from both HTTP and HTTPS URLs to
         * their respective ports and be processed accordingly by Jetty.
         */
        server.setConnectors(new Connector[] { http, https });

        server.setHandler(new HelloWorld());
        server.start();
        server.join();
    }
}
