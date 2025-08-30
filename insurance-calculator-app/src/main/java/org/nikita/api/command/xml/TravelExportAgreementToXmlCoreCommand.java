package org.nikita.api.command.xml;

import lombok.RequiredArgsConstructor;
import org.nikita.core.service.TravelExportAgreementToXmlCoreService;

import java.util.UUID;

@RequiredArgsConstructor
public class TravelExportAgreementToXmlCoreCommand implements CommandXml {

    private final UUID agreementUuid;
    private final TravelExportAgreementToXmlCoreService exportService;

    @Override
    public void execute() throws Exception {
        exportService.export(agreementUuid);
    }
}

