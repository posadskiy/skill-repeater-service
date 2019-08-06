package dev.posadskiy.skillrepeat.service;


import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoService {
	public static String createNewPassword(String nakedPassword) {
		String secret = "$!@#$%$#@";

		Mac sha256_HMAC = null;
		try {
			sha256_HMAC = Mac.getInstance("HmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		try {
			sha256_HMAC.init(secret_key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		return Hex.encodeHexString(sha256_HMAC.doFinal(nakedPassword.getBytes(StandardCharsets.UTF_8)));
	}
}
