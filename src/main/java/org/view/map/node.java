package org.view.map;

import java.util.ArrayList;

public class node {

    public static ArrayList<ArrayList<node>> All_Nodes = new ArrayList<>();

    public static void connect(node node1, node node2) {
        if(node2.layer <= node1.layer || node2.layer - node1.layer > 1) {
            return;
        }
        node1.NextLayerConnectedNodes.add(node2);
        node2.LastLayerConnectedNodes.add(node1);
        node1.is_connected = true; node2.is_connected = true;
    }

    private final int layer;
    private int index;
    private int type;
    private boolean is_connected;

    private ArrayList<node> NextLayerConnectedNodes = new ArrayList<node>();
    private ArrayList<node> LastLayerConnectedNodes = new ArrayList<node>();


    public node(int layer, int index, int type) {
        this.layer = layer;
        this.index = index;
        this.type = type;
        this.is_connected = false;

        if(layer > All_Nodes.size()){
            All_Nodes.add(new ArrayList<>());
        }
        All_Nodes.get(layer).add(this);
    }

    //getter setter
    public int getLayer() {
        return layer;
    }

    public int getIndex() {
        return index;
    }

    public int getType() {
        return type;
    }

    public boolean is_connected() {return is_connected;}

    public ArrayList<node> getNextLayerConnectedNodes() {
        return NextLayerConnectedNodes;
    }

    public ArrayList<node> getLastLayerConnectedNodes() {
        return LastLayerConnectedNodes;
    }

}