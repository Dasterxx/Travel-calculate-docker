package org.nikita.core.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProposalAckMessageDto {
    private String firstName;
    private String lastName;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private String proposalFilePath;
}
