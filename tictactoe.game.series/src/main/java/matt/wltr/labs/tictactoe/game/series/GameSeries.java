package matt.wltr.labs.tictactoe.game.series;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.game.series.util.HttpServer;
import matt.wltr.labs.tictactoe.player.PlayerBuilder;
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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GameSeries {

    protected final static Logger LOGGER = Logger.getLogger(GameSeries.class.getName());

    private final LinkedHashSet<Game> games = new LinkedHashSet<>();

    private final PlayerBuilder<?> player1Builder;
    private final PlayerBuilder<?> player2Builder;
    private final int repetitions;

    private String description;

    public GameSeries(@NotNull PlayerBuilder<?> player1Builder, @NotNull PlayerBuilder<?> player2Builder, int repetitions) {
        this.player1Builder = player1Builder;
        this.player2Builder = player2Builder;
        this.repetitions = repetitions;
    }

    public void start() {
        games.clear();
        for (int i = 0; i < repetitions; i++) {
            LOGGER.log(Level.INFO, "Game {0}", i + 1);
            Game game = new Game();
            game.setPlayers(player1Builder.build(game), player2Builder.build(game));
            game.play();
            games.add(game);
        }
    }

    public void printResults() {
        SeriesStatistic seriesStatistic = SeriesStatistic.ofSeries(this);
        URL evolutionChartUrl = serveEvolutionChart(seriesStatistic);
        DecimalFormat decimalFormat = new DecimalFormat("##0.0");
        LOGGER.log(Level.INFO, String.format("""
                | P1 Win %25s | P2 Win %25s |   Draw | Evolution                      
                ===============================================================================================================
                | %31s%% | %31s%% | %5s%% | %-30s%s""",
                player1Builder.getType().getSimpleName(),
                player2Builder.getType().getSimpleName(),
                decimalFormat.format(seriesStatistic.getPercentagePlayer1Wins()),
                decimalFormat.format(seriesStatistic.getPercentagePlayer2Wins()),
                decimalFormat.format(seriesStatistic.getPercentageDraws()),
                evolutionChartUrl.toString(),
                getDescription().map(description -> "\n\n" + description).orElse("")));
    }

    private URL serveEvolutionChart(@NotNull SeriesStatistic seriesStatistic) {
        String contextPath = "/" + new Random().nextString();
        HttpServer.createContext(contextPath, httpExchange -> {
            InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream("chart.html");
            assert templateInputStream != null;
            String chartTemplate = new BufferedReader(new InputStreamReader(templateInputStream)).lines().collect(Collectors.joining("\n"));
            String responseMarkup = chartTemplate
                    .replace("$seriesStatistic", JsonbBuilder.create().toJson(seriesStatistic))
                    .replace("$player1", player1Builder.getType().getSimpleName())
                    .replace("$player2", player2Builder.getType().getSimpleName());
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

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
