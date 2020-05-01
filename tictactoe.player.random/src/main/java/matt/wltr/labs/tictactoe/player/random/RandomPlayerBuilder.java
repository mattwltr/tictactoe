package matt.wltr.labs.tictactoe.player.random;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.PlayerBuilder;

public class RandomPlayerBuilder implements PlayerBuilder<RandomPlayer> {

    @Override
    public Class<RandomPlayer> getType() {
        return RandomPlayer.class;
    }

    @Override
    public RandomPlayer build(Game game) {
        return new RandomPlayer(game);
    }
}
