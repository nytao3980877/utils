package simple.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static List<Map<String, String>> wash(List<Map<String, String>> list){
		HashSet<Map<String, String>> h=new HashSet<>(list);
		list.clear();
		list.addAll(h);
		return list;
	}
	
	public static String toCN(String str) {
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa50-9]");
		Matcher m = p.matcher(str);
		StringBuilder s = new StringBuilder();
		while (m.find()) {
			s.append(m.group());
		}
		if(s.length()<1)return str;
		return s.toString();
	}
	
	public static String getCN(String str) {
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
		Matcher m = p.matcher(str);
		StringBuilder s = new StringBuilder();
		while (m.find()) {
			s.append(m.group());
		}
		if(s.length()<1)return str;
		return s.toString();
	}
	
	public static String encodeUTF8(String words) {
		String enc="utf-8";
		try {
			words=URLEncoder.encode(words, enc);
		} catch (UnsupportedEncodingException e) {
			System.out.println("该编码格式不支持："+enc);
		}
		return words;
	}
	
	public static String encodeGBK(String words) {
		String enc="utf-8";
		try {
			words=URLEncoder.encode(words, enc);
		} catch (UnsupportedEncodingException e) {
			System.out.println("该编码格式不支持："+enc);
		}
		return words;
	}
	/**
	 * -获取指定目录的所以限定文件
	 * @param dirPathOfFile 文件所在目录
	 * @param flieName 文件全（部分开头）名
	 * @return 文件数组
	 */
	public static File[] getFiles(String dirPathOfFile,String flieName) {
		File file = new File(dirPathOfFile);
		if(!file.isDirectory()) {
			System.err.println(dirPathOfFile+"------不是目录或文件夹!");
			return null;
		}
		File[] files = file.listFiles((dir,name)->name.startsWith(flieName));
		return files;
	}
	/**
	 * -截取冒号后面的字符串
	 * @return 
	 */
	public static String subColonString(String str) {
		if(str==null) return str;
		int index = str.indexOf(":")>=0 ? str.indexOf(":") : str.indexOf("：");
		return index>=0? str.substring(index+1).trim() : str;
	}
	
	/**
	 * -截取冒号前面的字符串
	 * @return 
	 */
	public static String preColonString(String str) {
		if(str==null) return str;
		int index = str.indexOf(":")>=0 ? str.indexOf(":") : str.indexOf("：");
		return index>=0? str.substring(0,index).trim() : str;
	}
	
	public static boolean contains(String string,String str) {
		if(string==null||str==null) return false;
		String[] arr = str.split("");
		str = "[\\s\\S]*";
		for (int i = 0; i < arr.length; i++) {
			str += i<arr.length-1? arr[i]+"\\s*" : arr[i]+"[\\s\\S]*";
		}
		return string.matches(str);
	}
}
