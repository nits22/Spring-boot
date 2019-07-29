package com.example.sample.library;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;

public class UserCoreUtils {

	private static String key = "90Hy23AUfMgCiu9I7XqArF1Bvy0o";
	private static final int MSN_UUV0_MAX = 17;
	private static final int MSN_UUV1_MAX = 27;

	public static void main(String[] args) {

		//System.out.println(generatePasswordToken("nitishbector.it@gmail.com", "nitish"));
	}

	public static String generatePasswordToken(String email, String password) {
		final String secret = "MW_SECRET_KEY";
		String pwdToken = null;
		try {
			String digestToken = calculateRFC2104HMAC(email, secret);

			pwdToken = calculateRFC2104HMAC(password, digestToken);
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return pwdToken;
	}

	public static String calculateRFC2104HMAC(String data, String secretKey) throws SignatureException {
		String result;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
			mac.init(key);
			byte[] authentication = mac.doFinal(data.getBytes());
			result = new String(org.apache.commons.codec.binary.Base64.encodeBase64(authentication));

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return result;
	}

	public static String generateUUID(String email) {
		String uuid = null;
		try {
			if (StringUtils.isNotBlank(email)) {

				uuid = String.valueOf(hmacSha1Enc(key, email, MSN_UUV0_MAX));
			} else {
				System.out.println("Email is blank");
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return uuid;

	}

	private static String hmacSha1Enc(String key, String in, int max) throws Exception {
		if (max > 0) {
			String Base64Data = base64(hmacsha1(key, in));
			String Base64urlencodeData = Base64Data.replace("+", "-").replace("/", "_").replace("=", "");

			if (Base64urlencodeData.length() > max) {
				return Base64urlencodeData.substring(0, max);
			}
			return Base64urlencodeData;
		}

		return "";
	}

	private static byte[] hmacsha1(String key, String in) throws Exception {
		try {
			SecretKeySpec sk = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(sk);
			return mac.doFinal(in.getBytes());
		} catch (Exception e) {
			throw e;
		}
	}

	private static String base64(byte[] hmacsha1_data) throws Exception {
		String Base64EncodeData = new String(Base64.encodeBase64(hmacsha1_data));
		return Base64EncodeData;
	}
	
}
