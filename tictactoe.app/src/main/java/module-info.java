module matt.wltr.labs.tictactoe.app {

    requires java.logging;
    requires java.validation;

    requires matt.wltr.labs.tictactoe.game.series;
    requires matt.wltr.labs.tictactoe.player.random;
    requires matt.wltr.labs.tictactoe.player.minimax;

    exports matt.wltr.labs.tictactoe.logging to java.logging;
}