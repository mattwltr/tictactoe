package matt.wltr.labs.tictactoe.player;

import matt.wltr.labs.tictactoe.game.Game;

public interface Player {

    void move();

    Game getGame();

    String getName();
}
