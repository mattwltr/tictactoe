package matt.wltr.labs.tictactoe.logging;

import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ConsoleHandler extends Handler {

    private static final Formatter FORMATTER = new SimpleFormatter();

    public ConsoleHandler() {
        super();
    }

    @Override
    public void publish(LogRecord record) {
        System.out.print(FORMATTER.format(record));
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
