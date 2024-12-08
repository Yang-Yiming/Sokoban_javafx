package org.view.level;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.model.config;

public class Hint {
    public Level level;

    public Hint(Level level) {
        this.level = level;
    }

    public void autoMove(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(config.move_anim_duration), event -> {
            char move = Character.toLowerCase(level.solve_next_move());
            KeyCode code;
            if (move == 'w') code = KeyCode.W;
            else if (move == 'a') code = KeyCode.A;
            else if (move == 's') code = KeyCode.S;
            else if (move == 'd') code = KeyCode.D;
            else return;

            KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false);
            level.getRoot().fireEvent(keyEvent);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}
