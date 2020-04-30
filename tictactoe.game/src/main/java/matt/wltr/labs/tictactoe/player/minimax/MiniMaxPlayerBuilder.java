package matt.wltr.labs.tictactoe.player.minimax;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.PlayerBuilder;

public class MiniMaxPlayerBuilder implements PlayerBuilder<MiniMaxPlayer> {

    @Override
    public Class<MiniMaxPlayer> getType() {
        return MiniMaxPlayer.class;
    }

    @Override
    public MiniMaxPlayer build(Game game) {
        return new MiniMaxPlayer(game);
    }
}
