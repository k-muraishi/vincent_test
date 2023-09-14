package com.local.vincent.contoroller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.action.URIAction.AltUri;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuArea;
import com.linecorp.bot.model.richmenu.RichMenuBounds;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuSize;
import com.local.vincent.properties.LineplatformProperties;

@RestController
public class RichMenuController {
    public static final Logger log = LoggerFactory.getLogger(Push.class);
    private final LineplatformProperties  lineplatformProperties;
    private final LineMessagingClient messagingClient;
    private final LineBlobClient blobClient;

    @Autowired
    public RichMenuController(LineMessagingClient lineMessagingClient, LineBlobClient blobClient, LineplatformProperties  lineplatformProperties) {
        this.messagingClient = lineMessagingClient;
        this.blobClient = blobClient;
        this.lineplatformProperties = lineplatformProperties;
    }

    // リッチメニューを作成する
    @GetMapping("addRich")
    public String addRichMenu() {
        String text = "リッチメニューを表示します";

        // ①リッチメニューを作成
        // それぞれの意味は https://developers.line.me/ja/reference/messaging-api/#rich-menu-object を参照
        RichMenu richMenu = RichMenu.builder()
                .name("リッチメニュー")
                .chatBarText("コントローラー")
                .areas(makeRichMenuAreas())
                .selected(true)
                .size(RichMenuSize.FULL)
                .build();
        try {

            // ②作成したリッチメニューの登録( rest1 は作成結果で、リッチメニューIDが入っている)
            RichMenuIdResponse resp1 = messagingClient.createRichMenu(richMenu).get();
            log.info("create richmenu:{}", resp1);

            // ③リッチメニューの背景画像の設定( resp2 は、画像の登録結果)
            // 画像の仕様は公式ドキュメントを参照されたい
            // ここでは、src/resource/img/RichMenuSample.jpg（ビルド後は classpath:/img/RichMenuSample.jpg）を指定
            ClassPathResource cpr = new ClassPathResource("/img/RichMenuSample.jpg");
            byte[] fileContent = Files.readAllBytes(cpr.getFile().toPath());
            BotApiResponse resp2 = blobClient.setRichMenuImage(resp1.getRichMenuId(), "image/jpeg", fileContent).get();
            log.info("set richmenu image:{}", resp2);

            // ④リッチメニューIDをユーザーIDとリンクする( resp3 は、紐付け結果)
            // リンクすることで作成したリッチメニューを使えるようになる
            BotApiResponse resp3 = messagingClient.linkRichMenuIdToUser(lineplatformProperties.getVincentUserId(), resp1.getRichMenuId()).get();
            log.info("link richmenu:{}", resp3);
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    @GetMapping("delRich")
    public String delRichMenu() {
        String text = "リッチメニューを全て削除します";
        try {

            // ①ユーザーからリッチメニューを削除する(※Messaging APIで作成したものだけ)
            messagingClient.unlinkRichMenuIdFromUser(lineplatformProperties.getVincentUserId());

            // ②作成されているリッチメニューの取得( resp4 はリッチメニューの一覧情報)
            RichMenuListResponse resp4 = messagingClient.getRichMenuList().get();
            log.info("get richmenus:{}", resp4);

            // ③リッチメニューIdを指定して削除する
            // ユーザーIdの対応をDBなどに保存しておいて、不要なものだけ削除する
            resp4.getRichMenus().stream()
                    .forEach(r -> messagingClient.deleteRichMenu(r.getRichMenuId()));

        } catch (InterruptedException | ExecutionException e){
            throw new RuntimeException(e);
        }
        return text;
    }

    // 画像のどの部分（ピクセル）に、どんな動作をするリッチメニューを割り当てるか設定します
    private List<RichMenuArea> makeRichMenuAreas() {
        final ArrayList<RichMenuArea> richMenuAreas = new ArrayList<>();
        richMenuAreas.add(makeMessageAction(551, 325, 321, 321, "Up"));
        richMenuAreas.add(makeMessageAction(876, 651, 321, 321, "Right"));
        richMenuAreas.add(makeMessageAction(551, 972, 321, 321, "Down"));
        richMenuAreas.add(makeMessageAction(225, 651, 321, 321, "Left"));
        richMenuAreas.add(makeURIAction(1433, 657, 367, 367, "a", lineplatformProperties.getLiffUrl()));// liffアプリを設定
        richMenuAreas.add(makeDateTimeAction(1907, 657, 367, 367, "b"));
        return richMenuAreas;
    }

    // Botにメッセージを送信する動作をリッチメニューとして割り当てます
    private RichMenuArea makeMessageAction(int x, int y, int w, int h, String label) {
        return new RichMenuArea(new RichMenuBounds(x, y, w, h),
                new MessageAction(label, label + "　is tapped！"));
    }

    // アプリ内ブラウザでWebサイトを表示する動作をリッチメニューとして割り当てます
    private RichMenuArea makeURIAction(int x, int y, int w, int h, String label, String uri) {
        return new RichMenuArea(new RichMenuBounds(x, y, w, h),
                new URIAction(label, URI.create(uri), new AltUri(URI.create(uri))));
    }

    // Botに日時イベントを送信する動作をリッチメニューとして割り当てます
    private RichMenuArea makeDateTimeAction(int x, int y, int w, int h, String label) {
        return new RichMenuArea(new RichMenuBounds(x, y, w, h),
                DatetimePickerAction.OfLocalDatetime.builder()
                        .label(label)
                        .data("DT")
                        .initial(LocalDateTime.now())
                        .min(LocalDateTime.now().minusYears(10l))
                        .max(LocalDateTime.now().plusYears(10l))
                        .build());
    }
}



