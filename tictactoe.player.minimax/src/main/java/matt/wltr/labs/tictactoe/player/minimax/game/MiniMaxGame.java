package matt.wltr.labs.tictactoe.player.minimax.game;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.game.Move;
import matt.wltr.labs.tictactoe.player.minimax.MiniMaxPlayer;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class MiniMaxGame extends Game {

    private static final ConcurrentMap<String, int[]> NEXT_BEST_MOVES;
    private static final DB DB;

    static {
        DB = DBMaker.fileDB("db//" + MiniMaxGame.class.getName()).fileMmapEnable().checksumHeaderBypass().make();
        NEXT_BEST_MOVES = DB
                .hashMap("nextBestMoves", Serializer.STRING, Serializer.INT_ARRAY)
                .createOrOpen();
    }

    public Optional<Integer> getFieldNumberForNextBestMove() {
        if (!canContinue()) {
            return Optional.empty();
        }
        if (moves.isEmpty()) {
            // this is the most computation-intensive move which would in fact result with all fields having the same score,
            // so we're taking a shortcut and return a random field number
            return Optional.of(Math.max(1, new Random().nextInt(10)));
        }
        if (moves.size() == 1) {
            // this is a computation-intensive move as well which would result in either a corner field (1,3,7,9) or the middle (5) so we're making
            // the decision with a simple if/else
            Move move = moves.iterator().next();
            if (move.getFieldNumber() == 5) {
                int[] fieldNumbers = new int[]{1, 3, 7, 9};
                return Optional.of(fieldNumbers[new Random().nextInt(fieldNumbers.length)]);
            } else {
                return Optional.of(5);
            }
        }
        Player player = getPlayerForNextMove().map(playerForNextMove -> playerForNextMove.equals(player1) ? Player.Player1 : Player.Player2).orElseThrow();
        return Optional.of(getRandomPossibleNextFieldNumber(player));
    }

    private int getRandomPossibleNextFieldNumber(Player player) {
        String boardHash = getBoardHash();
        int[] fieldNumbers;
        if (NEXT_BEST_MOVES.containsKey(boardHash)) {
            fieldNumbers = NEXT_BEST_MOVES.get(boardHash);
        } else {
            Map<MiniMaxGame, Integer> scoredMiniMaxGames = getPossibleNextGames().parallelStream().collect(Collectors.toMap(game -> game, game -> game.getGameValue(player)));
            int gameValue = getGameValue(player);
            fieldNumbers = scoredMiniMaxGames.entrySet().parallelStream()
                    .filter(miniMaxGameIntegerEntry -> miniMaxGameIntegerEntry.getValue() == gameValue)
                    .map(miniMaxGameIntegerEntry -> miniMaxGameIntegerEntry.getKey()
                            .getLastMove()
                            .map(Move::getFieldNumber)
                            .orElseThrow())
                    .mapToInt(value -> value)
                    .toArray();
            NEXT_BEST_MOVES.put(getBoardHash(), fieldNumbers);
            DB.commit();
        }
        return fieldNumbers[(new Random().nextInt(fieldNumbers.length))];
    }

    private List<MiniMaxGame> getPossibleNextGames() {
        if (!canContinue()) {
            return new ArrayList<>();
        }
        return getFreeFieldNumbers().parallelStream().map(fieldNumber -> {
            MiniMaxGame possibleNextGame = new MiniMaxGame();
            possibleNextGame.setPlayers(new MiniMaxPlayer(player1.getGame()), new MiniMaxPlayer(player2.getGame()));
            getMoves().forEach(move -> {
                matt.wltr.labs.tictactoe.player.Player player = move.getPlayer().equals(getPlayer1()) ? possibleNextGame.getPlayer1() : possibleNextGame.getPlayer2();
                possibleNextGame.move(player, move.getFieldNumber());
            });
            possibleNextGame.getPlayerForNextMove().ifPresent(player -> possibleNextGame.move(player, fieldNumber));
            return possibleNextGame;
        }).collect(Collectors.toList());
    }

    private int getGameValue(Player player) {
        int gameValue;
        switch (getStatus()) {
            case IN_PROGRESS -> gameValue = getPlayerForNextMove().map(playerForNextMove -> {
                if (playerForNextMove.equals(player1) && player == Player.Player2 || playerForNextMove.equals(player2) && player == Player.Player1) {
                    return getPossibleNextGames().parallelStream()
                            .map(possibleNextGame -> possibleNextGame.getGameValue(player))
                            .min(Integer::compareTo)
                            .orElseThrow();
                } else {
                    return getPossibleNextGames().parallelStream()
                            .map(possibleNextGame -> possibleNextGame.getGameValue(player))
                            .max(Integer::compareTo)
                            .orElseThrow();
                }
            }).orElseThrow();
            case DRAW -> gameValue = 0;
            case PLAYER_1_WON -> gameValue = player == Player.Player1 ? 1 : -1;
            case PLAYER_2_WON -> gameValue = player == Player.Player2 ? 1 : -1;
            default -> throw new IllegalStateException(MessageFormat.format("Game status {0} not yet supported", getStatus().name()));
        }
        return gameValue;
    }

    public enum Player {
        Player1,
        Player2
    }
}
