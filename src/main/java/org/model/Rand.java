package org.model;

import org.view.level.Grass;

import java.util.Random;
import java.util.random.RandomGenerator;

public class Rand {
    public static int generatePseudoRandom(int x, int y, int z) {
        // 使用XORShift算法生成伪随机数
        x ^= x << 6;
        y ^= y << 5;
        z ^= z << 4;
        x ^= x >> 7;
        y ^= y >> 3;
        z ^= z >> 1;
        return (x ^ y ^ z);
    }
    public static int myRand(int a, int b, int c, int l, int r) {
        int randomNumber = generatePseudoRandom(a, b, c);
//        System.out.println(a + " " + b + " " + c + " " + randomNumber);
        return (randomNumber % (r - l + 1)) + l;
    }
    public static int counter = 0;
    public static int seed = 0;

    public static void setSeed(int seed) {
        Rand.seed = seed;
    }
    public static void reset_counter() {
        counter = 0;
    }

    public static double next_double(){
        return (double) myRand(counter++, seed, seed ^ counter, 0, 1000) / 1000;
    }

}
