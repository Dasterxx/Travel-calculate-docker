package org.nikita.core.service;

import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.xml.TravelExportAgreementToXmlCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.exceptions.XmlExportException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class TravelResponseBuilderXml implements ITravelResponseBuilderXml{
    @Override
    public TravelExportAgreementToXmlCoreResult buildErrorResponse(List<XmlExportException> errors) {
        log.warn("Building response with XML export exceptions: {}", errors);
        TravelExportAgreementToXmlCoreResult result = new TravelExportAgreementToXmlCoreResult();
        result.setErrors(errors);
        result.setSuccess(false);
        return result;
    }

    @Override
    public TravelExportAgreementToXmlCoreResult buildSuccessResponse(AgreementDto agreement) {
      //  log.info("Successfully exported agreement {} to XML", agreement.getUuid());
        return new TravelExportAgreementToXmlCoreResult(true);
    }

}
