package matt.wltr.labs.tictactoe.game;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;

public abstract class BasePlayer implements Player {

    protected Game game;
    protected String name;

    public BasePlayer(@NotNull Game game) {
        this.game = game;
        name = getClass().getSimpleName();
    }

    public static Player instance(Class<? extends BasePlayer> playerClass, Game game) {
        try {
            return playerClass.getDeclaredConstructor(Game.class).newInstance(game);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Player clone(Game game) {
        try {
            return getClass().getDeclaredConstructor(Game.class).newInstance(game);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
