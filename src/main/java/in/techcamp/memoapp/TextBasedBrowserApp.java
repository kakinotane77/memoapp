package in.techcamp.memoapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class TextBasedBrowserApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();

        // JavaScriptを完全に無効化
        webEngine.setJavaScriptEnabled(false);

        // カスタムUser-Agentでテキストベースを模倣
        webEngine.setUserAgent("Mozilla/5.0 (compatible; Lynx/2.8.9rel.1) Text-Based Browser");

        // DuckDuckGo HTMLバージョンをロード
        webEngine.load("https://duckduckgo.com/html/");

        // ページのシンプル表示を設定
        configureTextOnlyDisplay(webEngine);

        BorderPane root = new BorderPane();
        root.setCenter(browser);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text-Based Browser (DuckDuckGo)");
        primaryStage.show();
    }

    /**
     * ページをテキストベースに最適化（画像や余計な要素を削除）
     *
     * @param webEngine WebEngineインスタンス
     */
    private void configureTextOnlyDisplay(WebEngine webEngine) {
        webEngine.documentProperty().addListener((observable, oldDoc, newDoc) -> {
            if (newDoc != null) {
                // 画像や動画、iframeを非表示にするCSS
                String css =
                        "body { background-color: white; color: black; font-family: monospace; font-size: 18px; }" +
                                "img, video, iframe, script, style { display: none !important; }" +
                                "a { color: blue; text-decoration: underline; font-weight: bold; }" +
                                "a:hover { text-decoration: none; }";

                // DOM操作で不要な要素を削除
                String script =
                        "Array.from(document.querySelectorAll('img, video, iframe, script, style')).forEach(el => el.remove());" +
                                "var style = document.createElement('style');" +
                                "style.type = 'text/css';" +
                                "style.innerHTML = `" + css + "`;" +
                                "document.head.appendChild(style);";

                try {
                    // スクリプトの実行
                    webEngine.executeScript(script);
                } catch (Exception e) {
                    System.err.println("Error executing script: " + e.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}