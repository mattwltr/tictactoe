module matt.wltr.labs.tictactoe.game.series {

    requires java.validation;
    requires java.json.bind;
    requires java.logging;
    requires transitive jdk.httpserver;

    requires matt.wltr.labs.tictactoe.game;
    requires matt.wltr.labs.tictactoe.util;

    exports matt.wltr.labs.tictactoe.game.series;
}