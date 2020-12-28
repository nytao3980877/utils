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
	//配置文件路径
	public static final String FILE = "src/main/resources/config/";
	//浏览器驱动
	private WebDriver driver;
	//配置文件解析
	private ConfigEntity cf;
	//生成后的文件名
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
	
	/**
	 * 主接口
	 */
	public void crawl() {
		try {
			//打开数据所在页面
			openUrl();
			//获取全部数据
			List<Map<String, String>> list = all();
			int size = list.size();
			System.out.println("-----共"+size+"条------------");
			//生成Excel文件
			fileName = Excel.write(list, fileName);
			//更改文件内容格式（设预览图、超链接、自动换行）
			Excel.resetFormat(fileName);
			System.out.println("完成！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//关闭驱动
			quit();
		}
	}
	
	/**
	 * 打开数据所在页面
	 */
	private void openUrl() {
		driver.get(cf.getUrl());
		sleep(1500);
	}
	/**
	 * 获取全部数据
	 * @return
	 */
	private List<Map<String, String>> all() {
		List<Map<String,String >> list =new ArrayList<>();
		while (true) {
			//数据列表集合
			List<WebElement> lis = els(cf.getLis());
			if(lis!=null) {
				for (WebElement el : lis) {
					//获取单条数据
					Map<String, String> map = one(el);
					if(map.isEmpty())continue;
					list.add(map);
				}
			}
			//翻页
			WebElement next = el(cf.getNext());
			if(next==null)break;
			new Actions(driver).moveToElement(next).click().perform();
			sleep(1500);
		}
		return list;
	}
	
	/**
	 * 获取一条数据
	 * @param el 每条数据所在元素
	 * @return
	 */
	private Map<String, String> one(WebElement el) {
		Map<String , String> map = new LinkedHashMap<String, String>();
		//获取所需数据的mappe对象集合
		List<Mapper> lis = cf.getLi();
		if(!lis.isEmpty()) {
			for (Mapper mapper : lis) {
				//设置数据
				put(map, mapper,el);
			}
		}
		//获取新页面数据
		
		//定位连接元素
		Mapper link = cf.getLink();
		if(link!=null) {
			//打开新页面
			WebElement linkel = el(el,link);
			new Actions(driver).moveToElement(linkel).click().perform();
			//获取当前页面句柄
			String hw = driver.getWindowHandle();
			//获取所有页面句柄
			Set<String> hws = driver.getWindowHandles();
			for (String h : hws) {
				if(!h.equals(hw)) {
					//进入新页面（driver初始时已关闭全部页面。正常情况下，此时只有两个页面）
					driver.switchTo().window(h);
					sleep(3000);
					//获取新页面数据mapper集合，设置数据内容
					List<Mapper> linkLis = cf.getLinkLi();
					if(linkLis!=null&&!linkLis.isEmpty()) {
						for (Mapper mapper : linkLis) {
							put(map, mapper,null);
						}
					}
					//关闭新页面
					driver.close();
					//转回数据列表所在页面
					driver.switchTo().window(hw);
				}
			}
		}
		System.out.println(map);
		return map;
	}
	

	/**
	 * 根据mapper对象获取by对象
	 * @param mapper
	 * @return
	 */
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
	
	//以下三个方法是根据mapper对象获取元素
	
	private WebElement el(Mapper mapper) {
		return findElement(by(mapper));
	}
	
	private WebElement el(WebElement el,Mapper mapper) {
		return findElement(el,by(mapper));
	}
	private List<WebElement> els(Mapper mapper) {
		return findElements(by(mapper));
	}
	/**
	 * 向map中添加数据
	 * @param map map对象
	 * @param mapper mapper对象
	 * @param e 定位到的数据元素
	 */
	private void put(Map<String, String> map,Mapper mapper,WebElement e) {
		WebElement el = e==null? el(mapper) : el(e,mapper);
		//获取数据名称，设置map的key
		String name = mapper.getName();
		String value = "";
		if(el!=null) {
			//根据数据所位于元素中的属性类型获取数据
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
		//map对象添加键值队
		map.put(name, value);
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = cf.getFileName()+"-"+fileName;
	}
	public ConfigEntity getCf() {
		return cf;
	}
	
	@Override
	public WebDriver getDriver() {
		return driver;
	}

}
