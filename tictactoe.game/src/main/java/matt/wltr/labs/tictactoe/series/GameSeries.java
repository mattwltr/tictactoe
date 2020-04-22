package matt.wltr.labs.tictactoe.series;

import matt.wltr.labs.tictactoe.game.BasePlayer;
import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.game.Player;
import matt.wltr.labs.tictactoe.util.HttpServer;
import matt.wltr.labs.tictactoe.util.Random;

import javax.json.bind.JsonbBuilder;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GameSeries {

    private final LinkedHashSet<Game> games = new LinkedHashSet<>();

    private Class<? extends Player> player1Class;
    private Class<? extends Player> player2Class;

    public void start(Class<? extends BasePlayer> player1Class, Class<? extends BasePlayer> player2Class, int repetitions) {
        games.clear();
        this.player1Class = player1Class;
        this.player2Class = player2Class;
        for (int i = 0; i < repetitions; i++) {
            System.out.println(MessageFormat.format("\nGame {0}", i + 1));
            Game game = new Game();
            game.setPlayers(BasePlayer.instance(player1Class, game), BasePlayer.instance(player2Class, game));
            game.play();
            games.add(game);
        }
    }

    public void printResults() {
        SeriesStatistic seriesStatistic = SeriesStatistic.ofSeries(this);
        URL evolutionChartUrl = serveEvolutionChart(seriesStatistic);
        DecimalFormat decimalFormat = new DecimalFormat("##0.0");
        System.out.print("\n");
        System.out.println("Player                        | P1 Win | P2 Win |   Draw | Evolution");
        System.out.println("=========================================================================================");
        System.out.println(String.format("%-30s| %5s%% | %5s%% | %5s%% | %s",
                player1Class.getSimpleName().concat(" - ").concat(player2Class.getSimpleName()),
                decimalFormat.format(seriesStatistic.getGameStatistics().get(seriesStatistic.getGameStatistics().size() - 1).getPercentagePlayer1Wins()),
                decimalFormat.format(seriesStatistic.getGameStatistics().get(seriesStatistic.getGameStatistics().size() - 1).getPercentagePlayer2Wins()),
                decimalFormat.format(seriesStatistic.getGameStatistics().get(seriesStatistic.getGameStatistics().size() - 1).getPercentageDraws()),
                evolutionChartUrl.toString()));
    }

    private URL serveEvolutionChart(@NotNull SeriesStatistic seriesStatistic) {
        String contextPath = "/" + new Random().nextString();
        HttpServer.createContext(contextPath, httpExchange -> {
            InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream("chart.html");
            assert templateInputStream != null;
            String chartTemplate = new BufferedReader(new InputStreamReader(templateInputStream)).lines().collect(Collectors.joining("\n"));
            String responseMarkup = chartTemplate
                    .replace("$seriesStatistic", JsonbBuilder.create().toJson(seriesStatistic))
                    .replace("$player1", player1Class.getSimpleName())
                    .replace("$player2", player2Class.getSimpleName());
            httpExchange.sendResponseHeaders(200, responseMarkup.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(responseMarkup.getBytes());
            outputStream.flush();
            outputStream.close();
        });
        try {
            return new URL(HttpServer.getOrigin() + contextPath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Game> getGames() {
        return Collections.unmodifiableSet(games);
    }
}
