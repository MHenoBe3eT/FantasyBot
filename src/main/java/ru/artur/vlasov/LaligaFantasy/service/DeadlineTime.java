package ru.artur.vlasov.LaligaFantasy.service;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.artur.vlasov.LaligaFantasy.model.Deadline;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class DeadlineTime {

    public String getDeadlineTime(Deadline deadline, String urlTime, String urlTeams) throws IOException {
        URL DeadLine = new URL(urlTime);
        Scanner in = new Scanner((InputStream) DeadLine.getContent());
        String resultDeadline = "";
        URL urlTeamsWithoutSquad = new URL(urlTeams);
        Scanner inTeams = new Scanner((InputStream) urlTeamsWithoutSquad.getContent());
        while (in.hasNext()) {
            resultDeadline += in.nextLine();
        }
        String resultTeamsWithoutSquad = "";
        while (inTeams.hasNext()) {
            resultTeamsWithoutSquad += inTeams.nextLine();
        }
        JSONObject object = new JSONObject(resultDeadline);
        JSONArray jsonArray = new JSONArray(resultTeamsWithoutSquad);

        StringBuilder teamsInfo = new StringBuilder("Команды без составов:\n");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject teamObject = jsonArray.getJSONObject(i);
            String teamName = teamObject.getString("teamName");
            String telegramId = teamObject.getString("telegramId");

            teamsInfo.append(teamName).append(" (").append(telegramId).append(")").append("\n");
        }

        deadline.setTour(object.getInt("tour"));
        deadline.setDeadline(object.getString("deadline"));
        deadline.setSecondsBeforeTour(object.getLong("secondsBeforeTour"));
        return "Номер тура : " + deadline.getTour() + "\n" +
                "Время deadline : " + deadline.getDeadline() + "\n" +
                "До deadline : " + (deadline.getSecondsBeforeTour() / 3600) + " часов"+ "\n" +
                "\n" +
                teamsInfo + "\n";

    }

}