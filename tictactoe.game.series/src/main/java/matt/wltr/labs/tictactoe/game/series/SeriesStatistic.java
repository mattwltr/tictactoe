package matt.wltr.labs.tictactoe.game.series;

import matt.wltr.labs.tictactoe.game.Game;

import java.util.LinkedList;
import java.util.List;

public class SeriesStatistic {

    private final List<Statistic> statistics = new LinkedList<>();
    private int player1Wins;
    private int player2Wins;
    private int draws;

    private SeriesStatistic() {
    }

    public static SeriesStatistic ofSeries(GameSeries gameSeries) {
        SeriesStatistic seriesStatistic = new SeriesStatistic();
        int i = 1;
        int batchIteration = 1;
        int batchSize = (int) Math.floor(gameSeries.getGames().size() / 100D); // 1% of dataset
        int player1WinsPerBatch = 0;
        int player2WinsPerBatch = 0;
        int drawsPerBatch = 0;
        for (Game game : gameSeries.getGames()) {
            switch (game.getStatus()) {
                case DRAW -> {
                    seriesStatistic.draws += 1;
                    drawsPerBatch += 1;
                }
                case PLAYER_1_WON -> {
                    seriesStatistic.player1Wins += 1;
                    player1WinsPerBatch += 1;
                }
                case PLAYER_2_WON -> {
                    seriesStatistic.player2Wins += 1;
                    player2WinsPerBatch += 1;
                }
            }
            if (batchIteration == batchSize || i == gameSeries.getGames().size()) {
                Statistic statistic = new Statistic();
                statistic.setIteration(i);
                statistic.setPercentagePlayer1Wins((player1WinsPerBatch * 100D) / batchSize);
                statistic.setPercentagePlayer2Wins((player2WinsPerBatch * 100D) / batchSize);
                statistic.setPercentageDraws((drawsPerBatch * 100D) / batchSize);
                seriesStatistic.statistics.add(statistic);
                player1WinsPerBatch = 0;
                player2WinsPerBatch = 0;
                drawsPerBatch = 0;
                batchIteration = 0;
            }
            batchIteration++;
            i++;
        }
        return seriesStatistic;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public int getPlayedGames() {
        return player1Wins + player2Wins + draws;
    }

    public double getPercentagePlayer1Wins() {
        return (player1Wins * 100D) / getPlayedGames();
    }

    public double getPercentagePlayer2Wins() {
        return (player2Wins * 100D) / getPlayedGames();
    }

    public double getPercentageDraws() {
        return (draws * 100D) / getPlayedGames();
    }
}
