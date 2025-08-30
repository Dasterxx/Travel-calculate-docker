package org.nikita.core.messagebroker;

import org.nikita.core.api.dto.AgreementDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Component
public class ProposalGenerator {

    @Value("${proposals.directory.path}")
    private String proposalsDirectoryPath;

    public String generateProposalAndStoreToFile(AgreementDto agreementDto) throws IOException {
        if (agreementDto.getAgreementDateFrom() == null || agreementDto.getAgreementDateTo() == null) {
            throw new IllegalArgumentException("agreementDateFrom or agreementDateTo is null, cannot generate file name");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateFromFormatted = agreementDto.getAgreementDateFrom().format(formatter);
        String dateToFormatted = agreementDto.getAgreementDateTo().format(formatter);

        Path path = pathParameters(agreementDto, dateFromFormatted, dateToFormatted);
        // Change extension from .txt to .pdf
        String pdfFileName = path.toString().replace(".txt", ".pdf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Load font from resources
            try (InputStream fontStream = getClass().getResourceAsStream("/fonts/DejaVuSans.ttf")) {
                if (fontStream == null) {
                    throw new IOException("Font file not found in resources/fonts/DejaVuSans.ttf");
                }
                PDType0Font font = PDType0Font.load(document, fontStream);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(font, 16);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 750);
                    contentStream.showText("Страховое предложение");

                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(0, -30);
                    contentStream.showText("Период: " + dateFromFormatted + " - " + dateToFormatted);

                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Страховая сумма: " + agreementDto.getInsuranceLimit());

                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Премия: " + agreementDto.getAgreementPremium());

                    if (agreementDto.getPersons() != null && !agreementDto.getPersons().isEmpty()) {
                        var person = agreementDto.getPersons().get(0);
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText("Клиент: " + person.getFirstName() + " " + person.getLastName());
                    }

                    contentStream.endText();
                }
            }

            document.save(pdfFileName);
        }
        return pdfFileName;
    }

    private Path pathParameters(AgreementDto agreementDto, String dateFromFormatted, String dateToFormatted) {
        String personLastName = "unknown";
        if (agreementDto.getPersons() != null && !agreementDto.getPersons().isEmpty()) {
            personLastName = agreementDto.getPersons().get(0).getLastName();
            if (personLastName == null || personLastName.isBlank()) {
                personLastName = "unknown";
            }
        }
        String personFirstName = "unknown";
        if (agreementDto.getPersons() != null && !agreementDto.getPersons().isEmpty()) {
            personFirstName = agreementDto.getPersons().get(0).getFirstName();
            if (personFirstName == null || personFirstName.isBlank()) {
                personFirstName = "unknown";
            }
        }

        String fileName = dateFromFormatted + "-" + dateToFormatted + "-" + personFirstName + "-" + personLastName + ".txt";
        return Path.of(proposalsDirectoryPath, fileName);
    }
}
