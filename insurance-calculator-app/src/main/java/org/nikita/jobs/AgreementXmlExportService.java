package org.nikita.jobs;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.response.AgreementResponse;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.mapper.AgreementMapper;
import org.nikita.core.repositories.AgreementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
class AgreementXmlExportService {
    @Value("${export.path}")
    private String exportPath;

    @Value("${AgreementXmlExportService.enabled}")
    private boolean isEnabled;

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;

    private void saveXmlToFile(String xml, String uuid) throws Exception {
        if (!isEnabled) {
            log.warn("XML export is disabled.");
            return;
        }
        File dir = new File(exportPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                log.error("Failed to create directory: {}", dir.getAbsolutePath());
                throw new IOException("Could not create directory " + dir.getAbsolutePath());
            }
        }

        File file = new File(dir, "agreement_" + uuid + ".xml");
        try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write(xml);
        }
    }

    private String createXmlFromAgreementResponse(AgreementDto agreementResponse) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AgreementResponse.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(agreementResponse, sw);
        return sw.toString();
    }

    public List<UUID> getAllAgreementUuids() {
        return agreementRepository.findAllUuids();
    }

    @Transactional
    public void exportAgreementToXml(UUID uuid) throws Exception {
        AgreementEntity loadedAgreement = agreementRepository.findByUuid(uuid);
        if (loadedAgreement == null) {
            throw new IllegalArgumentException("Agreement not found for UUID: " + uuid);
        }
        AgreementDto dto = agreementMapper.toDto(loadedAgreement);
        if (dto == null) {
            throw new IllegalStateException("Mapped AgreementResponse is null for UUID: " + uuid);
        }
        String xml = createXmlFromAgreementResponse(dto);
        saveXmlToFile(xml, uuid.toString());
    }

}
