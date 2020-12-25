package simple.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Driver {
	
	public static WebDriver buildChromeDriver() {
		return init();
	}
	
	public static WebDriver buildChromeDriver(int port) {
		return init(port);
	}
	
	private static WebDriver init() {
		ChromeOptions options=new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);//网页加载模式：eager：加载dom树之后，开始下一步
		options.addArguments("-disable-infobars");//禁用浏览器正在被自动化程序控制的提示
		options.addArguments("-disable-plugins");//禁止加载所有插件
		options.addArguments("-incognito");//启动无痕/隐私模式
		options.addArguments("-no-sandbox");//取消沙盒模式
		options.addArguments("-start-maximized");//最大化运行（全屏窗口）
		options.addArguments("-disable-gpu");
		String[] a= {"enable-automation"};//启用自动化
		options.setExperimentalOption("excludeSwitches", a);//开发者模式
		options.setExperimentalOption("useAutomationExtension", false);//自动化扩展
		Map<String, Object> map=new HashMap<>();
		//map.put("profile.managed_default_content_settings.images", 2);//不加载图片
		map.put("credentials_enable_service", false);//关闭保存密码弹窗
		map.put("profile.password_manager_enabled", false);//关闭保存密码弹窗
		options.setExperimentalOption("prefs", map);
		String key="webdriver.chrome.driver";
		String value="D:\\selenium\\chromedriver.exe";
		System.setProperty(key, value);//系统自动配置相应的参数
		WebDriver driver=new ChromeDriver(options);
		//driver.manage().timeouts().implicitlyWait(20000, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		return driver;
	}
	
	private static WebDriver init(int port) {
		ChromeOptions options=new ChromeOptions();
		options.setExperimentalOption("debuggerAddress", "127.0.0.1:"+port);
		WebDriver driver=new ChromeDriver(options);
		String hw = driver.getWindowHandle();
		Set<String> hws = driver.getWindowHandles();
		if(hws.size()>1) {
			for (String str : hws) {
				if(!str.equals(hw)) {
					driver.switchTo().window(str);
					driver.close();
					driver.switchTo().window(hw);
				}
			}
		}
		return driver;
	}
}
