package org.model.Solve;

import org.model.Coordinate;
import org.model.GameMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SimulatedAnnealing {
    private GameMap map;
    private Coordinate beginPlayer;
    private HashSet<Coordinate> Walls, Goals, beginBoxes;
    private Random random;

    // 模拟退火参数
    private double initialTemperature = 1000.0;
    private double coolingRate = 0.995;
    private double minTemperature = 0.1;
    private int maxIterations = 10000;
    private int maxMoves = 200; // 单个解的最大移动步数

    public SimulatedAnnealing(GameMap map) {
        this.map = map;
        this.random = new Random();
        this.Walls = new HashSet<>();
        this.Goals = new HashSet<>();
        this.beginBoxes = new HashSet<>();

        for(int i = 0; i < map.getHeight(); i++) {
            for(int j = 0; j < map.getWidth(); j++) {
                if(map.hasBox(j, i)) {
                    beginBoxes.add(new Coordinate(j, i));
                }
                if(map.hasPlayer(j, i)) {
                    beginPlayer = new Coordinate(j, i);
                }
                if(map.hasWall(j, i)) {
                    Walls.add(new Coordinate(j, i));
                }
                if(map.hasGoal(j, i)) {
                    Goals.add(new Coordinate(j, i));
                }
            }
        }
    }

    private static class Solution {
        String moves;
        double fitness;

        Solution(String moves, double fitness) {
            this.moves = moves;
            this.fitness = fitness;
        }

        Solution copy() {
            return new Solution(moves, fitness);
        }
    }

    public String solve() {
        Solution currentSolution = generateRandomSolution();
        Solution bestSolution = currentSolution.copy();

        double temperature = initialTemperature;
        int iterations = 0;

        while (temperature > minTemperature && iterations < maxIterations) {
            Solution newSolution = generateNeighbor(currentSolution);

            if (newSolution == null) {
                iterations++;
                temperature *= coolingRate;
                continue;
            }

            // 如果找到完整解
            if (newSolution.fitness == Double.MAX_VALUE) {
                return newSolution.moves;
            }

            double deltaE = newSolution.fitness - currentSolution.fitness;

            // 接受更好的解，或以概率接受较差的解
            if (deltaE > 0 || Math.exp(deltaE / temperature) > random.nextDouble()) {
                currentSolution = newSolution;

                if (currentSolution.fitness > bestSolution.fitness) {
                    bestSolution = currentSolution.copy();
                }
            }

            temperature *= coolingRate;
            iterations++;
        }

        return bestSolution.moves;
    }

    private Solution generateRandomSolution() {
        StringBuilder moves = new StringBuilder();
        char[] actions = {'w', 'a', 's', 'd', 'W', 'A', 'S', 'D'};

        // 生成随机移动序列
        int moveCount = random.nextInt(maxMoves / 2) + 20;
        for (int i = 0; i < moveCount; i++) {
            moves.append(actions[random.nextInt(actions.length)]);
        }

        double fitness = evaluateSolution(moves.toString());
        return new Solution(moves.toString(), fitness);
    }

    private Solution generateNeighbor(Solution solution) {
        String moves = solution.moves;
        StringBuilder newMoves = new StringBuilder(moves);

        // 随机选择一种变异操作
        int operation = random.nextInt(4);

        try {
            switch (operation) {
                case 0: // 添加动作
                    if (newMoves.length() < maxMoves) {
                        char[] actions = {'w', 'a', 's', 'd', 'W', 'A', 'S', 'D'};
                        int pos = random.nextInt(newMoves.length() + 1);
                        newMoves.insert(pos, actions[random.nextInt(actions.length)]);
                    }
                    break;
                case 1: // 删除动作
                    if (newMoves.length() > 1) {
                        int pos = random.nextInt(newMoves.length());
                        newMoves.deleteCharAt(pos);
                    }
                    break;
                case 2: // 修改动作
                    if (newMoves.length() > 0) {
                        char[] actions = {'w', 'a', 's', 'd', 'W', 'A', 'S', 'D'};
                        int pos = random.nextInt(newMoves.length());
                        newMoves.setCharAt(pos, actions[random.nextInt(actions.length)]);
                    }
                    break;
                case 3: // 交换两个动作
                    if (newMoves.length() > 1) {
                        int pos1 = random.nextInt(newMoves.length());
                        int pos2 = random.nextInt(newMoves.length());
                        char temp = newMoves.charAt(pos1);
                        newMoves.setCharAt(pos1, newMoves.charAt(pos2));
                        newMoves.setCharAt(pos2, temp);
                    }
                    break;
            }
        } catch (Exception e) {
            return null;
        }

        double fitness = evaluateSolution(newMoves.toString());
        return new Solution(newMoves.toString(), fitness);
    }

    private double evaluateSolution(String moves) {
        try {
            Coordinate player = new Coordinate(beginPlayer.x, beginPlayer.y);
            HashSet<Coordinate> boxes = new HashSet<>(beginBoxes);

            for (char action : moves.toCharArray()) {
                if (!isLegalAction(action, player, boxes)) {
                    break; // 遇到非法动作就停止
                }

                var result = updateState(player, boxes, action);
                boxes = result.getFirst();
                player = result.getSecond();

                // 检查是否完成
                if (isEndState(boxes)) {
                    return Double.MAX_VALUE; // 找到解
                }

                // 检查是否失败
                if (isFailed(boxes)) {
                    break;
                }
            }

            // 计算适应度：完成的目标数量 - 未完成箱子到目标的距离
            int completedGoals = setIntersect(boxes, Goals).size();
            double totalDistance = calculateTotalDistance(boxes);

            return completedGoals * 100 - totalDistance;

        } catch (Exception e) {
            return -1000; // 错误状态
        }
    }

    private double calculateTotalDistance(HashSet<Coordinate> boxes) {
        HashSet<Coordinate> completes = setIntersect(boxes, Goals);
        Object[] leftBoxes = setSub(boxes, completes).toArray();
        Object[] leftGoals = setSub(Goals, completes).toArray();

        double totalDistance = 0;
        for (Object box : leftBoxes) {
            double minDistance = Double.MAX_VALUE;
            for (Object goal : leftGoals) {
                double distance = manhattan((Coordinate) box, (Coordinate) goal);
                minDistance = Math.min(minDistance, distance);
            }
            if (minDistance != Double.MAX_VALUE) {
                totalDistance += minDistance;
            }
        }
        return totalDistance;
    }

    // 复用Solve类中的辅助方法
    private static Coordinate move(char action) {
        if(action == 'W' || action == 'w') {
            return new Coordinate(0, -1);
        } else if(action == 'A' || action == 'a'){
            return new Coordinate(-1, 0);
        } else if(action == 'S' || action == 's'){
            return new Coordinate(0, 1);
        } else if(action == 'D' || action == 'd'){
            return new Coordinate(1, 0);
        } else {
            return new Coordinate(0, 0);
        }
    }

    private boolean isLegalAction(char action, Coordinate player, HashSet<Coordinate> Boxes) {
        Coordinate next;
        if(Character.isUpperCase(action)) {
            next = new Coordinate(player.x + 2 * move(action).x, player.y + 2 * move(action).y);
        } else {
            next = new Coordinate(player.x + move(action).x, player.y + move(action).y);
        }
        return !setAdd(Walls, Boxes).contains(next);
    }

    private Solve.Pair<HashSet<Coordinate>, Coordinate> updateState(Coordinate player, HashSet<Coordinate> Boxes, char action) {
        Coordinate nextPlayer = new Coordinate(player.x + move(action).x, player.y + move(action).y);
        HashSet<Coordinate> nextBoxes = new HashSet<>(Boxes);
        if(Character.isUpperCase(action)) {
            nextBoxes.remove(new Coordinate(player.x + move(action).x, player.y + move(action).y));
            nextBoxes.add(new Coordinate(player.x + 2 * move(action).x, player.y + 2 * move(action).y));
        }
        return new Solve.Pair<>(nextBoxes, nextPlayer);
    }

    private boolean isEndState(HashSet<Coordinate> Boxes) {
        return setIntersect(Boxes, Goals).size() == Goals.size();
    }

    private boolean isFailed(HashSet<Coordinate> Boxes) {
        // 简化的失败检测，可以复用Solve类中的完整实现
        return false; // 暂时简化
    }

    private int manhattan(Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    // 集合操作方法
    private <T> HashSet<T> setAdd(HashSet<T> a, HashSet<T> b) {
        HashSet<T> res = new HashSet<>();
        res.addAll(a);
        res.addAll(b);
        return res;
    }

    private <T> HashSet<T> setSub(HashSet<T> a, HashSet<T> b) {
        HashSet<T> res = new HashSet<>();
        res.addAll(a);
        res.removeAll(b);
        return res;
    }

    private <T> HashSet<T> setIntersect(HashSet<T> a, HashSet<T> b) {
        HashSet<T> res = new HashSet<>();
        res.addAll(a);
        res.retainAll(b);
        return res;
    }
}