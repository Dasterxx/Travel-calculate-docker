package org.nikita.jobs;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.core.service.TravelExportAgreementToXmlCoreService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportTasksTest {

    private
    TravelExportAgreementToXmlCoreService exportService;
    private ExportTasks exportTasks;
    private ExecutorService executor;

    @BeforeEach
    void setUp() {
        exportService = mock(
                TravelExportAgreementToXmlCoreService.class);
        exportTasks = new ExportTasks(exportService);
        executor = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @Test
    void submitExportTasks_shouldSubmitAllTasks() throws Exception {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        List<UUID> uuids = List.of(uuid1, uuid2);

        List<Future<?>> futures = exportTasks.submitExportTasks(executor, uuids);

        assertEquals(2, futures.size());

        for (Future<?> future : futures) {
            future.get(); // ждем завершения
        }

        verify(exportService).export(uuid1);
        verify(exportService).export(uuid2);
    }

    @Test
    void submitExportTasks_shouldHandleExceptionInTask() throws Exception {
        UUID uuid = UUID.randomUUID();

        doThrow(new RuntimeException("Test exception"))
                .when(exportService).export(uuid);

        List<Future<?>> futures = exportTasks.submitExportTasks(executor, List.of(uuid));

        for (Future<?> future : futures) {
            // Исключение внутри задачи не пробрасывается наружу, get() не должен выбрасывать
            future.get();
        }

        verify(exportService).export(uuid);
    }
}
