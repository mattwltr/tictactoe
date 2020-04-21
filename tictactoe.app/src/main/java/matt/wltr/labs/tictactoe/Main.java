package matt.wltr.labs.tictactoe;

import matt.wltr.labs.tictactoe.game.GameSeries;
import matt.wltr.labs.tictactoe.game.minimax.MiniMaxPlayer;

public class Main {

    public static void main(String... args) {
        GameSeries series = new GameSeries();
        series.start(MiniMaxPlayer.class, MiniMaxPlayer.class, 500);
        series.printResults();
    }
}
