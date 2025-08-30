package org.nikita.jobs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public record AutoCloseableExecutorService(ExecutorService executorService) implements AutoCloseable {

    @Override
    public void close() {
        log.info("Shutting down ExecutorService...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.warn("ExecutorService did not terminate in time, forcing shutdown...");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.warn("Interrupted while waiting for ExecutorService termination, forcing shutdown...");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("ExecutorService shutdown complete.");
    }
}
