import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class Game{
    private int[][] map = new int[11][11];
    private int lenx, leny;
    private int playerX, playerY;
    private Pane root;
    private int id = 0;
    private int step = 0;
    private int tryNumber = 0;

    public Game(Pane root, int id){
        this.root = root;
        this.id = id;
        this.tryNumber = 0;
        this.lenx = MapData.maps[id].length;
        this.leny = MapData.maps[id][0].length;
    }

    public void init(){ // 0:空地 1:墙 2:箱子 3:目标 4:人 23:箱子+目标 34:目标+人
        this.step = 0;
        ++tryNumber;
        for(int x = 0; x < lenx; ++x)
            for(int y = 0; y < leny; ++y){
                map[x][y] = MapData.maps[id][x][y];
                if(map[x][y] == 4 || map[x][y] == 34){
                    playerX = x;
                    playerY = y;
                }
            }
        drawMap();
    }

    public void drawMap(){
        int tileSize = 50;
        for (int x = 0; x < lenx; ++x){
            for (int y = 0; y < leny; ++y) {
                Rectangle tile = new Rectangle(y * tileSize, x * tileSize, tileSize, tileSize);
                int b = map[x][y];
                if(b == 0) tile.setFill(Color.WHITE); // 空地
                if(b == 1) tile.setFill(CoRectanglelor.GREY); // 墙
                if(b == 2) tile.setFill(Color.YELLOW); // 箱子
                if(b == 3) tile.setFill(Color.RED); // 目标
                if(b == 4) tile.setFill(Color.BLUE); // 人
                if(b == 23) tile.setFill(Color.ORANGE); // 箱子+目标
                if(b == 34) tile.setFill(Color.PURPLE); // 人+目标
                root.getChildren().add(tile);
            }
        }
        Text text = new Text("step: " + step + "        try: " + tryNumber);
        text.setX(5); // 设置文本的x坐标
        text.setY(15); // 设置文本的y坐标
        text.setFill(Color.BLACK); // 设置文本颜色
        root.getChildren().add(text);
    }
    public boolean isEmpty(int x, int y){
        if(map[x][y] == 0 || map[x][y] == 3) return true;
        return false;
    }
    public boolean isBox(int x, int y){
        if(map[x][y] == 2 || map[x][y] == 23) return true;
        return false;
    }
    public void addPlayer(int x, int y){
        if(map[x][y] == 0) map[x][y] = 4;
        if(map[x][y] == 3) map[x][y] = 34;
    }
    public void removePlayer(int x, int y){
        if(map[x][y] == 4) map[x][y] = 0;
        if(map[x][y] == 34) map[x][y] = 3;
    }
    public void addBox(int x, int y){
        if(map[x][y] == 0) map[x][y] = 2;
        if(map[x][y] == 3) map[x][y] = 23;
    }
    public void removeBox(int x, int y){
        if(map[x][y] == 2) map[x][y] = 0;
        if(map[x][y] == 23) map[x][y] = 3;
    }
    
    public boolean isWin(){
        for(int x = 0; x < lenx; ++x)
            for(int y = 0; y < leny; ++y)
                if(map[x][y] == 2) return false;
        return true;
    }

    public void movePlayer(int dx, int dy) {
        int newx = playerX + dx;
        int newy = playerY + dy;
        if(newx < 0 || newx >= lenx || newy < 0 || newy >= leny) return;
        if(isEmpty(newx, newy)){
            addPlayer(newx, newy);
            removePlayer(playerX, playerY);
            playerX = newx;
            playerY = newy;
            ++step;
        }else if(isBox(newx, newy)){
            int newbx = newx + dx;
            int newby = newy + dy;
            if(newbx < 0 || newbx >= lenx || newby < 0 || newby >= leny) return;
            if(isEmpty(newbx, newby)){
                addBox(newbx, newby);   
                removeBox(newx, newy);
                addPlayer(newx, newy);
                removePlayer(playerX, playerY);
                playerX = newx;
                playerY = newy;
                ++step;
            }
        }
        drawMap(); // 重新绘制地图
    }
}