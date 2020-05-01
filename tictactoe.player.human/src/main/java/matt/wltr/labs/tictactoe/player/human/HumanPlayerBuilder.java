package matt.wltr.labs.tictactoe.player.human;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.PlayerBuilder;

public class HumanPlayerBuilder implements PlayerBuilder<HumanPlayer> {

    @Override
    public Class<HumanPlayer> getType() {
        return HumanPlayer.class;
    }

    @Override
    public HumanPlayer build(Game game) {
        return new HumanPlayer(game);
    }
}
