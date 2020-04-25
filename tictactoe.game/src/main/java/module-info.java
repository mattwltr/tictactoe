module matt.wltr.labs.tictactoe.game {

    requires java.json.bind;
    requires java.validation;
    requires jdk.httpserver;

    requires mapdb;

    exports matt.wltr.labs.tictactoe.game;
    exports matt.wltr.labs.tictactoe.game.human;
    exports matt.wltr.labs.tictactoe.game.minimax;
    exports matt.wltr.labs.tictactoe.game.random;
    exports matt.wltr.labs.tictactoe.series;
}