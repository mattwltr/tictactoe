package matt.wltr.labs.tictactoe;

import matt.wltr.labs.tictactoe.game.series.GameSeries;
import matt.wltr.labs.tictactoe.player.minimax.MiniMaxPlayerBuilder;
import matt.wltr.labs.tictactoe.player.random.RandomPlayerBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

public class Main {

    private static final int DEFAULT_REPETITIONS = 100;

    static {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {

        MiniMaxPlayerBuilder miniMaxPlayerBuilder = new MiniMaxPlayerBuilder();
        RandomPlayerBuilder randomPlayerBuilder = new RandomPlayerBuilder();

        List<GameSeries> seriesList = new ArrayList<>();
        seriesList.add(new GameSeries(miniMaxPlayerBuilder, randomPlayerBuilder, DEFAULT_REPETITIONS));
//        seriesList.add(new GameSeries(randomPlayerBuilder, miniMaxPlayerBuilder, DEFAULT_REPETITIONS));

        seriesList.forEach(GameSeries::start);
        seriesList.forEach(GameSeries::printResults);

        promptExit();
    }

    private static void promptExit() {
        System.out.print("\nPress enter key to exit ... ");
        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}
