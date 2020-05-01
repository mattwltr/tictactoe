package matt.wltr.labs.tictactoe.player.tabularquality;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.BasicPlayer;
import matt.wltr.labs.tictactoe.util.Random;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.logging.Logger;

public class TabularQualityPlayer extends BasicPlayer {

    private final static Logger LOGGER = Logger.getLogger(TabularQualityPlayer.class.getName());

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
