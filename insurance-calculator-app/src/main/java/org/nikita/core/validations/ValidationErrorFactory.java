package org.nikita.core.validations;

import org.nikita.core.util.Placeholder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Component
public class ValidationErrorFactory {

    private Properties props;

    public ValidationErrorFactory() {
        try {
            Resource resource = new ClassPathResource("error-codes.properties");
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load error-codes.properties", e);
        }
    }

    public String getErrorDescription(String errorCode) {
        String description = props.getProperty(errorCode);
        if (description == null) {
            return "Unknown error code: " + errorCode;
        }
        return description;
    }

    public String getErrorDescription(String errorCode, List<Placeholder> placeholders) {
        String errorDescription = props.getProperty(errorCode);
        if (errorDescription == null) {
            return "Unknown error code: " + errorCode;
        }
        if (placeholders != null) {
            for (Placeholder placeholder : placeholders) {
                String placeholderToReplace = "{" + placeholder.getPlaceholderName() + "}";
                errorDescription = errorDescription.replace(placeholderToReplace, placeholder.getPlaceholderValue());
            }
        }
        return errorDescription;
    }
}

