package matt.wltr.labs.tictactoe.game;

import matt.wltr.labs.tictactoe.player.Player;

public class Move {

    private final Player player;

    private final int fieldNumber;

    public Move(Player player, int fieldNumber) {
        this.player = player;
        this.fieldNumber = fieldNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
