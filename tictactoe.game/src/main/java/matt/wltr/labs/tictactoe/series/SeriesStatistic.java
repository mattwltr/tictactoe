package matt.wltr.labs.tictactoe.series;

import matt.wltr.labs.tictactoe.game.Game;

import java.util.LinkedList;
import java.util.List;

public class SeriesStatistic {

    private final List<GameStatistic> gameStatistics = new LinkedList<>();
    private int player1Wins;
    private int player2Wins;
    private int draws;

    private SeriesStatistic() {
    }

    public static SeriesStatistic ofSeries(GameSeries gameSeries) {

        SeriesStatistic seriesStatistic = new SeriesStatistic();
        int i = 1;
        for (Game game : gameSeries.getGames()) {
            switch (game.getStatus()) {
                case DRAW -> seriesStatistic.draws += 1;
                case PLAYER_1_WON -> seriesStatistic.player1Wins += 1;
                case PLAYER_2_WON -> seriesStatistic.player2Wins += 1;
            }
            GameStatistic gameStatistic = new GameStatistic();
            gameStatistic.setIteration(i);
            gameStatistic.setPercentagePlayer1Wins((seriesStatistic.player1Wins * 100D) / i);
            gameStatistic.setPercentagePlayer2Wins((seriesStatistic.player2Wins * 100D) / i);
            gameStatistic.setPercentageDraws((seriesStatistic.draws * 100D) / i);
            seriesStatistic.gameStatistics.add(gameStatistic);
            i++;
        }
        return seriesStatistic;
    }

    public List<GameStatistic> getGameStatistics() {
        return gameStatistics;
    }
}
