package org.nikita.jobs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.core.service.TravelExportAgreementToXmlCoreService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgreementXmlExporterJobTest {

    @Mock
    private ExportTasks exportTasks;

    @Mock
    private TravelExportAgreementToXmlCoreService exportService;

    @InjectMocks
    private AgreementXmlExporterJob job;

    @BeforeEach
    void setUp() {
        job.setThreadPoolSize(2);
        job.setEnabled(true);
    }

    @Test
    void execute_shouldSkipWhenDisabled() {
        job.setEnabled(false);
        job.execute();

        verifyNoInteractions(exportService);
        verifyNoInteractions(exportTasks);
    }

    @Test
    void execute_shouldFinishWithoutTasksIfNoUuids() {
        when(exportService.getNotExportedAgreementUuids()).thenReturn(List.of());

        job.execute();

        verify(exportService).getNotExportedAgreementUuids();
        verifyNoInteractions(exportTasks);
    }

    @Test
    void execute_shouldSubmitTasksAndWait() throws Exception {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        List<UUID> uuids = List.of(uuid1, uuid2);

        when(exportService.getNotExportedAgreementUuids()).thenReturn(uuids);

        Future<?> future1 = mock(Future.class);
        Future<?> future2 = mock(Future.class);

        when(exportTasks.submitExportTasks(any(ExecutorService.class), eq(uuids)))
                .thenReturn(List.of(future1, future2));

        when(future1.get(30, SECONDS)).thenReturn(null);
        when(future2.get(30, SECONDS)).thenReturn(null);

        job.execute();

        verify(exportService).getNotExportedAgreementUuids();
        verify(exportTasks).submitExportTasks(any(ExecutorService.class), eq(uuids));
        verify(future1).get(30, SECONDS);
        verify(future2).get(30, SECONDS);
    }

    @Test
    void execute_shouldHandleInterruptedException() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(exportService.getNotExportedAgreementUuids()).thenReturn(List.of(uuid));

        Future<?> future = mock(Future.class);
        when(exportTasks.submitExportTasks(any(ExecutorService.class), anyList()))
                .thenReturn(List.of(future));

        doThrow(new InterruptedException()).when(future).get(30, SECONDS);

        job.execute();

        verify(future).cancel(true);
        // Проверяем, что прерывание потока произошло
        assert Thread.currentThread().isInterrupted();
    }

    @Test
    void execute_shouldHandleExecutionException() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(exportService.getNotExportedAgreementUuids()).thenReturn(List.of(uuid));

        Future<?> future = mock(Future.class);
        when(exportTasks.submitExportTasks(any(ExecutorService.class), anyList()))
                .thenReturn(List.of(future));

        doThrow(new ExecutionException(new RuntimeException("Test"))).when(future).get(30, SECONDS);

        job.execute();

        verify(future).get(30, SECONDS);
    }

    @Test
    void execute_shouldHandleTimeoutException() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(exportService.getNotExportedAgreementUuids()).thenReturn(List.of(uuid));

        Future<?> future = mock(Future.class);
        when(exportTasks.submitExportTasks(any(ExecutorService.class), anyList()))
                .thenReturn(List.of(future));

        doThrow(new TimeoutException()).when(future).get(30, SECONDS);

        job.execute();

        verify(future).cancel(true);
    }
}
