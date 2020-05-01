package matt.wltr.labs.tictactoe.game.series.util;

import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;

public class HttpServer {

    private static final com.sun.net.httpserver.HttpServer HTTP_SERVER;

    /**
     * registered context paths that serve something
     */
    private static final Set<String> CONTEXT_PATHS = new HashSet<>();

    static {
        try {
            HTTP_SERVER = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress("localhost", getAvailablePort().orElseThrow()), 0);
            HTTP_SERVER.setExecutor(Executors.newFixedThreadPool(10));
            HTTP_SERVER.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<Integer> getAvailablePort() {
        for (int i = 8080; i < 9000; i++) {
            if (isPortAvailable(i)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private static boolean isPortAvailable(int port) {
        boolean portAvailable;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            portAvailable = true;
        } catch (IOException e) {
            portAvailable = false;
        }
        return portAvailable;
    }

    public static void createContext(String path, HttpHandler httpHandler) {
        HTTP_SERVER.createContext(path, httpHandler);
        CONTEXT_PATHS.add(path);
    }

    public static void removeContext(String path) {
        HTTP_SERVER.removeContext(path);
        CONTEXT_PATHS.remove(path);
    }

    public static Set<String> getContextPaths() {
        return Collections.unmodifiableSet(CONTEXT_PATHS);
    }

    public static String getOrigin() {
        return "http://" + HTTP_SERVER.getAddress().getHostString() + ":" + HTTP_SERVER.getAddress().getPort();
    }
}
