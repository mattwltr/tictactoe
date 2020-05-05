module matt.wltr.labs.tictactoe.player.tabularquality {

    requires java.logging;
    requires java.validation;
    requires mapdb;

    requires matt.wltr.labs.tictactoe.game;
    requires matt.wltr.labs.tictactoe.util;
    requires transitive matt.wltr.labs.tictactoe.player.evolution;

    exports matt.wltr.labs.tictactoe.player.tabularquality;
}