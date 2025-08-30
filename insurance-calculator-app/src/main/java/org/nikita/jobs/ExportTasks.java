package org.nikita.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.xml.CommandXml;
import org.nikita.api.command.xml.TravelExportAgreementToXmlCoreCommand;
import org.nikita.core.service.TravelExportAgreementToXmlCoreService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
@Component
class ExportTasks {

    private final TravelExportAgreementToXmlCoreService exportService;

    public List<Future<?>> submitExportTasks(ExecutorService executor, List<UUID> uuids) {
        List<Future<?>> futures = new CopyOnWriteArrayList<>();

        for (UUID uuid : uuids) {
            CommandXml command = new TravelExportAgreementToXmlCoreCommand(uuid, exportService);
            futures.add(executor.submit(() -> {
                try {
                    command.execute();
                    log.info("Agreement {} exported successfully", uuid);
                } catch (Exception e) {
                    log.error("Error exporting agreement {}: {}", uuid, e.getMessage(), e);
                }
            }));
        }
        return futures;
    }
}
