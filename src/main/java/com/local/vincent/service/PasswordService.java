package com.local.vincent.service;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.local.vincent.properties.LineplatformProperties;

@Service
public class PasswordService {

	private final LineplatformProperties lineplatoformProperties;

	@Autowired
	public PasswordService(LineplatformProperties lineplatoformProperties) {
		this.lineplatoformProperties = lineplatoformProperties;
	}

	public String generateRandomPassword() {
		// // パスワードに使用する文字セット
		// String characters = lineplatoformProperties.getPasswordBase();

		// // パスワードの長さ
		// int length = 12;

		// SecureRandom secureRandom = new SecureRandom();

		// StringBuilder password = new StringBuilder(length);
		// PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		// for (int i = 0; i < length; i++) {
		//     int randomIndex = secureRandom.nextInt(characters.length());
		//     char randomChar = characters.charAt(randomIndex);
		//     password.append(randomChar);
		// }

		// // パスワードをハッシュ化
		// return passwordEncoder.encode(password.toString());
		return "testPass";
	}
}
