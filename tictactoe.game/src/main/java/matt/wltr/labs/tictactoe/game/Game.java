package matt.wltr.labs.tictactoe.game;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game implements Cloneable {

    protected final Set<Move> moves = new LinkedHashSet<>();

    protected Player player1;
    protected Player player2;

    public void move(@NotNull Player player, @Min(1) @Max(9) short fieldNumber) {
        if (!player.equals(player1) && !player.equals(player2)) {
            throw new IllegalArgumentException("Player is not in the game");
        }
        if (getLastMove().map(move -> move.getPlayer().equals(player)).orElse(false)) {
            throw new IllegalArgumentException("Player cannot move twice");
        }
        if (moves.parallelStream().anyMatch(move -> move.getFieldNumber() == fieldNumber)) {
            throw new IllegalArgumentException(MessageFormat.format("Field {0} is already taken", fieldNumber));
        }
        moves.add(new Move(player, fieldNumber));
    }

    public Optional<Player> getWinner() {
        if (player1 == null || player2 == null) {
            return Optional.empty();
        }
        if (isWinner(player1)) {
            return Optional.of(player1);
        } else if (isWinner(player2)) {
            return Optional.of(player2);
        }
        return Optional.empty();
    }

    /**
     * @param player
     * @return {@code true} if {@code player} is the winner
     */
    private boolean isWinner(@NotNull Player player) {
        return getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 1 || fieldNumber == 5 || fieldNumber == 9).count() == 3
                || getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 3 || fieldNumber == 5 || fieldNumber == 7).count() == 3
                || getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 1 || fieldNumber == 2 || fieldNumber == 3).count() == 3
                || getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 4 || fieldNumber == 5 || fieldNumber == 6).count() == 3
                || getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 7 || fieldNumber == 8 || fieldNumber == 9).count() == 3
                || getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 1 || fieldNumber == 4 || fieldNumber == 7).count() == 3
                || getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 2 || fieldNumber == 5 || fieldNumber == 8).count() == 3
                || getFieldNumberStream(player).filter(fieldNumber -> fieldNumber == 3 || fieldNumber == 6 || fieldNumber == 9).count() == 3;
    }

    public Status getStatus() {
        if (canContinue()) {
            return Status.IN_PROGRESS;
        } else {
            return getWinner().map(player -> player.equals(player1) ? Status.PLAYER_1_WON : Status.PLAYER_2_WON).orElse(Status.DRAW);
        }
    }

    private Stream<Short> getFieldNumberStream(@NotNull Player player) {
        return moves.parallelStream().filter(move -> move.getPlayer().equals(player)).map(Move::getFieldNumber);
    }

    /**
     * @return {@code true} if there're 2 players in the game, it has less than 9 moves and there's no winner yet
     */
    public boolean canContinue() {
        return player1 != null && player2 != null && moves.size() < 9 && getWinner().isEmpty();
    }

    /**
     * @return the player for the next move if the game can continue
     */
    public Optional<Player> getPlayerForNextMove() {
        if (!canContinue()) {
            return Optional.empty();
        }
        if (moves.isEmpty()) {
            return Optional.of(player1);
        }
        return getLastMove().map(move -> move.getPlayer().equals(player2) ? player1 : player2);
    }

    /**
     * @return a list of playable field numbers
     */
    public List<Short> getFreeFieldNumbers() {
        return IntStream.rangeClosed(1, 9)
                .boxed()
                .parallel()
                .map(Integer::shortValue)
                .filter(shortValue -> moves.parallelStream().map(Move::getFieldNumber).noneMatch(fieldNumber -> fieldNumber == shortValue.shortValue()))
                .collect(Collectors.toList());
    }

    /**
     * Print the current game state to stdout
     */
    public void print() {
        String text = """
                ┌---┬-─-┬-─-┐
                │{6}│{7}│{8}│
                ├─-─┼───┼---┤
                │{3}│{4}│{5}│
                ├─-─┼───┼---┤
                │{0}│{1}│{2}│
                └─-─┴───┴---┘
                """.replaceAll("\\{([0-8])}", " {$1} ");

        List<String> variables = new LinkedList<>();
        for (short i = 0; i < 9; i++) {
            final short fieldNumber = (short) (i + 1);
            String characterToPrint = moves.parallelStream()
                    .filter(move -> move.getFieldNumber() == fieldNumber)
                    .map(move -> "\033[1;30m" + getSymbol(move.getPlayer()) + "\033[0m")
                    .findFirst().orElse("\033[0;37m" + (i + 1) + "\033[0m");
            variables.add(characterToPrint);
        }
        System.out.print(MessageFormat.format(text, variables.toArray()));
    }

    /**
     * Play the game
     */
    public void play() {
        while (canContinue()) {
            print();
            getPlayerForNextMove().ifPresent(player -> {
                System.out.print(MessageFormat.format("It''s your turn, {0}: ", String.valueOf(getSymbol(player))));
                player.move();
            });
        }
        print();
        getWinner().ifPresentOrElse(
                player -> System.out.println(MessageFormat.format("{0} won!", getSymbol(player))),
                () -> System.out.println("It's a draw."));
    }

    /**
     * Reset all moves of the game
     */
    public void reset() {
        moves.clear();
    }

    public void setPlayers(@NotNull Player player1, @NotNull Player player2) {
        if (this.player1 != null || this.player2 != null) {
            reset();
        }
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Set<Move> getMoves() {
        return Collections.unmodifiableSet(moves);
    }

    public Optional<Move> getLastMove() {
        return moves.isEmpty() ? Optional.empty() : moves.stream().skip(moves.size() - 1).findFirst();
    }

    public Optional<Move> getMove(short fieldNumber) {
        return moves.parallelStream().filter(move -> move.getFieldNumber() == fieldNumber).findFirst();
    }

    private char getSymbol(@NotNull Player player) {
        return player.equals(player1) ? 'x' : 'o';
    }

    public String getBoardHash() {
        return IntStream.rangeClosed(1, 9)
                .boxed()
                .map(integer -> getMove(integer.shortValue()).map(move -> move.getPlayer().equals(player1) ? "x" : "o").orElse("-"))
                .collect(Collectors.joining());
    }

    @Override
    public Game clone() {
        final Game game = new Game();
        game.setPlayers(player1.clone(game), player2.clone(game));
        moves.forEach(move -> game.moves.add(new Move(move.getPlayer().equals(player1) ? game.getPlayer1() : game.getPlayer2(), move.getFieldNumber())));
        return game;
    }

    public enum Status {
        IN_PROGRESS,
        DRAW,
        PLAYER_1_WON,
        PLAYER_2_WON
    }
}
