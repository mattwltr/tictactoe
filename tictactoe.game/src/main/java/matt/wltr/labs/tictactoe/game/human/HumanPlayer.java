package matt.wltr.labs.tictactoe.game.human;

import matt.wltr.labs.tictactoe.game.BasePlayer;
import matt.wltr.labs.tictactoe.game.Game;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The {@code HumanPlayer} moves by user input (stdin).
 */
public class HumanPlayer extends BasePlayer {

    private static final Pattern INPUT_PATTERN = Pattern.compile("[1-9]$");

    public HumanPlayer(@NotNull Game game) {
        super(game);
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
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
