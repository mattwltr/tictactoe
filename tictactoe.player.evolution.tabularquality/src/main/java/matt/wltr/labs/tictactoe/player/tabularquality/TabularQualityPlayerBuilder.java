package matt.wltr.labs.tictactoe.player.tabularquality;

import matt.wltr.labs.tictactoe.game.Game;
import matt.wltr.labs.tictactoe.player.PlayerBuilder;
import matt.wltr.labs.tictactoe.player.evolution.Evolution;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TabularQualityPlayerBuilder implements PlayerBuilder<TabularQualityPlayer> {

    private final String evolutionKey;

    private Evolution evolution;

    public TabularQualityPlayerBuilder(@NotBlank String evolutionKey) {
        this(evolutionKey, Evolution.IN_PROGRESS);
    }

    public TabularQualityPlayerBuilder(@NotBlank String evolutionKey, @NotNull Evolution evolution) {
        this.evolutionKey = evolutionKey;
        this.evolution = evolution;
    }

    @Override
    public Class<TabularQualityPlayer> getType() {
        return TabularQualityPlayer.class;
    }

    @Override
    public TabularQualityPlayer build(Game game) {
        return new TabularQualityPlayer(game, evolutionKey, evolution);
    }

    public String getEvolutionKey() {
        return evolutionKey;
    }

    public Evolution getEvolution() {
        return evolution;
    }

    public void setEvolution(Evolution evolution) {
        this.evolution = evolution;
    }
}
