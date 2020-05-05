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

    /**
     * The distance weight is a small modification to the original Minimax algorithm that adds a weight to each step until the game's end
     * (either loss, win or draw). That means the less moves are necessary to end the game, the higher the score will be that is used to determine
     * which move is the next best choice.
     */
    private static final double DISTANCE_WEIGHT = 0.9;

    private static final ConcurrentMap<String, int[]> NEXT_BEST_MOVES;
    private static final DB DB;

    static {
        DB = DBMaker.fileDB("db//" + MiniMaxGame.class.getSimpleName()).fileMmapEnable().checksumHeaderBypass().make();
        NEXT_BEST_MOVES = DB
                .hashMap("nextBestMoves", Serializer.STRING, Serializer.INT_ARRAY)
                .createOrOpen();
    }

    public Optional<Integer> getFieldNumberForNextBestMove() {
        if (!canContinue()) {
            return Optional.empty();
        }
        if (moves.isEmpty()) {
            // This is the most computation-intensive move which would in fact result with all fields having the same score, assuming the opponent plays
            // perfectly (which would result in a draw). If we assume the opponent does not play perfectly, the highest chance to win is one of
            // the corner fields (1,3,7,9), so we choose one randomly.
            int[] fieldNumbers = new int[]{1, 3, 7, 9};
            return Optional.of(fieldNumbers[new Random().nextInt(fieldNumbers.length)]);
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
            Map<MiniMaxGame, Double> scoredMiniMaxGames = getPossibleNextGames().stream().collect(Collectors.toMap(game -> game, game -> game.getGameValue(player)));
            Map.Entry<MiniMaxGame, Double> maxEntry = scoredMiniMaxGames.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow();
            fieldNumbers = scoredMiniMaxGames.entrySet().stream()
                    .filter(miniMaxGameDoubleEntry -> miniMaxGameDoubleEntry.getValue().doubleValue() == maxEntry.getValue())
                    .map(miniMaxGameDoubleEntry -> miniMaxGameDoubleEntry.getKey()
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

    private double getGameValue(Player player) {
        double gameValue;
        switch (getStatus()) {
            case IN_PROGRESS -> gameValue = getPlayerForNextMove().map(playerForNextMove -> {
                if (playerForNextMove.equals(player1) && player == Player.Player2 || playerForNextMove.equals(player2) && player == Player.Player1) {
                    return DISTANCE_WEIGHT * getPossibleNextGames().parallelStream()
                            .map(possibleNextGame -> possibleNextGame.getGameValue(player))
                            .min(Double::compareTo)
                            .orElseThrow();
                } else {
                    return DISTANCE_WEIGHT * getPossibleNextGames().parallelStream()
                            .map(possibleNextGame -> possibleNextGame.getGameValue(player))
                            .max(Double::compareTo)
                            .orElseThrow();
                }
            }).orElseThrow();
            case DRAW -> gameValue = 0D;
            case PLAYER_1_WON -> gameValue = player == Player.Player1 ? 1D : -1D;
            case PLAYER_2_WON -> gameValue = player == Player.Player2 ? 1D : -1D;
            default -> throw new IllegalStateException(MessageFormat.format("Game status {0} not yet supported", getStatus().name()));
        }
        return gameValue;
    }

    public enum Player {
        Player1,
        Player2
    }
}