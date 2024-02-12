package com.local.vincent.replier;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

// フォローされた時用の返信クラス
public class Follow implements Replier {

	private final String userName;

	public Follow(String userName) {

		this.userName = userName;
	}

	@Override
	public Message reply() {
		String text = String.format("%Sさん 登録ありがとう！  \nお手紙LIHEを楽しんでね！", userName);
		return new TextMessage(text);
	}
}
