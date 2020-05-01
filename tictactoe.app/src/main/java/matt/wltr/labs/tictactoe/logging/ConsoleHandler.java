package matt.wltr.labs.tictactoe.logging;

public class ConsoleHandler extends java.util.logging.ConsoleHandler {

    public ConsoleHandler() {
        super();
        setOutputStream(System.out);
    }
}
