package matt.wltr.labs.tictactoe.player.random;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.BasicPlayer;

import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code RandomPlayer} moves randomly.
 */
public class RandomPlayer extends BasicPlayer {

    private final static Logger LOGGER = Logger.getLogger(RandomPlayer.class.getName());

    public RandomPlayer(@NotNull Game game) {
        super(game);
    }

    @Override
    public void move() {
        int fieldToPlay = game.getFreeFieldNumbers().get(new java.util.Random().nextInt(game.getFreeFieldNumbers().size()));
        LOGGER.log(Level.INFO, String.valueOf(fieldToPlay));
        game.move(this, fieldToPlay);
    }
}
