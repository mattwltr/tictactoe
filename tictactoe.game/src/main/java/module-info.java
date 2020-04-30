module matt.wltr.labs.tictactoe.game {

    requires java.json.bind;
    requires java.validation;
    requires jdk.httpserver;

    requires mapdb;

    exports matt.wltr.labs.tictactoe.game;
    exports matt.wltr.labs.tictactoe.player;
    exports matt.wltr.labs.tictactoe.player.human;
    exports matt.wltr.labs.tictactoe.player.minimax;
    exports matt.wltr.labs.tictactoe.player.random;
    exports matt.wltr.labs.tictactoe.game.series;
}