package org.model.solve;

import org.model.MapMatrix;

import java.util.*;

public class AStar {
    private Map<Integer, Map<Integer, Boolean>> visited = new HashMap<>();
    private MapMatrix map;

    // 判断坐标是否无障碍且未被访问过
    private boolean hasNoObstacle(int x, int y, Map<Integer, Map<Integer, Boolean>> visited) {
        Map<Integer, Boolean> row = visited.getOrDefault(x, new HashMap<>());
        return !row.containsKey(y) && map.hasNoObstacle(x, y);
    }

    // 计算一个坐标点到一组目标坐标点中最近目标的曼哈顿距离
    private int manhattanDistanceToNearestGoal(int x, int y, List<Node> goals) {
        int minDistance = Integer.MAX_VALUE;
        for (Node goal : goals) {
            int distance = Math.abs(x - goal.x) + Math.abs(y - goal.y);
            minDistance = Math.min(minDistance, distance);
        }
        return minDistance;
    }

    // 启发式函数，按照每个箱子到离它最近goal的曼哈顿距离 * 10 + 玩家到离他最近箱子的曼哈顿距离 * 3 来估算
    private double heuristic(List<Node> boxes, Node player, List<Node> goals) {
        int boxToGoalDistanceSum = 0;
        for (Node box : boxes) {
            boxToGoalDistanceSum += manhattanDistanceToNearestGoal(box.x, box.y, goals) * 10;
        }
        int playerToBoxDistance = Integer.MAX_VALUE;
        for (Node box : boxes) {
            playerToBoxDistance = Math.min(playerToBoxDistance, Math.abs(player.x - box.x) + Math.abs(player.y - box.y));
        }
        return boxToGoalDistanceSum + playerToBoxDistance * 3;
    }

    public List<Node> findPath(List<Node> boxes, Node player, List<Node> goals) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        openSet.add(player);

        player.gScore = 0.0;
        player.fScore = heuristic(boxes, player, goals);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (boxes.isEmpty()) {
                // 所有箱子都已经在目标点上，构建路径并返回
                List<Node> path = new ArrayList<>();
                Node node = current;
                while (node != null) {
                    path.add(0, node);
                    node = node.parent;
                }
                return path;
            }

            int[] dx = {-1, 0, 1, 0};
            int[] dy = {0, -1, 0, 1};
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];
                if (hasNoObstacle(newX, newY, visited)) {
                    Node neighbor = new Node(newX, newY);
                    double tentativeGScore = current.gScore + 1; // 假设相邻节点移动代价为1
                    if (!openSet.contains(neighbor) || tentativeGScore < neighbor.gScore) {
                        neighbor.parent = current;
                        neighbor.gScore = tentativeGScore;
                        List<Node> newBoxes = new ArrayList<>(boxes);
                        if (map.hasBox(newX, newY)) {
                            // 如果邻居节点是箱子，尝试推动箱子到相邻的可到达位置
                            for (int j = 0; j < 4; j++) {
                                int pushX = newX + dx[j];
                                int pushY = newY + dy[j];
                                if (hasNoObstacle(pushX, pushY, visited) && !map.hasBox(pushX, pushY)) {
                                    Node pushedBox = new Node(pushX, pushY);
                                    newBoxes.remove(neighbor);
                                    newBoxes.add(pushedBox);
                                    break;
                                }
                            }
                        }
                        neighbor.fScore = tentativeGScore + heuristic(newBoxes, neighbor, goals);
                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }
            }
            Map<Integer, Boolean> row = visited.getOrDefault(current.x, new HashMap<>());
            row.put(current.y, true);
            visited.put(current.x, row);
        }

        return null; // 如果没有找到路径，返回null
    }

    public AStar (MapMatrix map) {
        this.map = map;
    }
}
