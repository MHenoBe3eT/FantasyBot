package ru.artur.vlasov.LaligaFantasy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Deadline {
    Integer tour;
    String deadline;
    long secondsBeforeTour;
    String teamName;
    String telegramId;


}
