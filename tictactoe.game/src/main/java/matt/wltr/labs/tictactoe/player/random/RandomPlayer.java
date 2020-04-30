package matt.wltr.labs.tictactoe.player.random;

import matt.wltr.labs.tictactoe.player.BasicPlayer;
import matt.wltr.labs.tictactoe.game.Game;

import javax.validation.constraints.NotNull;

/**
 * The {@code RandomPlayer} moves randomly.
 */
public class RandomPlayer extends BasicPlayer {

    public RandomPlayer(@NotNull Game game) {
        super(game);
    }

    @Override
    public void move() {
        int fieldToPlay = game.getFreeFieldNumbers().get(new java.util.Random().nextInt(game.getFreeFieldNumbers().size()));
        System.out.println(fieldToPlay);
        game.move(this, fieldToPlay);
    }
}
