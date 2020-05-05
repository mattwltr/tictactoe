package matt.wltr.labs.tictactoe;

import matt.wltr.labs.tictactoe.game.series.GameSeries;
import matt.wltr.labs.tictactoe.player.evolution.Evolution;
import matt.wltr.labs.tictactoe.player.human.HumanPlayerBuilder;
import matt.wltr.labs.tictactoe.player.minimax.MiniMaxPlayerBuilder;
import matt.wltr.labs.tictactoe.player.random.RandomPlayerBuilder;
import matt.wltr.labs.tictactoe.player.tabularquality.TabularQualityPlayerBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;

public class Main {

    private static final int DEFAULT_REPETITIONS = 2000;

    private static final HumanPlayerBuilder HUMAN_PLAYER_BUILDER = new HumanPlayerBuilder();
    private static final MiniMaxPlayerBuilder MINI_MAX_PLAYER_BUILDER = new MiniMaxPlayerBuilder();
    private static final RandomPlayerBuilder RANDOM_PLAYER_BUILDER = new RandomPlayerBuilder();
    private static final TabularQualityPlayerBuilder TABULAR_QUALITY_PLAYER_BUILDER = new TabularQualityPlayerBuilder("MiniMax_vs_TabularQuality");

    static {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {

        cleanup();

        List<GameSeries> seriesList = new ArrayList<>();

        // MiniMax vs Random
        GameSeries miniMaxVsRandom = new GameSeries(MINI_MAX_PLAYER_BUILDER, RANDOM_PLAYER_BUILDER, DEFAULT_REPETITIONS);
        miniMaxVsRandom.start();
        seriesList.add(miniMaxVsRandom);

        // Random vs MiniMax
        GameSeries randomVsMiniMax = new GameSeries(RANDOM_PLAYER_BUILDER, MINI_MAX_PLAYER_BUILDER, DEFAULT_REPETITIONS);
        randomVsMiniMax.start();
        seriesList.add(randomVsMiniMax);

        // MiniMax vs TabularQuality
        GameSeries miniMaxVsTabularQuality = new GameSeries(MINI_MAX_PLAYER_BUILDER, TABULAR_QUALITY_PLAYER_BUILDER, DEFAULT_REPETITIONS);
        miniMaxVsTabularQuality.start();
        seriesList.add(miniMaxVsTabularQuality);

        // TabularQuality vs MiniMax without further evolution
        TABULAR_QUALITY_PLAYER_BUILDER.setEvolution(Evolution.FROZEN);
        GameSeries tabularQualityVsMiniMaxWithFrozenEvolution = new GameSeries(TABULAR_QUALITY_PLAYER_BUILDER, MINI_MAX_PLAYER_BUILDER, DEFAULT_REPETITIONS);
        tabularQualityVsMiniMaxWithFrozenEvolution.setDescription("""
                This is the evaluation of how good the Q table of "MiniMax (P1) vs TabularQuality (P2)" works
                without further evolution in this series.""");
        tabularQualityVsMiniMaxWithFrozenEvolution.start();
        seriesList.add(tabularQualityVsMiniMaxWithFrozenEvolution);

        // TabularQuality vs MiniMax with "MiniMax vs TabularQuality" as base
        TABULAR_QUALITY_PLAYER_BUILDER.setEvolution(Evolution.IN_PROGRESS);
        GameSeries tabularQualityVsMiniMax = new GameSeries(TABULAR_QUALITY_PLAYER_BUILDER, MINI_MAX_PLAYER_BUILDER, DEFAULT_REPETITIONS);
        tabularQualityVsMiniMax.setDescription("""
                Further training the evolution "MiniMax (P1) vs TabularQuality (P2)" with reversed positions.""");
        tabularQualityVsMiniMax.start();
        seriesList.add(tabularQualityVsMiniMax);

        seriesList.forEach(GameSeries::logResults);

        cleanup();
        promptExit();
    }

    private static void cleanup() {
        File directory = new File("db");
        File[] directoryListing = directory.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isFile() && !Arrays.asList("MiniMaxGame", ".gitignore").contains(child.getName())) {
                    //noinspection ResultOfMethodCallIgnored
                    child.delete();
                }
            }
        }
    }

    private static void promptExit() {
        System.out.print("\nPress enter key to exit ... ");
        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}
