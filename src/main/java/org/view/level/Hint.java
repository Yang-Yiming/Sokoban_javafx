package org.view.level;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.model.config;
import org.view.game.player;

import java.sql.Time;

public class Hint {
    public Level level;
    String moves;

    public Hint(Level level) {
        this.level = level;
        moves = "";
    }

    public void autoMove(){
        moves = level.solve_moves(level.map);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            if(moves.isEmpty()) {
                return;
            }
            if(!player.is_moving){
                char move = Character.toLowerCase(moves.charAt(0));
                Move(move);
                moves = moves.substring(1);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public char autoMoveOnce() {
        char move = Character.toLowerCase(level.solve_next_move());
        Move(move);
        return move;
    }

    public void Move(char move) {
        KeyCode code;
        if (move == 'w') code = KeyCode.W;
        else if (move == 'a') code = KeyCode.A;
        else if (move == 's') code = KeyCode.S;
        else if (move == 'd') code = KeyCode.D;
        else return;

        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false);
        level.getRoot().fireEvent(keyEvent);
    }

}
