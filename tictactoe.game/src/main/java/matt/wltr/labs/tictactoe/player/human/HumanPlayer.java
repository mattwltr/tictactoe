package matt.wltr.labs.tictactoe.player.human;

import matt.wltr.labs.tictactoe.player.BasicPlayer;
import matt.wltr.labs.tictactoe.game.Game;

import javax.validation.constraints.NotNull;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The {@code HumanPlayer} moves by user input (stdin).
 */
public class HumanPlayer extends BasicPlayer {

    private static final Pattern INPUT_PATTERN = Pattern.compile("[1-9]$");

    public HumanPlayer(@NotNull Game game) {
        super(game);
    }

    @Override
    public void move() {
        String input = new Scanner(System.in).next();
        if (INPUT_PATTERN.matcher(input).matches()) {
            game.move(this, Integer.parseInt(input));
        }
    }
}
