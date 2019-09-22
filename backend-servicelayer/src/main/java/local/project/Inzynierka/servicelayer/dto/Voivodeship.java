package local.project.Inzynierka.servicelayer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Voivodeship {
    SLASKIE("śląskie"),
    DOLNOSLASKIE("dolnośląskie"),
    KUJAWSKOPOMORSKIE("kujawsko-pomorskie"),
    LUBELSKIE("lubelskie"),
    LUBUSKIE("lubuskie"),
    LODZKIE("łódzkie"),
    MALOPOLSKIE("małopolskie"),
    MAZOWIECKIE("mazowieckie"),
    OPOLSKIE("opolskie"),
    PODKARPACKIE("podkarpackie"),
    PODLASKIE("podlaskie"),
    SWIETOKRZYSKIE("świętokrzyskie"),
    WARMINSKOMAZURSKIE("warmińsko-mazurskie"),
    WIELKOPOLSKIE("wielkopolskie"),
    ZACHODNIOPOMORSKIE("zachodniopomorskie");

    private final String voivodeship;

    Voivodeship(String voivodeship){
        this.voivodeship = voivodeship;
    }


    @Override
    @JsonValue
    public String toString() {
        return this.voivodeship;
    }


    @JsonCreator
    public static Voivodeship fromVoivodeship(String value) {
        if(value == null ) {
            throw new IllegalArgumentException();
        }

        return Arrays.stream(Voivodeship.values())
                .filter(v ->  v.toString().equals(value.toLowerCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
