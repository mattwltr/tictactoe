package matt.wltr.labs.tictactoe.series;

public class GameStatistic {

    private int iteration;

    private double percentagePlayer1Wins;
    private double percentagePlayer2Wins;
    private double percentageDraws;

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public double getPercentagePlayer1Wins() {
        return percentagePlayer1Wins;
    }

    public void setPercentagePlayer1Wins(double percentagePlayer1Wins) {
        this.percentagePlayer1Wins = percentagePlayer1Wins;
    }

    public double getPercentagePlayer2Wins() {
        return percentagePlayer2Wins;
    }

    public void setPercentagePlayer2Wins(double percentagePlayer2Wins) {
        this.percentagePlayer2Wins = percentagePlayer2Wins;
    }

    public double getPercentageDraws() {
        return percentageDraws;
    }

    public void setPercentageDraws(double percentageDraws) {
        this.percentageDraws = percentageDraws;
    }
}
