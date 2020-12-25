package simple.frame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import simple.utils.AppUtils;
import simple.utils.Excel;

public class Module implements AppUtils{
	public static final String FILE = "src/main/resources/config/";
	private WebDriver driver;
	private ConfigEntity cf;
	private String fileName;
	
	public Module(WebDriver driver,String configFile) {
		this.driver = driver;
		cf = new ConfigEntity(FILE+configFile);
		this.fileName = cf.getFileName();
	}
	 
	public Module(WebDriver driver,String configFile,String fileName) {
		this.driver = driver;
		cf = new ConfigEntity(configFile);
		this.fileName = cf.getFileName()+"-"+fileName;
	}
	
	public void crawl() {
		try {
			openUrl();
			List<Map<String, String>> list = all();
			int size = list.size();
			System.out.println("-----共"+size+"条------------");
			fileName = Excel.write(list, fileName);
			Excel.resetFormat(fileName);
			System.out.println("完成！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			quit();
		}
	}
	
	private void openUrl() {
		driver.get(cf.getUrl());
		sleep(1500);
	}
	
	private List<Map<String, String>> all() {
		List<Map<String,String >> list =new ArrayList<>();
		while (true) {
			List<WebElement> lis = els(cf.getLis());
			if(lis!=null) {
				for (WebElement el : lis) {
					Map<String, String> map = one(el);
					if(map.isEmpty())continue;
					list.add(map);
				}
			}
			WebElement next = el(cf.getNext());
			if(next==null)break;
			new Actions(driver).moveToElement(next).click().perform();
			sleep(1500);
		}
		return list;
	}
	
	private Map<String, String> one(WebElement el) {
		Map<String , String> map = new LinkedHashMap<String, String>();
		List<Mapper> lis = cf.getLi();
		if(!lis.isEmpty()) {
			for (Mapper mapper : lis) {
				put(map, mapper,el);
			}
		}
		Mapper link = cf.getLink();
		if(link!=null) {
			WebElement linkel = el(el,link);
			new Actions(driver).moveToElement(linkel).click().perform();
			String hw = driver.getWindowHandle();
			Set<String> hws = driver.getWindowHandles();
			for (String h : hws) {
				if(!h.equals(hw)) {
					driver.switchTo().window(h);
					sleep(3000);
					List<Mapper> linkLis = cf.getLinkLi();
					if(linkLis!=null&&!linkLis.isEmpty()) {
						for (Mapper mapper : linkLis) {
							put(map, mapper,null);
						}
					}
					
					driver.close();
					driver.switchTo().window(hw);
				}
			}
		}
		System.out.println(map);
		return map;
	}
	
	public ConfigEntity getCf() {
		return cf;
	}

	private By by(Mapper mapper) {
		String selector = mapper.getSelector();
		String value = mapper.getValue();
		switch (selector) {
		case "tagName":
			return By.tagName(value);
		case "className":
			return By.className(value);	
		case "cssSelector":
			return By.cssSelector(value);	
		case "xpath":
			return By.xpath(value);	
		case "id":
			return By.id(value);
		case "linkText":
			return By.linkText(value);
		case "name":
			return By.name(value);
		case "partialLinkText":
			return By.partialLinkText(value);
		}
		return null;
	}
	private WebElement el(Mapper mapper) {
		return findElement(by(mapper));
	}
	
	private WebElement el(WebElement el,Mapper mapper) {
		return findElement(el,by(mapper));
	}
	private List<WebElement> els(Mapper mapper) {
		return findElements(by(mapper));
	}

	private void put(Map<String, String> map,Mapper mapper,WebElement e) {
		WebElement el = e==null? el(mapper) : el(e,mapper);
		String name = mapper.getName();
		String value = "";
		if(el!=null) {
			String type = mapper.getType();
			if(type.equals("text")) {
				value = el.getText().trim();
				int index = value.indexOf(":")>=0? value.indexOf(":") : value.indexOf("：");
				if(index==0) {
					value = value.substring(1);
				}else if(index>=1&&index==value.length()-1) {
					value = value.substring(0, index);
				}
			}else {
				value = el.getAttribute(type).trim();
				if(value.startsWith("//"))value = "http:"+value;
			}
		}
		map.put(name, value);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = cf.getFileName()+"-"+fileName;
	}

	
	@Override
	public WebDriver getDriver() {
		return driver;
	}

}
