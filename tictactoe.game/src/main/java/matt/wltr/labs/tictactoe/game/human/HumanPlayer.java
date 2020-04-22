package matt.wltr.labs.tictactoe.game.human;

import matt.wltr.labs.tictactoe.game.BasePlayer;
import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.util.Random;

import javax.validation.constraints.NotNull;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The {@code HumanPlayer} moves by user input (stdin).
 */
public class HumanPlayer extends BasePlayer {

    private static final Pattern INPUT_PATTERN = Pattern.compile("[1-9]$");

    private final String evolutionKey;

    public HumanPlayer(@NotNull Game game) {
        super(game);
        evolutionKey = new Random().nextString();
    }

    @Override
    public void move() {
        String input = new Scanner(System.in).next();
        if (INPUT_PATTERN.matcher(input).matches()) {
            game.move(this, Short.parseShort(input));
        }
    }

    @Override
    public String getEvolutionKey() {
        return evolutionKey;
    }
}
