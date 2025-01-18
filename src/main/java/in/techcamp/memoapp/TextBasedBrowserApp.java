package in.techcamp.memoapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
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
        configureSimpleTextDisplay(webEngine);

        BorderPane root = new BorderPane();
        root.setCenter(browser);

        Scene scene = new Scene(root, 800, 600);

        // キーボードイベントリスナーを追加
        scene.setOnKeyPressed(event -> {
            if (!isFocusedOnInput(browser)) { // フォーカスが入力フィールドでない場合
                if ("H".equalsIgnoreCase(event.getText())) {
                    webEngine.load("https://duckduckgo.com/html/");
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Text-Based Browser");
        primaryStage.show();
    }

    /**
     * ページをテキストベースに最適化
     *
     * @param webEngine WebEngineインスタンス
     */
    private void configureSimpleTextDisplay(WebEngine webEngine) {
        webEngine.documentProperty().addListener((observable, oldDoc, newDoc) -> {
            if (newDoc != null) {
                String script =
                        // 基本の背景と文字色を設定
                        "document.body.style.backgroundColor = 'transparent';" +  // 背景を透明に
                                "document.body.style.color = 'black';" +
                                "document.body.style.fontFamily = 'monospace';" +
                                "document.body.style.fontSize = '20px';" +

                                // 全要素の色と背景を強制的に白黒に
                                "Array.from(document.querySelectorAll('*')).forEach(el => {" +
                                "    el.style.backgroundColor = 'transparent';" + // 背景を透明に
                                "    el.style.color = 'black';" +
                                "    el.style.borderColor = 'black';" +
                                "    el.style.textDecoration = 'none';" + // 下線を初期化
                                "    el.style.boxShadow = 'none';" +
                                "    el.style.outline = 'none';" +
                                "    el.style.backgroundImage = 'none';" +
                                "});" +

                                // リンクを青い文字＆下線付きに
                                "Array.from(document.querySelectorAll('a')).forEach(link => {" +
                                "    link.style.color = 'blue';" +
                                "    link.style.textDecoration = 'underline';" +
                                "});" +

                                // imgタグの置き換え
                                "Array.from(document.querySelectorAll('img, iframe, video')).forEach(el => {" +
                                "    var altText = el.getAttribute('alt') || '[Embedded Media]';" +
                                "    var textNode = document.createTextNode(altText);" +
                                "    el.replaceWith(textNode);" +
                                "});" +

                                // SVG内の画像削除
                                "Array.from(document.querySelectorAll('svg')).forEach(svg => {" +
                                "    var textNode = document.createTextNode('[SVG Content]');" +
                                "    svg.replaceWith(textNode);" +
                                "});" +

                                // Base64画像の削除
                                "Array.from(document.querySelectorAll('img[src^=\"data:image/\"]')).forEach(img => {" +
                                "    var altText = img.getAttribute('alt') || '[Embedded Image]';" +
                                "    var textNode = document.createTextNode(altText);" +
                                "    img.replaceWith(textNode);" +
                                "});" +

                                // 動的変更の監視
                                "new MutationObserver((mutations) => {" +
                                "    mutations.forEach(mutation => {" +
                                "        Array.from(mutation.addedNodes).forEach(node => {" +
                                "            if (node.tagName === 'IMG' || node.tagName === 'VIDEO' || node.tagName === 'IFRAME') {" +
                                "                var altText = node.getAttribute('alt') || '[Embedded Media]';" +
                                "                var textNode = document.createTextNode(altText);" +
                                "                node.replaceWith(textNode);" +
                                "            } else if (node.nodeType === Node.ELEMENT_NODE) {" +
                                "                node.style.backgroundColor = 'transparent';" + // 背景透明
                                "                node.style.color = 'black';" +
                                "                node.style.borderColor = 'black';" +
                                "                node.style.textDecoration = 'none';" +
                                "                node.style.boxShadow = 'none';" +
                                "                node.style.outline = 'none';" +
                                "                node.style.backgroundImage = 'none';" +
                                "                if (node.tagName === 'A') {" + // 新しいリンク要素にスタイル適用
                                "                    node.style.color = 'blue';" +
                                "                    node.style.textDecoration = 'underline';" +
                                "                }" +
                                "            }" +
                                "        });" +
                                "    });" +
                                "}).observe(document.body, { childList: true, subtree: true });";

                try {
                    webEngine.executeScript(script);
                } catch (Exception e) {
                    System.err.println("Error configuring display: " + e.getMessage());
                }
            }
        });
    }

    /**
     * 現在フォーカスされている要素が入力フィールドかどうかを判定
     *
     * @param browser WebViewインスタンス
     * @return 入力フィールドがフォーカスされている場合はtrue
     */
    private boolean isFocusedOnInput(WebView browser) {
        try {
            String focusedElementTag = (String) browser.getEngine().executeScript(
                    "document.activeElement.tagName.toLowerCase();"
            );
            return "input".equals(focusedElementTag) || "textarea".equals(focusedElementTag);
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}