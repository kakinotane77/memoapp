package in.techcamp.memoapp;

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class TextBasedBrowser extends BorderPane {
    private final WebView webView;
    private final WebEngine webEngine;
    private final TextArea textArea;

    public TextBasedBrowser() {
        // WebViewとWebEngineを初期化
        webView = new WebView();
        webEngine = webView.getEngine();

        // テキスト表示用のTextArea
        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);

        // レイアウトの設定
        this.setCenter(textArea);

        // Webページ読み込みとテキスト抽出
        webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                String content = webEngine.executeScript("document.body.innerText").toString();
                textArea.setText(content);
            }
        });
    }

    // ページの読み込み
    public void loadURL(String url) {
        webEngine.load(url);
    }
}