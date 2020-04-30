//package matt.wltr.labs.tictactoe.player.tabularquality;
//
//import matt.wltr.labs.tictactoe.player.Player;
//import matt.wltr.labs.tictactoe.game.Game;
//
//import javax.validation.constraints.NotNull;
//
//public class TabularQualityPlayer extends Player {
//
//    public TabularQualityPlayer(@NotNull Game game) {
//        super(game);
//    }
//
//    @Override
//    public void move() {
//
//    }
//
//    @Override
//    public String getEvolutionKey() {
//        return null;
//    }
//}

//
///**
// * @return evolution key. Depending on its implementation, the player might get better over time the more games are played. Previous "knowledge" can
// * be stored in a cache or long term storage (e.g. filesystem, database). The evolution key is used to store/access that "knowledge".
// */
//    String getEvolutionKey();