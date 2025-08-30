package org.nikita.jobs;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.nikita.core.service.TravelExportAgreementToXmlCoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@Slf4j
@RequiredArgsConstructor
class AgreementXmlExporterJob {
    // setters are only for testing purposes
    @Value("${AgreementXmlExporterJob.enabled}")
    @Setter
    private boolean isEnabled;

    @Value("${AgreementXmlExporterJob.threadPoolSize}")
    @Setter
    private int threadPoolSize;

    private final ExportTasks tasks;
    private final TravelExportAgreementToXmlCoreService exportService;

    @Scheduled(fixedRate = 60000)
    public void execute() {
        if (!isEnabled) {
            log.info("AgreementXmlExporterJob is disabled. Skipping execution.");
            return;
        }

        log.info("Starting AgreementXmlExporterJob with thread pool size {}", threadPoolSize);

        // Получаем только UUID, которые ещё не экспортированы
        List<UUID> uuidsToExport = exportService.getNotExportedAgreementUuids();
        if (uuidsToExport.isEmpty()) {
            log.info("No agreements found to export.");
            return;
        }

        try (AutoCloseableExecutorService executor =
                     new AutoCloseableExecutorService(Executors.newFixedThreadPool(threadPoolSize))) {

            List<Future<?>> futures = tasks.submitExportTasks(executor.executorService(), uuidsToExport);

            for (Future<?> future : futures) {
                try {
                    future.get(30, SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Thread was interrupted", e);
                    future.cancel(true);
                } catch (ExecutionException e) {
                    log.error("Execution exception in task: {}", e.getMessage(), e);
                } catch (TimeoutException e) {
                    log.warn("Timeout while waiting for export task to complete");
                    future.cancel(true);
                }
            }
        }

        log.info("AgreementXmlExporterJob finished");
    }
}
