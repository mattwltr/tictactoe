package matt.wltr.labs.tictactoe.game;

public class Move {

    private final Player player;

    private final short fieldNumber;

    public Move(Player player, short fieldNumber) {
        this.player = player;
        this.fieldNumber = fieldNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public short getFieldNumber() {
        return fieldNumber;
    }
}
