package org.nikita.core.service;

import org.nikita.api.command.xml.TravelExportAgreementToXmlCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.exceptions.XmlExportException;

import java.util.List;

public interface ITravelResponseBuilderXml {
    TravelExportAgreementToXmlCoreResult buildErrorResponse(List<XmlExportException> errors);

    TravelExportAgreementToXmlCoreResult buildSuccessResponse(AgreementDto agreement);
}
