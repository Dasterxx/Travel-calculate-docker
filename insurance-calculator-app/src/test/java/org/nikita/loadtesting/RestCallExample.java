package org.nikita.loadtesting;

import java.util.ArrayList;
import java.util.List;

public class RestCallExample {

    public static void main(String[] args) {
        LoadTestingStatistic v1Statistic = new LoadTestingStatistic();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            threads.add(new Thread(new V1Call(v1Statistic)));
        }

        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("=== /v1 Statistics ===");
        System.out.println("Average: " + v1Statistic.getAverageExecutionTime() + " ms");
        System.out.println("Min: " + v1Statistic.getMinExecutionTime() + " ms");
        System.out.println("Max: " + v1Statistic.getMaxExecutionTime() + " ms");

    }
}
