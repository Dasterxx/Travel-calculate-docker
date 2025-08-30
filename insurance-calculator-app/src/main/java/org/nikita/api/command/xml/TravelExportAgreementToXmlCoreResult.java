package org.nikita.api.command.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nikita.api.exceptions.XmlExportException;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelExportAgreementToXmlCoreResult {
    private boolean success;
    private List<XmlExportException> errors;

    public TravelExportAgreementToXmlCoreResult(boolean success) {
        this.success = success;
    }

    public TravelExportAgreementToXmlCoreResult(List<XmlExportException> errors) {
        this.errors = errors;
        this.success = false;
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}

