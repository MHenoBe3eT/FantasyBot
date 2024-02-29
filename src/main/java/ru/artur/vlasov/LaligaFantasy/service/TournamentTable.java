package ru.artur.vlasov.LaligaFantasy.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TournamentTable {
    public static String getTournamentTable() {
        StringBuilder message = new StringBuilder();

        try {
            Document document = Jsoup.connect("https://laliga.soccerfantasy.ru/table-fantasy/index").get();
            Element tbody = document.select("tbody").first();
            Elements rows = tbody.select("tr");
            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cells = row.select("td:lt(6)");
                if (cells.size() >= 6) {
                    String column1 = cells.get(0).text();
                    String column2 = cells.get(1).text();
                    String column3 = cells.get(2).text();

                    message.append(column1).append(" ");
                    message.append(column2).append(" ");
                    message.append(column3).append("\n");

                } else {
                    message.append("Ошибка");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.append("Произошла ошибка при извлечении данных.\n");
        }

        return message.toString();
    }
}

