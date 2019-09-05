package com.jex.utils.lang;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlFilterUtils {

	private HtmlFilterUtils() {
	}

	/**
	 * 根据正则过滤内容

	 * @param html
	 * @param regex
	 * @return
	 */
	private static String filter(String html, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		return sb.toString().trim();
	}

	/**
	 * 过滤html标签
	 * @param html
	 * @return
	 */
	public static String filterHtml(String html) {
		if (StringUtils.isNotBlank(html)) {
			return filter(html, "<([^>]*)>");
		}
		return null;
	}

	/**
	 * 过滤指定html标签
	 * @param html
	 * @param tag
	 * @return
	 */
	public static String filterHtml(String html, String tag) {
		if (StringUtils.isNotBlank(html)) {
			if (StringUtils.isBlank(tag)) {
				return filter(html, "<([^>]*)>");
			} else {
				return filter(html, "<\\s*(/?)\\s*" + tag + "\\s*([^>]*)\\s*>");
			}
		}
		return null;
	}

	public static void main(String[] args) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/test.txt"), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();
			br = null;
			System.out.println(filterHtml(sb.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
