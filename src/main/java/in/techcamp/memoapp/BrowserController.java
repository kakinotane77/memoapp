package in.techcamp.memoapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrowserController {

    @GetMapping("/textBasedBrowserLauncher")
    public String launchBrowser() {
        new Thread(() -> {
            try {
                TextBasedBrowserApp.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return "redirect:/"; // 必要に応じて適切なビューを返します
    }
}