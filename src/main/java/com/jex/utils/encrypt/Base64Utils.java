package com.jex.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;


/**
 * BASE64加解密工具类
 *
 *
 */
public class Base64Utils {


	/**
	 * 将字节数组进行Base64加密
	 * @param data
	 * @return
	 */
	public static String encrypt(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	/**
	 * 将字符串进行Base64加密
	 * @param data 字符串
	 * @return 加密后的字符串
	 */
	public static String encrypt(String data) {
		if (data != null && data.length() > 0) {
			// BASE64Encoder encoder = new BASE64Encoder();
			// encoder.encode(data.getBytes());
			return Base64.encodeBase64String(StringUtils.getBytesUtf8(data));
		}
		return null;
	}

	/**
	 * 将数据进行Base64解密
	 * @param data
	 * @return
	 */
	public static byte[] decryptBytes(String data) {
		if (data != null && data.length() > 0) {
			return Base64.decodeBase64(data);
		}
		return null;
	}

	/**
	 * 将数据进行Base64解密
	 * @param data 字符串
	 * @return 解码后的字符串
	 */
	public static String decryptString(String data) {
		if (data != null && data.length() > 0) {
			// BASE64Decoder decoder = new BASE64Decoder();
			// decoder.decodeBuffer(data);
			return StringUtils.newStringUtf8(Base64.decodeBase64(data));
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(Base64Utils.encrypt("jex000"));
		System.out.println(Base64Utils.decryptString("amV4MDAw"));
	}

}