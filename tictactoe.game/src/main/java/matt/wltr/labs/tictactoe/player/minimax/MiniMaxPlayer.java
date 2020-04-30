package matt.wltr.labs.tictactoe.player.minimax;

import matt.wltr.labs.tictactoe.player.BasicPlayer;
import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.Player;

import javax.validation.constraints.NotNull;

/**
 * The {@code MiniMaxPlayer} is based on the <a href="https://en.wikipedia.org/wiki/Minimax">Minimax</a> algorithm.
 * It never loses a game and it will get faster the more games are played.
 */
public class MiniMaxPlayer extends BasicPlayer {

    public MiniMaxPlayer(@NotNull Game game) {
        super(game);
    }

    @Override
    public void move() {
        MiniMaxGame miniMaxGame = new MiniMaxGame();
        miniMaxGame.setPlayers(new MiniMaxPlayer(game), new MiniMaxPlayer(game));
        game.getMoves().forEach(move -> {
            Player player = move.getPlayer().equals(game.getPlayer1()) ? miniMaxGame.getPlayer1() : miniMaxGame.getPlayer2();
            miniMaxGame.move(player, move.getFieldNumber());
        });
        int fieldToPlay = miniMaxGame.getFieldNumberForNextBestMove().orElseThrow();
        System.out.println(fieldToPlay);
        game.move(this, fieldToPlay);
    }
}
