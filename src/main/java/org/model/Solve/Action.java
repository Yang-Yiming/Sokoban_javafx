package org.model.Solve;

public class Action {
    public int move_1, move_2;
    public char move; // L, U, R, D // 大写代表推
    private boolean push;

    public Action(int x, int y) {
        this.move_1 = x;
        this.move_2 = y;
        if(x==0 && y==1) move = 'r';
        else if(x==0 && y==-1) move = 'l';
        else if(x==1 && y==0) move = 'd';
        else if(x==-1 && y==0) move = 'u';
    }

    public boolean push(){
        return push;
    }
    public void set_push(boolean push){
        this.push = push;
        if(push){
            this.move = Character.toUpperCase(this.move);
        } else {
            this.move = Character.toLowerCase(this.move);
        }
    }
}
