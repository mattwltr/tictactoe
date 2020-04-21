package matt.wltr.labs.tictactoe.game.random;

import matt.wltr.labs.tictactoe.game.BasePlayer;
import matt.wltr.labs.tictactoe.game.Game;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * The {@code RandomPlayer} moves randomly.
 */
public class RandomPlayer extends BasePlayer {

    public RandomPlayer(@NotNull Game game) {
        super(game);
    }

    @Override
    public void move() {
        short fieldToPlay = game.getFreeFieldNumbers().get(new Random().nextInt(game.getFreeFieldNumbers().size()));
        System.out.println(fieldToPlay);
        game.move(this, fieldToPlay);
    }

    @Override
    public String getEvolutionKey() {
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
