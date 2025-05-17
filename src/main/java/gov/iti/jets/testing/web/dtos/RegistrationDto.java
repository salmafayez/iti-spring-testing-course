package gov.iti.jets.testing.web.dtos;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class RegistrationDto {
    private Long userId;
    private Long eventId;
    //add validation
    private String ticketType;
}
