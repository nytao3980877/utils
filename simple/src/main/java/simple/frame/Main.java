package simple.frame;


import java.io.IOException;

import simple.utils.Driver;


public class Main {

	public static void main(String[] args) {
		String FILE = "config.xml";
		Module m = new Module(Driver.buildChromeDriver(9999),FILE,"女装");
		try {
			m.crawl();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
