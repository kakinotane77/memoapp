package in.techcamp.memoapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class TextBasedBrowserApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url = "https://duckduckgo.com/html/"; // 初期URL

        while (true) {
            System.out.println("\nAccessing: " + url);

            try {
                // URL から HTML を取得
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (compatible; Lynx/2.8.9rel.1) Text-Based Browser").get();

                // ページタイトルを表示
                System.out.println("\nTitle: " + doc.title());

                // ページ内容を表示
                System.out.println("\nContent:");
                System.out.println(getTextContent(doc));

                // リンク一覧を表示
                System.out.println("\nLinks:");
                printLinks(doc);

            } catch (IOException e) {
                System.err.println("Error loading page: " + e.getMessage());
            }

            // 次の操作を入力
            System.out.println("\nEnter a number to follow a link, 'h' to reload the home page, or 'q' to quit:");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting browser.");
                break;
            } else if (input.equalsIgnoreCase("h")) {
                url = "https://duckduckgo.com/html/"; // ホームページに戻る
            } else {
                try {
                    int linkIndex = Integer.parseInt(input);
                    String nextUrl = getLinkByIndex(url, linkIndex);
                    if (nextUrl != null) {
                        url = nextUrl;
                    } else {
                        System.out.println("Invalid link number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number or a valid command.");
                }
            }
        }

        scanner.close();
    }

    /**
     * ページ本文のテキストを抽出
     *
     * @param doc Jsoup の Document インスタンス
     * @return ページ本文のテキスト
     */
    private static String getTextContent(Document doc) {
        Element body = doc.body();
        return body.text(); // 本文のテキストのみ取得
    }

    /**
     * ページ内のリンクを一覧表示
     *
     * @param doc Jsoup の Document インスタンス
     */
    private static void printLinks(Document doc) {
        Elements links = doc.select("a[href]"); // href 属性を持つ <a> 要素を選択
        int index = 1;
        for (Element link : links) {
            System.out.printf("[%d] %s (%s)%n", index++, link.text(), link.absUrl("href"));
        }
    }

    /**
     * リンクのインデックスから URL を取得
     *
     * @param baseUrl 現在のページの URL
     * @param index   リンクのインデックス（1 から始まる）
     * @return リンクの URL
     */
    private static String getLinkByIndex(String baseUrl, int index) {
        try {
            Document doc = Jsoup.connect(baseUrl).get();
            Elements links = doc.select("a[href]");
            if (index > 0 && index <= links.size()) {
                return links.get(index - 1).absUrl("href");
            }
        } catch (IOException e) {
            System.err.println("Error retrieving links: " + e.getMessage());
        }
        return null;
    }
}