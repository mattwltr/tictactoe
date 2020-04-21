package matt.wltr.labs.tictactoe.game.minimax;

import matt.wltr.labs.tictactoe.game.BasePlayer;
import matt.wltr.labs.tictactoe.game.Game;

import javax.validation.constraints.NotNull;

/**
 * The {@code MiniMaxPlayer} is based on the <a href="https://en.wikipedia.org/wiki/Minimax">Minimax</a> algorithm. It never loses a game.
 */
public class MiniMaxPlayer extends BasePlayer {

    public MiniMaxPlayer(@NotNull Game game) {
        super(game);
    }

    @Override
    public void move() {
        MiniMaxGame miniMaxGame = new MiniMaxGame(game);
        short fieldToPlay = miniMaxGame.getFieldNumberForNextBestMove().orElseThrow();
        System.out.println(fieldToPlay);
        game.move(this, fieldToPlay);
    }

    @Override
    public String getEvolutionKey() {
        return getClass().getSimpleName();
    }
}
