package matt.wltr.labs.tictactoe;

import matt.wltr.labs.tictactoe.game.minimax.MiniMaxPlayer;
import matt.wltr.labs.tictactoe.game.random.RandomPlayer;
import matt.wltr.labs.tictactoe.series.GameSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int DEFAULT_REPETITIONS = 1000;

    public static void main(String... args) {

        List<GameSeries> seriesList = new ArrayList<>();
        seriesList.add(new GameSeries(MiniMaxPlayer.class, RandomPlayer.class, DEFAULT_REPETITIONS));
        seriesList.add(new GameSeries(RandomPlayer.class, MiniMaxPlayer.class, DEFAULT_REPETITIONS));

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
