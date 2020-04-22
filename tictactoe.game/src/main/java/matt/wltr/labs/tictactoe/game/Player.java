package matt.wltr.labs.tictactoe.game;

import javax.validation.constraints.NotNull;

public interface Player {

    void move();

    Player clone(@NotNull Game game);

    /**
     * @return evolution key. Depending on its implementation, the player might get better over time the more games are played. Previous "knowledge" can
     * be stored in a cache or long term storage (e.g. filesystem, database). The evolution key is used to store/access that "knowledge".
     */
    String getEvolutionKey();
}
