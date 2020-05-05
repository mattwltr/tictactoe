package matt.wltr.labs.tictactoe.player.tabularquality;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.game.Move;
import matt.wltr.labs.tictactoe.player.BasicPlayer;
import matt.wltr.labs.tictactoe.player.evolution.Evolution;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TabularQualityPlayer extends BasicPlayer {

    private static final Logger LOGGER = Logger.getLogger(TabularQualityPlayer.class.getName());

    /**
     * Learning rate (α). 1 means that an existing quality will be replaced completely, while 0 in theory wouldn't change anything.
     * Value range 0 < α ≤ 1
     */
    private static final double LEARNING_RATE = 0.9D;

    /**
     * Discount rate (γ), value range 0 ≤ γ ≤ 1
     * Choosing a value close to 0 means the further in the past a move has been made, the less importance it has, while a value close to 1 means all moves
     * are almost equally important for a win/draw.
     */
    private static final double DISCOUNT_RATE = 0.9999D;

    /**
     * <p>Initial quality (Q(S)ᵢ). Value range 0 < Q(S)ᵢ ≤ 1</p>
     *
     * <strong>Defensive Initialisation</strong>
     * <p>
     *     If the initial value is close to 0 the player will be very defensive and expect every move leads to a loss. If it
     *     achieves a draw before the first win, it will increase those values instead of trying something new with a potential chance to win.
     * </p>
     *
     * <strong>Offensive Initialisation</strong>
     * <p>
     *     If the initial value is close to 1 the player will be very offensive and assume every move leads to a win by actively choosing
     *     different approaches. Therefore it's less likely that it will achieve a draw in first line and thus it will take longer to achieve a good
     *     winning quote.
     * </p>
     */
    private static final double INITIAL_QUALITY = 0.001D;

    /**
     * The player will get better over time the more games are played. Previous "knowledge" will
     * be stored in a local database (file). The evolution key is used to store/access this "knowledge".
     */
    private final String evolutionKey;

    private final Evolution evolution;

    private final DB db;

    private final ConcurrentMap<String, Map<Integer, Double>> qualityLookupTable;

    public TabularQualityPlayer(@NotNull Game game, @NotBlank String evolutionKey, @NotNull Evolution evolution) {
        super(game);
        this.evolutionKey = evolutionKey;
        this.evolution = evolution;
        db = DBMaker.fileDB("db//" + evolutionKey).fileMmapEnable().checksumHeaderBypass().make();
        //noinspection unchecked
        ConcurrentMap<String, Map<Integer, Double>> qualityLookupTable = (ConcurrentMap<String, Map<Integer, Double>>) db.hashMap("qualityLookupTable").createOrOpen();
        if (evolution == Evolution.FROZEN) {
            this.qualityLookupTable = new ConcurrentHashMap<>(qualityLookupTable);
            db.close();
        } else {
            this.qualityLookupTable = qualityLookupTable;
        }
    }

    @Override
    public void move() {
        String boardHash = game.getBoardHash();
        List<Integer> freeFieldNumbers = game.getFreeFieldNumbers();
        int fieldToPlay;
        if (qualityLookupTable.containsKey(boardHash)) {
            double maxValue = qualityLookupTable.get(boardHash).entrySet()
                    .parallelStream()
                    .filter(entry -> freeFieldNumbers.contains(entry.getKey()))
                    .max(Map.Entry.comparingByValue()).orElseThrow()
                    .getValue();
            List<Map.Entry<Integer, Double>> entriesWithMaxValue = qualityLookupTable.get(boardHash).entrySet()
                    .parallelStream()
                    .filter(entry -> freeFieldNumbers.contains(entry.getKey()) && entry.getValue() == maxValue)
                    .collect(Collectors.toList());
            fieldToPlay = entriesWithMaxValue.get(new java.util.Random().nextInt(entriesWithMaxValue.size())).getKey();
        } else {
            fieldToPlay = freeFieldNumbers.get(new java.util.Random().nextInt(freeFieldNumbers.size()));
        }
        LOGGER.log(Level.INFO, String.valueOf(fieldToPlay));
        game.move(this, fieldToPlay);
    }

    @Override
    public void onGameEnd() {
        if (evolution == Evolution.FROZEN) {
            return;
        }
        List<Move> moves = new ArrayList<>(game.getMoves());
        boolean playersLastMove = true;
        double nextMoveQuality = 0;
        for (int i = moves.size() - 1; i >= 0; i--) {
            Move move = moves.get(i);
            Move previousMove = i > 0 ? moves.get(i - 1) : null;
            String previousMoveBoardHash = game.getBoardHash(previousMove);
            if (move.getPlayer().equals(this)) {
                Map<Integer, Double> lookupRow = getLookupRow(previousMoveBoardHash);
                double quality;
                if (playersLastMove) {
                    quality = game.getWinner().map(player -> player.equals(this) ? 1D : 0D).orElse(0.5);
                    playersLastMove = false;
                } else {
                    double currentMoveQuality = lookupRow.get(move.getFieldNumber());
                    quality = calculateQuality(currentMoveQuality, nextMoveQuality);
                }
                lookupRow.put(move.getFieldNumber(), quality);
                qualityLookupTable.put(previousMoveBoardHash, lookupRow);
                nextMoveQuality = lookupRow.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getValue).orElseThrow();
            }
        }
        db.close();
    }

    /**
     * @param currentMoveQuality Q(S,A), the current quality (or initial quality if it hasn't been calculated before)
     * @param nextMoveQuality Q(S',A), the quality of S' (the next state) that is followed by action A
     * @return
     *  Q(S,A) = Q(S,A) * (1 − α) + α ∗ γ ∗ maxaQ(S′,a)
     *            S = the current state
     *            A = the current action
     *            α = learning rate, 0 < α ≤ 1
     *            γ = discount factor, 0 < γ < 1
     *  maxaQ(S′,a) = the Q value of the best move in the following state (S′ = the state after doing A)
     */
    private double calculateQuality(double currentMoveQuality, double nextMoveQuality) {
        return currentMoveQuality * (1D - LEARNING_RATE) + LEARNING_RATE * DISCOUNT_RATE * nextMoveQuality;
    }

    private Map<Integer, Double> getLookupRow(String boardHash) {
        Map<Integer, Double> lookupRow;
        if (qualityLookupTable.containsKey(boardHash)) {
            lookupRow = qualityLookupTable.get(boardHash);
        } else {
            lookupRow = new HashMap<>();
            IntStream.rangeClosed(1, 9)
                    .boxed()
                    .forEach(fieldNumber -> lookupRow.put(fieldNumber, INITIAL_QUALITY));
        }
        return lookupRow;
    }
}
