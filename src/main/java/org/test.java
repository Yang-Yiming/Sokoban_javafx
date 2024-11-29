package org;

import org.data.mapdata;
import org.model.Coordinate;
import org.model.MapMatrix;

import java.util.Map;

public class test {
    public static void main(String[] args) {
        MapMatrix map = new MapMatrix(mapdata.maps[0]);

        System.out.println(map.get(1,1));
        for(int i = 0; i < map.getWidth(); i++){
            for(int j = 0; j<map.getHeight(); j++){
                System.out.print(map.get(i,j));
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
