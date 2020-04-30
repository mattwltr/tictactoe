package matt.wltr.labs.tictactoe.player;

import matt.wltr.labs.tictactoe.game.Game;

public interface PlayerBuilder<T extends Player> {

    Class<T> getType();

    T build(Game game);
}
