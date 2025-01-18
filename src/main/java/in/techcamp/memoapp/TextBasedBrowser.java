package in.techcamp.memoapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class TextBasedBrowser {
    private final Scanner scanner;

    public TextBasedBrowser() {
        scanner = new Scanner(System.in);
    }

    // URLを読み込み、テキストを表示
    public void loadURL(String url) {
        try {
            // HTMLを取得して解析
            Document document = Jsoup.connect(url).get();
            String textContent = document.body().text();

            // ページのテキストを表示
            System.out.println("==== Page Content ====");
            System.out.println(textContent);

            // ページのリンクを抽出して表示
            Elements links = document.select("a[href]");
            System.out.println("\n==== Links ====");
            int index = 1;
            for (Element link : links) {
                System.out.printf("[%d] %s (%s)%n", index++, link.text(), link.attr("abs:href"));
            }

            // ユーザー入力によるリンクのフォロー
            handleUserInput(links);
        } catch (IOException e) {
            System.err.println("Error loading URL: " + e.getMessage());
        }
    }

    // ユーザーの入力を処理
    private void handleUserInput(Elements links) {
        System.out.println("\nEnter the number of the link to follow, or 'q' to quit:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting browser.");
                break;
            }
            try {
                int linkIndex = Integer.parseInt(input) - 1;
                if (linkIndex >= 0 && linkIndex < links.size()) {
                    String nextURL = links.get(linkIndex).attr("abs:href");
                    System.out.println("\nLoading: " + nextURL);
                    loadURL(nextURL);
                } else {
                    System.out.println("Invalid link number. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'q' to quit.");
            }
        }
    }

    public static void main(String[] args) {
        TextBasedBrowser browser = new TextBasedBrowser();
        System.out.println("Enter a URL to browse:");
        Scanner scanner = new Scanner(System.in);
        String url = scanner.nextLine();
        browser.loadURL(url);
    }
}