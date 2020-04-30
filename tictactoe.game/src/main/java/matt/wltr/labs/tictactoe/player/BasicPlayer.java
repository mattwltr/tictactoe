package matt.wltr.labs.tictactoe.player;

import matt.wltr.labs.tictactoe.game.Game;

import javax.validation.constraints.NotNull;

public abstract class BasicPlayer implements Player {

    protected Game game;
    protected String name;

    public BasicPlayer(@NotNull Game game) {
        this.game = game;
        name = getClass().getSimpleName();
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public String getName() {
        return name;
    }
}
