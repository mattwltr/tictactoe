package matt.wltr.labs.tictactoe.game.random;

import matt.wltr.labs.tictactoe.game.BasePlayer;
import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.util.Random;

import javax.validation.constraints.NotNull;

/**
 * The {@code RandomPlayer} moves randomly.
 */
public class RandomPlayer extends BasePlayer {

    private final String evolutionKey;

    public RandomPlayer(@NotNull Game game) {
        super(game);
        evolutionKey = new Random().nextString();
    }

    @Override
    public void move() {
        short fieldToPlay = game.getFreeFieldNumbers().get(new java.util.Random().nextInt(game.getFreeFieldNumbers().size()));
        System.out.println(fieldToPlay);
        game.move(this, fieldToPlay);
    }

    @Override
    public String getEvolutionKey() {
        return evolutionKey;
    }
}
