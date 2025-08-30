package org.nikita.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.xml.TravelExportAgreementToXmlCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.exceptions.XmlExportException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TravelResponseBuilderXmlTest {
    private TravelResponseBuilderXml builder;

    @BeforeEach
    void setUp() {
        builder = new TravelResponseBuilderXml();
    }

    @Test
    void testBuildErrorResponseWithXmlExportExceptions() {
        XmlExportException error1 = new XmlExportException("Error 1", null);
        XmlExportException error2 = new XmlExportException("Error 2", null);

        List<XmlExportException> errors = Arrays.asList(error1, error2);

        TravelExportAgreementToXmlCoreResult result = builder.buildErrorResponse(errors);

        assertFalse(result.isSuccess());
        assertNotNull(result.getErrors());
        assertEquals(2, result.getErrors().size());
        assertEquals("Error 1", result.getErrors().get(0).getMessage());
        assertEquals("Error 2", result.getErrors().get(1).getMessage());
    }

    @Test
    void testBuildSuccessResponse() {
        AgreementDto agreement = new AgreementDto();
        agreement.setUuid(java.util.UUID.randomUUID());

        TravelExportAgreementToXmlCoreResult result = builder.buildSuccessResponse(agreement);

        assertTrue(result.isSuccess());
        assertNull(result.getErrors());
    }
}