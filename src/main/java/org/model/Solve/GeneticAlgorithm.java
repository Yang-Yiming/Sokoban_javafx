package org.model.Solve;

import org.model.Coordinate;
import org.model.GameMap;

import java.util.*;

public class GeneticAlgorithm {
    private GameMap map;
    private Coordinate beginPlayer;
    private HashSet<Coordinate> Walls, Goals, beginBoxes;
    private Random random;

    // 遗传算法参数
    private int populationSize = 100;
    private int maxGenerations = 500;
    private double mutationRate = 0.1;
    private double crossoverRate = 0.8;
    private int maxGeneLength = 150;
    private int eliteSize = 10; // 精英个体数量

    public GeneticAlgorithm(GameMap map) {
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

    private static class Chromosome implements Comparable<Chromosome> {
        String genes; // 移动序列
        double fitness;
        boolean isComplete; // 是否找到完整解

        Chromosome(String genes) {
            this.genes = genes;
            this.fitness = 0;
            this.isComplete = false;
        }

        Chromosome copy() {
            Chromosome copy = new Chromosome(genes);
            copy.fitness = fitness;
            copy.isComplete = isComplete;
            return copy;
        }

        @Override
        public int compareTo(Chromosome other) {
            if (this.isComplete && !other.isComplete) return -1;
            if (!this.isComplete && other.isComplete) return 1;
            return Double.compare(other.fitness, this.fitness); // 降序
        }
    }

    public String solve() {
        List<Chromosome> population = initializePopulation();

        for (int generation = 0; generation < maxGenerations; generation++) {
            // 评估适应度
            evaluatePopulation(population);

            // 排序
            Collections.sort(population);

            // 检查是否找到解
            if (population.get(0).isComplete) {
                return population.get(0).genes;
            }

            // 生成新一代
            population = evolvePopulation(population);

            // 每50代输出一次最佳适应度
            if (generation % 50 == 0) {
                System.out.println("Generation " + generation + ", Best fitness: " + population.get(0).fitness);
            }
        }

        // 返回最佳解
        Collections.sort(population);
        return population.get(0).genes;
    }

    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        char[] actions = {'w', 'a', 's', 'd', 'W', 'A', 'S', 'D'};

        for (int i = 0; i < populationSize; i++) {
            StringBuilder genes = new StringBuilder();
            int length = random.nextInt(maxGeneLength / 2) + 20;

            for (int j = 0; j < length; j++) {
                genes.append(actions[random.nextInt(actions.length)]);
            }

            population.add(new Chromosome(genes.toString()));
        }

        return population;
    }

    private void evaluatePopulation(List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            evaluateChromosome(chromosome);
        }
    }

    private void evaluateChromosome(Chromosome chromosome) {
        try {
            Coordinate player = new Coordinate(beginPlayer.x, beginPlayer.y);
            HashSet<Coordinate> boxes = new HashSet<>(beginBoxes);
            int validMoves = 0;

            for (char action : chromosome.genes.toCharArray()) {
                if (!isLegalAction(action, player, boxes)) {
                    break; // 遇到非法动作就停止
                }

                var result = updateState(player, boxes, action);
                boxes = result.getFirst();
                player = result.getSecond();
                validMoves++;

                // 检查是否完成
                if (isEndState(boxes)) {
                    chromosome.fitness = 10000 + (maxGeneLength - validMoves); // 奖励更短的解
                    chromosome.isComplete = true;
                    return;
                }

                // 检查是否失败
                if (isFailed(boxes)) {
                    break;
                }
            }

            // 计算适应度
            int completedGoals = setIntersect(boxes, Goals).size();
            double totalDistance = calculateTotalDistance(boxes);
            double moveEfficiency = (double) validMoves / chromosome.genes.length();

            chromosome.fitness = completedGoals * 200 - totalDistance + moveEfficiency * 50;
            chromosome.isComplete = false;

        } catch (Exception e) {
            chromosome.fitness = -1000;
            chromosome.isComplete = false;
        }
    }

    private List<Chromosome> evolvePopulation(List<Chromosome> population) {
        List<Chromosome> newPopulation = new ArrayList<>();

        // 保留精英
        for (int i = 0; i < eliteSize && i < population.size(); i++) {
            newPopulation.add(population.get(i).copy());
        }

        // 生成新个体
        while (newPopulation.size() < populationSize) {
            Chromosome parent1 = tournamentSelection(population);
            Chromosome parent2 = tournamentSelection(population);

            Chromosome offspring;
            if (random.nextDouble() < crossoverRate) {
                offspring = crossover(parent1, parent2);
            } else {
                offspring = parent1.copy();
            }

            if (random.nextDouble() < mutationRate) {
                mutate(offspring);
            }

            newPopulation.add(offspring);
        }

        return newPopulation;
    }

    private Chromosome tournamentSelection(List<Chromosome> population) {
        int tournamentSize = 5;
        Chromosome best = population.get(random.nextInt(population.size()));

        for (int i = 1; i < tournamentSize; i++) {
            Chromosome candidate = population.get(random.nextInt(population.size()));
            if (candidate.compareTo(best) < 0) {
                best = candidate;
            }
        }

        return best;
    }

    private Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        String genes1 = parent1.genes;
        String genes2 = parent2.genes;

        if (genes1.length() == 0 || genes2.length() == 0) {
            return genes1.length() > genes2.length() ? parent1.copy() : parent2.copy();
        }

        // 单点交叉
        int crossoverPoint1 = random.nextInt(genes1.length());
        int crossoverPoint2 = random.nextInt(genes2.length());

        String offspring = genes1.substring(0, crossoverPoint1) + genes2.substring(crossoverPoint2);

        // 限制长度
        if (offspring.length() > maxGeneLength) {
            offspring = offspring.substring(0, maxGeneLength);
        }

        return new Chromosome(offspring);
    }

    private void mutate(Chromosome chromosome) {
        if (chromosome.genes.length() == 0) return;

        StringBuilder genes = new StringBuilder(chromosome.genes);
        char[] actions = {'w', 'a', 's', 'd', 'W', 'A', 'S', 'D'};

        // 随机选择变异类型
        int mutationType = random.nextInt(4);

        try {
            switch (mutationType) {
                case 0: // 点突变
                    if (genes.length() > 0) {
                        int pos = random.nextInt(genes.length());
                        genes.setCharAt(pos, actions[random.nextInt(actions.length)]);
                    }
                    break;
                case 1: // 插入
                    if (genes.length() < maxGeneLength) {
                        int pos = random.nextInt(genes.length() + 1);
                        genes.insert(pos, actions[random.nextInt(actions.length)]);
                    }
                    break;
                case 2: // 删除
                    if (genes.length() > 1) {
                        int pos = random.nextInt(genes.length());
                        genes.deleteCharAt(pos);
                    }
                    break;
                case 3: // 交换
                    if (genes.length() > 1) {
                        int pos1 = random.nextInt(genes.length());
                        int pos2 = random.nextInt(genes.length());
                        char temp = genes.charAt(pos1);
                        genes.setCharAt(pos1, genes.charAt(pos2));
                        genes.setCharAt(pos2, temp);
                    }
                    break;
            }
        } catch (Exception e) {
            // 忽略变异错误
        }

        chromosome.genes = genes.toString();
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

    // 复用的辅助方法（与SimulatedAnnealing中相同）
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
        return false; // 简化版本，可以复用Solve类中的完整实现
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