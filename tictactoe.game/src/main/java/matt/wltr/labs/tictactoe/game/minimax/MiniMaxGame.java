package matt.wltr.labs.tictactoe.game.minimax;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.game.Move;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

class MiniMaxGame extends Game {

    private static final Map<String, List<Short>> BEST_NEXT_MOVES = new HashMap<>();

    MiniMaxGame(Game game) {
        player1 = game.getPlayer1().clone(this);
        player2 = game.getPlayer2().clone(this);
        moves.addAll(game.getMoves().stream()
                .map(move -> new Move(move.getPlayer().equals(game.getPlayer1()) ? player1 : player2, move.getFieldNumber()))
                .collect(Collectors.toList()));
    }

    Optional<Short> getFieldNumberForNextBestMove() {
        if (!canContinue()) {
            return Optional.empty();
        }
        if (moves.isEmpty()) {
            // this is the most computation-intensive move which would in fact result with all fields having the same score,
            // so we're taking a shortcut and return a random field number
            return Optional.of((short) (Math.max(1, new Random().nextInt(10))));
        }
        if (moves.size() == 1) {
            // this is a computation-intensive move as well which would result in either a corner field (1,3,7,9) or the middle (5) so we're making
            // the decision with a simple if/else
            Move move = moves.iterator().next();
            if (move.getFieldNumber() == 5) {
                short[] fieldNumbers = new short[]{1, 3, 7, 9};
                return Optional.of(fieldNumbers[new Random().nextInt(fieldNumbers.length)]);
            } else {
                return Optional.of((short) 5);
            }
        }
        Player player = getPlayerForNextMove().map(playerForNextMove -> playerForNextMove.equals(player1) ? Player.Player1 : Player.Player2).orElseThrow();
        return Optional.of(getRandomPossibleNextFieldNumber(player));
    }

    private short getRandomPossibleNextFieldNumber(Player player) {
        String boardHash = getBoardHash();
        List<Short> fieldNumbers;
        if (BEST_NEXT_MOVES.containsKey(boardHash)) {
            fieldNumbers = BEST_NEXT_MOVES.get(boardHash);
        } else {
            Map<MiniMaxGame, Short> scoredMiniMaxGames = getPossibleNextGames().parallelStream().collect(Collectors.toMap(game -> game, game -> game.getGameValue(player)));
            int gameValue = getGameValue(player);
            fieldNumbers = scoredMiniMaxGames.entrySet().parallelStream()
                    .filter(miniMaxGameIntegerEntry -> miniMaxGameIntegerEntry.getValue() == gameValue)
                    .map(miniMaxGameIntegerEntry -> miniMaxGameIntegerEntry.getKey()
                            .getLastMove()
                            .map(Move::getFieldNumber)
                            .orElseThrow())
                    .collect(Collectors.toList());
            BEST_NEXT_MOVES.put(getBoardHash(), fieldNumbers);
        }
        return fieldNumbers.get(new Random().nextInt(fieldNumbers.size()));
    }

    private List<MiniMaxGame> getPossibleNextGames() {
        if (!canContinue()) {
            return new ArrayList<>();
        }
        return getFreeFieldNumbers().parallelStream().map(fieldNumber -> {
            MiniMaxGame possibleNextGame = new MiniMaxGame(clone());
            possibleNextGame.getPlayerForNextMove().ifPresent(player -> possibleNextGame.move(player, fieldNumber));
            return possibleNextGame;
        }).collect(Collectors.toList());
    }

    private short getGameValue(Player player) {
        short gameValue;
        switch (getStatus()) {
            case IN_PROGRESS -> gameValue = getPlayerForNextMove().map(playerForNextMove -> {
                if (playerForNextMove.equals(player1) && player == Player.Player2 || playerForNextMove.equals(player2) && player == Player.Player1) {
                    return getPossibleNextGames().parallelStream()
                            .map(possibleNextGame -> possibleNextGame.getGameValue(player))
                            .min(Short::compareTo)
                            .orElseThrow();
                } else {
                    return getPossibleNextGames().parallelStream()
                            .map(possibleNextGame -> possibleNextGame.getGameValue(player))
                            .max(Short::compareTo)
                            .orElseThrow();
                }
            }).orElseThrow();
            case DRAW -> gameValue = 0;
            case PLAYER_1_WON -> gameValue = player == Player.Player1 ? (short) 1 : -1;
            case PLAYER_2_WON -> gameValue = player == Player.Player2 ? (short) 1 : -1;
            default -> throw new IllegalStateException(MessageFormat.format("Game status {0} not yet supported", getStatus().name()));
        }
        return gameValue;
    }

    enum Player {
        Player1,
        Player2
    }
}
