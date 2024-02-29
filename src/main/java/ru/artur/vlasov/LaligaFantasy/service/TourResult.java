package ru.artur.vlasov.LaligaFantasy.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TourResult {
    public static String getTourResult(int tourResult) {
        StringBuilder message = new StringBuilder();
        String tourDate = null;
        try {
            Document document = Jsoup.connect("https://laliga.soccerfantasy.ru/match-fantasy/index").get();
            Element tbody = document.select("tbody").first();
            Elements rows = tbody.select("tr");
            boolean foundTourResults = false;
            for (Element row : rows) {
                int tourNumber = Integer.parseInt(row.select("td").first().text());
                if (tourNumber == tourResult) {
                    if (!foundTourResults) {
                        Elements tdElements = row.select("td");
                        tourDate = tdElements.get(1).text();
                        message.append("Дата тура: ").append(tourDate).append("\n\n");
                        foundTourResults = true;
                    }
                    Elements tdElements = row.select("td");
                    String matchInfo = tdElements.get(2).text();
                    String score = tdElements.get(3).text();
                    message.append(matchInfo).append("\n");
                    message.append("Счет: ").append(score).append("\n\n");
                } else if (foundTourResults) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!message.isEmpty()) {
            return "Результаты тура " + tourResult + ":\n" + message;
        } else {
            return "Результаты для тура " + tourResult + " не найдены.";
        }
    }
}


