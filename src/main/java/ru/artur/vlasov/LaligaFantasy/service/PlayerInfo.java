package ru.artur.vlasov.LaligaFantasy.service;

import org.json.JSONObject;
import ru.artur.vlasov.LaligaFantasy.model.Player;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class PlayerInfo {
    public static String getPlayersInfo(String message, Player player,String leagueUrl) throws IOException {
        URL url = new URL(leagueUrl + message);
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        if (!result.startsWith("{")) {
            return "По этой ссылке игрока в базе нет. Либо ссылка не корректная.";
        }
        JSONObject object = new JSONObject(result);

        player.setName(object.getString("name"));
        player.setPosition(object.getString("position"));
        player.setTeam(object.getString("team"));
        player.setTeamFantasy(object.getString("teamFantasy"));
        return "Имя : " + player.getName() + "\n" +
                "Позиция : " + player.getPosition() + "\n" +
                "Команда в реальности : " + player.getTeam() + "\n" +
                "Команда Fantasy : " + player.getTeamFantasy() + "\n";

    }
}
