package matt.wltr.labs.tictactoe.game;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class GameSeries {

    private final LinkedHashSet<Game> games = new LinkedHashSet<>();

    private Class<? extends Player> player1;
    private Class<? extends Player> player2;

    private int player1Wins;
    private int player2Wins;
    private int draws;

    public void start(Class<? extends BasePlayer> player1Class, Class<? extends BasePlayer> player2Class, int repetitions) {
        this.player1 = player1Class;
        this.player2 = player2Class;
        for (int i = 0; i < repetitions; i++) {
            System.out.println(MessageFormat.format("\nGame {0}", i + 1));
            Game game = new Game();
            game.setPlayers(BasePlayer.instance(player1Class, game), BasePlayer.instance(player2Class, game));
            game.play();
            switch (game.getStatus()) {
                case DRAW -> draws += 1;
                case PLAYER_1_WON -> player1Wins += 1;
                case PLAYER_2_WON -> player2Wins += 1;
            }
            games.add(game);
        }
    }

    public void printResults() {
        System.out.print("\n");

        DecimalFormat decimalFormat = new DecimalFormat("##0.0");
        System.out.println("Player                        | P1 Win | P2 Win |   Draw");
        System.out.println("========================================================");
        System.out.println(String.format("%-30s| %5s%% | %5s%% | %5s%%",
                player1.getSimpleName().concat(" - ").concat(player2.getSimpleName()),
                decimalFormat.format((player1Wins * 100D) / games.size()),
                decimalFormat.format((player2Wins * 100D) / games.size()),
                decimalFormat.format((draws * 100D) / games.size())));
    }

    public Set<Game> getGames() {
        return Collections.unmodifiableSet(games);
    }
}
