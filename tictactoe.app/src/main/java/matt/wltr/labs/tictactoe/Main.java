package matt.wltr.labs.tictactoe;

import matt.wltr.labs.tictactoe.game.minimax.MiniMaxPlayer;
import matt.wltr.labs.tictactoe.game.random.RandomPlayer;
import matt.wltr.labs.tictactoe.series.GameSeries;

import java.io.IOException;

public class Main {

    public static void main(String... args) {

        GameSeries miniMaxVsRandom = new GameSeries();
        miniMaxVsRandom.start(MiniMaxPlayer.class, RandomPlayer.class, 5000);

        GameSeries randomVsMiniMax = new GameSeries();
        randomVsMiniMax.start(RandomPlayer.class, MiniMaxPlayer.class, 5000);

        miniMaxVsRandom.printResults();
        randomVsMiniMax.printResults();

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
