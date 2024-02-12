package com.local.vincent.contoroller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.local.vincent.properties.LineplatformProperties;
import com.local.vincent.replier.BubbleSample;
import com.local.vincent.replier.CarouselSample;
import com.local.vincent.replier.DialogAnswer;
import com.local.vincent.replier.Greet;
import com.local.vincent.replier.Omikuji;
import com.local.vincent.replier.Parrot;
import com.local.vincent.service.GetLineplatformService;

@RestController
@LineMessageHandler
public class Callback {
	private static final Logger log = LoggerFactory.getLogger(Callback.class);
	private LineBlobClient client;
	private GetLineplatformService getLineplatformService;
	private final LineplatformProperties lineplatformProperties;
	private LineMessagingClient lineMessagingClient;

	@Autowired
	public Callback(LineBlobClient client, GetLineplatformService getLineplatformService,
			LineplatformProperties lineplatformProperties, LineMessagingClient lineMessagingClient) {

		this.client = client;
		this.getLineplatformService = getLineplatformService;
		this.lineplatformProperties = lineplatformProperties;
		this.lineMessagingClient = lineMessagingClient;
	}

	// フォローイベントに対応する
	@EventMapping
	public void handleFollow(FollowEvent event) {
		// フォーローイベントと同時にユーザーのプロフィール情報を取得する
		String userId = event.getSource().getUserId();
		String userName = getLineplatformService.getUserinf(userId, lineplatformProperties.getAccessToken());

		// ユーザーIDをUserテーブルに保存
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(userId, headers);
		ResponseEntity<String> resp = new RestTemplate().postForEntity(lineplatformProperties.getUrl() + "/insertUser", requestEntity, String.class);

		// フォローリプライ
		TextMessage textMessage = new TextMessage(String.format("%Sさん 登録ありがとう！  \nお手紙LIFEを楽しんでね！", userName));
		lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage)));
	}

	@EventMapping
	public void handleUnfollow(UnfollowEvent Unfollow) {
		String lineUserId = Unfollow.getSource().getUserId();
		
		// ユーザーIDをUserテーブルから削除
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(lineUserId, headers);
		RestTemplate rt = new RestTemplate();
		rt.postForEntity(lineplatformProperties.getUrl() + "/deleteUser", requestEntity, String.class);
		System.out.println("ユーザーを削除しました lineUserId:" + lineUserId);
	}

	// 文章で話しかけられた時(テキストメッセージのイベント)に対応する
	@EventMapping
	public Message handleMessage(MessageEvent<TextMessageContent> event) {
		TextMessageContent tmc = event.getMessage();
		String text = tmc.getText();
		switch (text) {
		case "やあ":
			Greet greet = new Greet();
			return greet.reply();
		case "おみくじ":
			Omikuji omikuji = new Omikuji();
			return omikuji.reply();
		case "バブル":
			BubbleSample bubbleSample = new BubbleSample();
			return bubbleSample.reply();
		case "カルーセル":
			CarouselSample carouselSample = new CarouselSample();
			return carouselSample.reply();
		case "リッチメニュー":
			String url = lineplatformProperties.getUrl() + "addRich";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(event.getSource().getUserId(), headers);
			ResponseEntity<String> resp = new RestTemplate().postForEntity(url, requestEntity, String.class);
			if (resp.getBody() != null)
			 return new TextMessage(resp.getBody());
		default:
			Parrot parrot = new Parrot(event);
			return parrot.reply();
		}
	}

	// 画像のメッセージイベントに対応する
	@EventMapping
	public Message hadleImg(MessageEvent<ImageMessageContent> event) {
		// ①画像メッセージのidを取得する
		String msgId = event.getMessage().getId();
		Optional<String> opt = Optional.empty();

		try {
			// ②画像メッセージのIDを使って MessageContentResoponse を取得する
			MessageContentResponse resp = client.getMessageContent(msgId).get();
			log.info("get content{}:", resp);
			// ③MessageContentResponse からファイルをローカルに保存する
			// ※Lineでは、どの解像度で写真を送っても、サーバー側でjpgファイルに変換される
			opt = makeTmpFile(resp, ".jpg");

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		// ④ファイルが保存できたことが確認できるように、ローカルファイルパスをコールバックする
		// 運用ではファイルパスを表示することは避けましょう
		String path = opt.orElseGet(() -> "ファイル書き込みNG");
		return new TextMessage(path);
	}

	// MessageContentResponseの中のバイト入力ストリームを、拡張子を指定してファイルに書き込む。
	// また保存先のファイルパスをOptional型で返す。
	private Optional<String> makeTmpFile(MessageContentResponse resp, String extentino) {
		// また、保存先のファイルパスをOptional型で返す。
		try (InputStream is = resp.getStream()) {
			Path tmpFilePath = Files.createTempFile("linebot", extentino);
			Files.copy(is, tmpFilePath, StandardCopyOption.REPLACE_EXISTING);
			return Optional.ofNullable(tmpFilePath.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	// PostBackEventに対する
	@EventMapping
	public Message handlePostBack(PostbackEvent event) {
		DialogAnswer dialogAnswer = new DialogAnswer(event);
		return dialogAnswer.reply();
	}
}
