package org.nikita.loadtesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadTestingStatistic {
    private final List<Long> executionTimes = Collections.synchronizedList(new ArrayList<>());

    public void addExecutionTime(long time) {
        executionTimes.add(time);
    }

    public double getAverageExecutionTime() {
        synchronized (executionTimes) {
            if (executionTimes.isEmpty()) return 0;
            long sum = 0;
            for (long time : executionTimes) {
                sum += time;
            }
            return (double) sum / executionTimes.size();
        }
    }

    public long getMaxExecutionTime() {
        synchronized (executionTimes) {
            if (executionTimes.isEmpty()) return 0;
            long max = executionTimes.get(0);
            for (long time : executionTimes) {
                if (time > max) max = time;
            }
            return max;
        }
    }

    public long getMinExecutionTime() {
        synchronized (executionTimes) {
            if (executionTimes.isEmpty()) return 0;
            long min = executionTimes.get(0);
            for (long time : executionTimes) {
                if (time < min) min = time;
            }
            return min;
        }
    }
}
