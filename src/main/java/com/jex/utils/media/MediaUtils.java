package com.jex.utils.media;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通过ffmpeg, 获取视频时长, 截图, 转成flv
 * 通过qtfaststart, 将元信息放到最前面
 * 通过yamdi, 在flv中插入关键帧
 */
public class MediaUtils {

	private MediaUtils() {
	}

	/**
	 * 判断是否是支持的视频格式
	 * @param fileName
	 * @return
	 */
	private static boolean isSupportedType(String fileName) {
		if (!StringUtils.isBlank(fileName)) {
			Pattern pattern = Pattern.compile("(asx|asf|mpg|wmv|3gp|mp4|mov|avi|flv){1}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(fileName);
			return matcher.find();
		}
		return false;
	}

	/**
	 * 获取视频时长
	 * @param ffmpegPath
	 * @param filePath
	 * @return
	 */
	public static int getDuration(String ffmpegPath, String filePath) {
		if (!isSupportedType(filePath)) {
			throw new IllegalArgumentException("File type is not supported");
		}

		List<String> command = new ArrayList<String>();
		command.add(ffmpegPath);
		command.add("-i");
		command.add(filePath);
		String result = executeCommand(command);

		String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
		Pattern pattern = Pattern.compile(regexDuration);
		Matcher matcher = pattern.matcher(result);
		if (matcher.find()) {
			// System.out.println("duration=" + matcher.group(1) + ", start=" + matcher.group(2) + ", bitrate=" + matcher.group(3));
			String[] hms = matcher.group(1).split(":");
			return Integer.parseInt(hms[0]) * 60 * 60 + Integer.parseInt(hms[1]) * 60 + (int) Math.floor(Double.parseDouble(hms[2]));
		}
		return 0;
	}

	/**
	 * 截取视频中某一秒的图片
	 * @param ffmpegPath
	 * @param filePath
	 * @param imagePath
	 * @param second
	 */
	public static void screenshot(String ffmpegPath, String filePath, String imagePath, int second) {
		if (!isSupportedType(filePath)) {
			throw new IllegalArgumentException("File type is not supported");
		}

		List<String> command = new ArrayList<String>();
		command.add(ffmpegPath);
		command.add("-i");
		command.add(filePath);
		command.add("-y");
		command.add("-f");
		command.add("image2");
		command.add("-ss");
		command.add("" + second);
		command.add("-t");
		command.add("0.001");
		command.add("-s");
		command.add("320*240");
		command.add(imagePath);
		executeCommand(command);
	}

	/**
	 * 转成flv
	 * @param ffmpegPath
	 * @param srcPath
	 * @param destPath
	 */
	public static void convertFormat(String ffmpegPath, String srcPath, String destPath, String width, String height) {
		if (!isSupportedType(srcPath)) {
			throw new IllegalArgumentException("File type is not supported");
		}

		List<String> command = new ArrayList<String>();
		command.add(ffmpegPath);
		command.add("-i");
		command.add(srcPath);
		command.add("-ab");
		command.add("128");
		command.add("-ar");
		command.add("22050");
		command.add("-qscale");
		command.add("6");
		command.add("-r");
		command.add("25");
		if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
			command.add("-s");
			command.add(width + "*" + height);
		}
		command.add(destPath);
		executeCommand(command);
	}

	/**
	 * 将元信息放到最前面
	 * @param qtfaststartPath
	 * @param srcPath
	 * @param destPath
	 */
	public static void qtfaststart(String qtfaststartPath, String srcPath, String destPath) {
		if (!isSupportedType(srcPath)) {
			throw new IllegalArgumentException("File type is not supported");
		}

		List<String> command = new ArrayList<String>();
		command.add(qtfaststartPath);
		command.add(srcPath);
		command.add(destPath);
		executeCommand(command);
	}

	/**
	 * 在flv中插入关键帧
	 * @param yamdiPath
	 * @param srcPath
	 * @param destPath
	 */
	public static void yamdi(String yamdiPath, String srcPath, String destPath) {
		if (!StringUtils.endsWith(srcPath, "flv")) {
			throw new IllegalArgumentException("File type is not supported");
		}

		List<String> command = new ArrayList<String>();
		command.add(yamdiPath);
		command.add("-i");
		command.add(srcPath);
		command.add("-o");
		command.add(destPath);
		executeCommand(command);
	}

	/**
	 * 执行命令行
	 * @param command
	 * @return
	 */
	private static String executeCommand(List<String> command) {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		Process process = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			process = builder.start();
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str.trim());
			}
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
					br = null;
				}
				if (process != null) {
					process.destroy();
					process = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String ffmpegPath = "F:/ffmpeg.exe";
		String filePath = "F:/20190915222.mp4";
		String qtfaststartPath = "F:/qt-faststart.exe";

		int duration = getDuration(ffmpegPath, filePath);
		System.out.println(duration);
		int i = duration / 4;
		for (int j = i; j < duration; j += i) {
			screenshot(ffmpegPath, filePath, "F:/aaa/test_" + j + ".jpg", j);
		}

		convertFormat(ffmpegPath, filePath, "F:/aaa/test.mp4", "1920", "1080");

		qtfaststart(qtfaststartPath, "F:/aaa/test.mp4", "F:/aaa/output.mp4");
		System.out.println("ok");
	}

}
