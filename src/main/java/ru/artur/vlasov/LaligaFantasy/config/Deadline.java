package ru.artur.vlasov.LaligaFantasy.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Deadline {
    public static String getDeadlineStatus(DeadlineModel deadlineModel) throws IOException {
        URL urlDedline = new URL("https://laliga.soccerfantasy.ru/api/deadline");
        Scanner in = new Scanner((InputStream) urlDedline.getContent());
        String resultDeadline = "";
        URL urlTeamsWithoutSquad = new URL("https://laliga.soccerfantasy.ru/api/teams-without-squad");
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

        deadlineModel.setTour(object.getInt("tour"));
        deadlineModel.setDeadline(object.getString("deadline"));
        deadlineModel.setSecondsBeforeTour(object.getLong("secondsBeforeTour"));
        return "Номер тура : " + deadlineModel.getTour() + "\n" +
                "Время deadline : " + deadlineModel.getDeadline() + "\n" +
                "До deadline : " + (deadlineModel.getSecondsBeforeTour() / 3600) + " часов"+ "\n" +
                "\n" +
                teamsInfo + "\n";

    }

//    public static long getHoursBeforeDedline(DeadlineModel deadlineModel) throws IOException {
//        URL url = new URL("https://laliga.soccerfantasy.ru/api/deadline");
//        Scanner in = new Scanner((InputStream) url.getContent());
//        String result = "";
//        JSONObject object = new JSONObject(result);
//        deadlineModel.setSecondsBeforeTour(object.getLong("secondsBeforeTour"));
//        long hoursBeforeDeadline = deadlineModel.getSecondsBeforeTour() / 60;
//        return hoursBeforeDeadline;
//    }
}
