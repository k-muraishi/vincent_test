package com.local.vincent.replier;

import java.time.LocalTime;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

// 挨拶用の返信クラス
public class Greet implements Replier {

	@Override
	public Message reply() {
		LocalTime lt = LocalTime.now();
		int hour = lt.getHour();
		if (hour >= 17) {
			return new TextMessage("こんばんは〜");
		}
		if (hour >= 11) {
			return new TextMessage("こんにちはぁ");
		}
		return new TextMessage("おはよう！");
	}

}
