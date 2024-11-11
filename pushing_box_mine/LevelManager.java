import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LevelManager{
    private int currentLevel;
    private int[][] map;
    private Pane root;
    private Scene scene;

    public LevelManager(Pane root){
        this.root = root;
        this.currentLevel = 0;
        this.map = MapData.maps[currentLevel];
    }

    public void start(Stage primaryStage){
        this.root = new Pane();
        scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        loadLevel();
    }

    private void nextLevel(){
        if(currentLevel < MapData.maps.length){
            showCongratulation();
            ++currentLevel;
            loadLevel();
        }else{
            showFinish();
            System.exit(0);
        }
    }

    private void loadLevel(){
        // 初始化游戏地图和逻辑
        Game game = new Game(root, currentLevel);
        game.init();
        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if(code == KeyCode.UP) game.movePlayer(-1, 0);
            if(code == KeyCode.DOWN) game.movePlayer(1, 0);
            if(code == KeyCode.LEFT) game.movePlayer(0, -1);
            if(code == KeyCode.RIGHT) game.movePlayer(0, 1);
            if(code == KeyCode.R){
                game.init();
                // System.out.println("reset"); 
            }
            if(game.isWin()) nextLevel();
        });
    }

    public void showCongratulation(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText(null);
        alert.setContentText("You have completed level " + (currentLevel + 1) + "!");
        alert.showAndWait();
    }

    public void showFinish(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText(null);
        alert.setContentText("You have completed all levels!");
        alert.showAndWait();
        System.exit(0);
    }
}