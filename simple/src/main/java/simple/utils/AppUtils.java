package simple.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface AppUtils {
	
	
	WebDriver getDriver();

	/**
	 * utf-8格式处理路径中文搜索词
	 * @param words 搜索词
	 * @return utf-8格式的url搜索词
	 */
	default String encode(String words) {
		String enc="utf-8";
		try {
			words=URLEncoder.encode(words, enc);
		} catch (UnsupportedEncodingException e) {
			System.out.println("该编码格式不支持："+enc);
		}
		return words;
	}
	
	/**
	 * 右侧滚动条向下滚动
	 */
	default void scroll() {
		JavascriptExecutor js=(JavascriptExecutor)this.getDriver();
		long height=(long) js.executeScript("return document.body.scrollHeight");
		int[] h=getScroll((int) height);
		for (int i : h) {
			js.executeScript("scroll(0,"+i+")");
			sleep(10);
		}
		sleep(100);
	}
	
	default void scroll(WebElement el) {
		JavascriptExecutor js=(JavascriptExecutor)this.getDriver();
		while(true) {
			long height=(long) js.executeScript("return arguments[0].scrollHeight",el);
			js.executeScript("arguments[0].scrollIntoView(false)", el);
			sleep(1000);
			long end=(long) js.executeScript("return arguments[0].scrollHeight",el);
			if(end==height)break;
		}
	}
	/**
	 * 滚动条滚动轨迹
	 * @param distance
	 * @return
	 */
	default int[] getScroll(int distance) {
		int[] track=new int[0];
		double current=0;
		double mid=distance*3/4;
		double t=0.2;
		double v=1;
		double a=0;
		while(current<distance) {
			a+=current<mid?2:-3;
			double v0=v;
			v=v0+a*t;
			double move=v0*t+1/2*a*t*t;
			current+=move;
			track=Arrays.copyOf(track, track.length+1);
			track[track.length-1]=(int) Math.round(current);
		}
		return track;
	}
	
	default void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			System.err.println("程序停顿时出现异常！");
		}
	}
	
	default WebElement findElement(final By by) {
		try {
			WebDriverWait wait=new WebDriverWait(this.getDriver(), Duration.ofMillis(300));
			WebElement element =wait.until((arg0)->{return arg0.findElement(by);});
			return element;
		} catch (Exception e) {
			return null;
		}
	}
	default WebElement findE(By by) {
		while(true) {
			WebElement el=findElement(by);
			if(el!=null)return el;
		}
	}
	default WebElement findE(final By by,long time) {
		long start=System.currentTimeMillis();
		while(true) {
			WebElement el=findElement(by);
			if(el!=null)return el;
			if(System.currentTimeMillis()-start>=time)return null;
		}
	}
	
	default WebElement findElement(WebElement element,final By by) {
		try {
			return element.findElement(by);
		} catch (Exception e) {
			return null;
		}
	}
	
	default List<WebElement> findElements(WebElement element,final By by) {
		try {
			return element.findElements(by);
		} catch (Exception e) {
			return null;
		}
	}
	default List<WebElement> findElements(final By by) {
		try {
			WebDriverWait wait=new WebDriverWait(this.getDriver(), Duration.ofMillis(1000));
			List<WebElement> element=wait.until((arg0)->{return arg0.findElements(by);});
			return element;
		} catch (Exception e) {
			return null;
		}
	}
	
	default void quit() {
		if(this.getDriver()!=null)this.getDriver().quit();
	}
	
	default void refresh() {
		try {
			getDriver().navigate().refresh();
		} catch (Exception e) {
		}
	}
	
	default List<Map<String, String>> wash(List<Map<String, String>> list){
		HashSet<Map<String, String>> h=new HashSet<>(list);
		list.clear();
		list.addAll(h);
		return list;
	}
}
