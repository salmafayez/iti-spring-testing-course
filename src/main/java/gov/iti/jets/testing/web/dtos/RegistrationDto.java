package gov.iti.jets.testing.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDto {
    private Long userId;
    private Long eventId;
    private String ticketType;
}
