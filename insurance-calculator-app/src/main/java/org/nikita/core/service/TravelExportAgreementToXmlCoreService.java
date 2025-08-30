package org.nikita.core.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.AgreementDto;
import org.nikita.core.domain.AgreementXmlExportEntity;
import org.nikita.core.repositories.AgreementXmlExportRepository;
import org.nikita.core.repositories.AgreementRepository;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.mapper.AgreementMapper;
import org.nikita.api.dto.response.AgreementResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelExportAgreementToXmlCoreService {

    private final AgreementXmlExportRepository exportRepository;
    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;

    @Value("${export.path}")
    private String exportPath;

    private void saveXmlToFile(String xml, String uuid) throws IOException {
        File dir = new File(exportPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create directory " + dir.getAbsolutePath());
        }

        File file = new File(dir, "agreement_" + uuid + ".xml");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(xml);
        }
    }

    private String createXmlFromAgreementResponse(AgreementDto agreementResponse) throws JAXBException {
        if (agreementResponse == null) {
            throw new IllegalArgumentException("AgreementResponse is null");
        }

        JAXBContext context = JAXBContext.newInstance(AgreementResponse.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();
        marshaller.marshal(agreementResponse, sw);
        return sw.toString();
    }

    @Transactional
    public void export(UUID agreementUuid) throws Exception {
        if (exportRepository.existsByAgreementUuid(agreementUuid)) {
            log.info("Agreement {} already exported. Skipping.", agreementUuid);
            return;
        }

        AgreementEntity agreementEntity = agreementRepository.findByUuid(agreementUuid);
        if (agreementEntity == null) {
            throw new IllegalArgumentException("Agreement with UUID " + agreementUuid + " not found");
        }

        AgreementDto dto = agreementMapper.toDto(agreementEntity);
        String xml = createXmlFromAgreementResponse(dto);
        saveXmlToFile(xml, agreementUuid.toString());

        AgreementXmlExportEntity exportEntity = new AgreementXmlExportEntity(agreementUuid, true);
        exportRepository.save(exportEntity);

        log.info("Agreement {} exported and marked successfully.", agreementUuid);
    }

    public List<UUID> getNotExportedAgreementUuids() {
        List<UUID> allUuids = agreementRepository.findAllUuids();

        return allUuids.stream()
                .filter(uuid -> !exportRepository.existsByAgreementUuid(uuid))
                .toList();
    }
}
