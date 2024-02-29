package ru.artur.vlasov.LaligaFantasy.config;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class PlayerInfo {
    public static String getPlayersInfo(String message, Model model) throws IOException {
        URL url = new URL("http://laliga.soccerfantasy.ru/api/player?link=" + message);
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        if (!result.startsWith("{")) {
            return "По этой ссылке игрока в базе нет. Либо ссылка не корректная.";
        }
        JSONObject object = new JSONObject(result);

        model.setName(object.getString("name"));
        model.setPosition(object.getString("position"));
        model.setTeam(object.getString("team"));
        model.setTeamFantasy(object.getString("teamFantasy"));
        return "Имя : " + model.getName() + "\n" +
                "Позиция : " + model.getPosition() + "\n" +
                "Команда в реальности : " + model.getTeam() + "\n" +
                "Команда Fantasy : " + model.getTeamFantasy() + "\n";

    }
}
