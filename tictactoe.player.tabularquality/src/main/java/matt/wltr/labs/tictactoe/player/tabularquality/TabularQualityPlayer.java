package matt.wltr.labs.tictactoe.player.tabularquality;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.BasicPlayer;
import matt.wltr.labs.tictactoe.util.Random;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TabularQualityPlayer extends BasicPlayer {

    private final String evolutionKey;

    public TabularQualityPlayer(@NotNull Game game) {
        this(game, new Random().nextString());
    }

    public TabularQualityPlayer(@NotNull Game game, @NotBlank String evolutionKey) {
        super(game);
        this.evolutionKey = evolutionKey;
    }

    @Override
    public void move() {

    }
}
