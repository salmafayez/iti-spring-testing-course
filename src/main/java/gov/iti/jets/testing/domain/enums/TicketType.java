package gov.iti.jets.testing.domain.enums;

import lombok.Getter;

import java.util.Arrays;

public enum TicketType {
    Silver("silver"),
    Gold("gold");

    @Getter
    private final String name;

    TicketType(String name) {
        this.name = name;
    }

    public static TicketType getByName(String name) {
        return Arrays.stream(TicketType.values())
                .filter(languageEnum -> languageEnum.getName().equals(name))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
