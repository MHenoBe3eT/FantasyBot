package ru.artur.vlasov.LaligaFantasy.config;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@Getter
@Setter
public class DeadlineModel {
    Integer tour;
    String deadline;
    long secondsBeforeTour;
    String teamName;
    String telegramId;


}

