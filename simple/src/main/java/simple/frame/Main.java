package simple.frame;


import java.io.IOException;

import simple.utils.Driver;


public class Main {

	public static void main(String[] args) {
		String FILE = "config.xml";
		//创建模块Module对象（这里用的是远程debug模式连接已经打开的浏览器，有些需要登录的网站，可以先登录再连接）
		Module m = new Module(Driver.buildChromeDriver(9999),FILE,"女装");
		try {
			//执行爬虫方法
			m.crawl();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//关闭当前程序
			System.exit(0);
		}
	}
	
	public static void run360() {
		Runtime run = Runtime.getRuntime();
		try {
			//run.exec("taskkill /f /fi \"imagename eq 360se.exe\" /t");
			run.exec("D:\\360\\360se6\\Application\\360se.exe --remote-debugging-port=9999");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
