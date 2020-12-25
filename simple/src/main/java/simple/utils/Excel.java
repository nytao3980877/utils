package simple.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.spire.xls.CellRange;
import com.spire.xls.ExcelPicture;
import com.spire.xls.HorizontalAlignType;
import com.spire.xls.HyperLink;
import com.spire.xls.VerticalAlignType;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.core.IStyle;

import net.coobird.thumbnailator.Thumbnails;
/**
 * Excel文件操作
 *
 */
public class Excel {
	
	/**
	 * 生成Excel文件
	 * @param map 数据类型与值映射
	 * @param fileName 文件名
	 */
	public static void write(Map<String, ?> map,String fileName) {
		//将文件名转为路径：D:\fileName.xlsx
		fileName=createPath(fileName);
		File file=new File(fileName);
		
		Workbook wb=new Workbook();
		Worksheet sheet = wb.getWorksheets().get(0);
		int row=0;
		int column=1;
		
		if(file.exists()) {
			wb.loadFromFile(fileName);
			sheet = wb.getWorksheets().get(0);
			row=sheet.getLastRow();
			column=sheet.getLastColumn();
		}
		
		if(row>=1048576||column>=16384) {
			System.err.println("列或行已达上限！");
			return;
		}
		
		boolean has;
		for (String key : map.keySet()) {
			if(row==0) {
				setStyle(sheet, 1, column);
				CellRange cell=sheet.getCellRange(1, column);
				cell.setValue(key);
				setValue(sheet, 2, column, map.get(key).toString());
				column++;
			}else {
				has=false;
				for (int i = 1; i <=column; i++) {
					String v=sheet.getCellRange(1, i).getValue();
					if(v.equals(key)) {
						setValue(sheet, row+1, i, map.get(key).toString());
						has=true;
						break;
					}
				}
				//不存在同类型，将该数据的类型追加写入列，数据值追加写入行
				if(!has) {
					setStyle(sheet, 1, column+1);
					setStyle(sheet, row+1, column+1);
					sheet.getCellRange(1, column+1).setValue(key);
					sheet.getCellRange(row+1, column+1).setValue(map.get(key).toString());
					column++;
				}
			}
		}
		
		
		//保存
		wb.saveToFile(fileName);
		System.out.println("已保存到——"+fileName);
	}
	
	/**
	 * 生成固定类型数据的Excel
	 * @param list 数据
	 * @param fileName 文件名
	 * @return 
	 */
	public static String write(List<Map<String,String>> list,String fileName) {
		fileName=createPath(fileName);
		File file=new File(fileName);
		Workbook wb=new Workbook();
		int row=0;
		int column=1;
		Worksheet sheet=wb.getWorksheets().get(0);
		//转为二维数组
		String[][] arr=toArray(list);
		
		if(file.exists()) {
			wb.loadFromFile(fileName);
			sheet=wb.getWorksheets().get(0);
			row=sheet.getLastRow();
			//去掉类型名
			arr=Arrays.copyOfRange(arr, 1,arr.length);
		}
		//插入二维数组数据
		sheet.insertArray(arr, row+1,column);
		//保存
		wb.saveToFile(fileName);
		System.out.println("已保存到——"+fileName);
		return fileName;
	}
	
	
	public static void writeMapInList(List<Map<String,String>> list,String fileName) {
		//将文件名转为路径：D:\fileName.xlsx
				fileName=createPath(fileName);
				File file=new File(fileName);
				
				Workbook wb=new Workbook();
				Worksheet sheet = wb.getWorksheets().get(0);
				int row=0;
				int column=1;
				
				if(file.exists()) {
					wb.loadFromFile(fileName);
					sheet = wb.getWorksheets().get(0);
					row=sheet.getLastRow();
					column=sheet.getLastColumn();
				}
				
				if(row>=1048576||column>=16384) {
					System.err.println("列或行已达上限！");
					return;
				}
				for (Map<String, String> map : list) {
					boolean has;
					for (String key : map.keySet()) {
						if(row==0) {
							setStyle(sheet, 1, column);
							CellRange cell=sheet.getCellRange(1, column);
							cell.setValue(key);
							setValue(sheet, 2, column, map.get(key).toString());
							column++;
						}else {
							has=false;
							for (int i = 1; i <=column; i++) {
								String v=sheet.getCellRange(1, i).getValue();
								if(v.contains(key)) {
									setValue(sheet, row+1, i, map.get(key).toString());
									has=true;
									break;
								}
							}
							//不存在同类型，将该数据的类型追加写入列，数据值追加写入行
							if(!has) {
								setStyle(sheet, 1, column+1);
								setStyle(sheet, row+1, column+1);
								sheet.getCellRange(1, column+1).setValue(key);
								sheet.getCellRange(row+1, column+1).setValue(map.get(key).toString());
								column++;
							}
						}
					}
					row++;
					column--;
				}
				
				//保存
				wb.saveToFile(fileName);
				System.out.println("已保存到——"+fileName);
	}
	
	public static void writeList(List<String> list,String fileName) {
		fileName=createPath(fileName);
		File file=new File(fileName);
		Workbook wb=new Workbook();
		int row=0;
		int column=1;
		Worksheet sheet=wb.getWorksheets().get(0);
		if(file.exists()) {
			wb.loadFromFile(fileName);
			sheet=wb.getWorksheets().get(0);
			row=sheet.getLastRow();
		}
		//插入二维数组数据
		sheet.insertArray(list.toArray(new String[list.size()]), row+1,column,true);
		//保存
		wb.saveToFile(fileName);
		System.out.println("已保存到——"+fileName);
	}
	public static void writeMap(Map<String, String> map,String fileName) {
		fileName=createPath(fileName);
		File file=new File(fileName);
		Workbook wb=new Workbook();
		int row=0;
		int column=1;
		Worksheet sheet=wb.getWorksheets().get(0);
		if(file.exists()) {
			wb.loadFromFile(fileName);
			sheet=wb.getWorksheets().get(0);
			row=sheet.getLastRow();
		}
		//插入二维数组数据
		String[][] arr = toArray(map);
		sheet.insertArray(arr, row+1,column);
		//保存
		wb.saveToFile(fileName);
		System.out.println("已保存到——"+fileName);
	}
	
	
	/**
	 * 向单元格插入数据（若有网图片，则插入图片并添加超链接）
	 * @param sheet 单元格所在工作表
	 * @param row 单元格所在行
	 * @param column 单元格所在列
	 * @param value 插入的数据
	 */
	private static void setValue(Worksheet sheet,int row,int column,String value) {
		if(value.contains(".jpg")||value.contains(".png")) {
			HyperLink url=sheet.getHyperLinks().add(sheet.getCellRange(row, column));
			url.setAddress(value);
			if(value.trim().startsWith("http")) {
				setUrlImg(sheet, row, column, value);
			}else {
				setPathImg(sheet, row, column, value);
			}
		}else if(value.startsWith("http")){
			//hyperlink将图片之外的连接设为excel的超链接
			HyperLink url=sheet.getHyperLinks().add(sheet.getCellRange(row, column));
			url.setAddress(value);
		}else {
			if(value.trim().matches("[\\d]*"))value = "'"+value.trim();
			//插入图片之外的数据
			sheet.getCellRange(row, column).setValue(value);
		}
		//设置格式
		setStyle(sheet, row, column);
	}
	
	/**
	 * 设置单元格格式
	 * @param sheet 单元格所在工作表 
	 * @param row 单元格所在行
	 * @param column 单元格所在列
	 */
	private static void setStyle(Worksheet sheet,int row,int column) {
		//格式实例
		IStyle style = sheet.getCellRange(row, column).getCellStyle();
		//水平对齐
		style.setHorizontalAlignment(HorizontalAlignType.Center);
		//垂直对齐
		style.setVerticalAlignment(VerticalAlignType.Center);
		//自动换行
		style.setWrapText(true);
	}
	
	/**
	 * 生成文件路径
	 * @param fileName 文件名
	 * @return D:\fileName.xlsx
	 */
	private static String createPath(String fileName) {
		//将文件名转为路径：D:\fileName.xlsx
		if(fileName.endsWith(".xls")) {
			fileName=fileName.substring(0,fileName.lastIndexOf("."))+".xlsx";
		}else if(!fileName.endsWith(".xlsx")) {
			fileName=fileName+".xlsx";
		}
		fileName ="D:\\" + fileName;
		return fileName;
	}
	
	private static String[][] toArray(Map<String, String> map){
		if(map.isEmpty())return new String[0][];
		String[][] arr = new String[map.size()][];
		int index=0;
		for (String key : map.keySet()) {
			String[] a = {key,map.get(key)};
			arr[index] = a;
			index++;
		}
		return arr;
	}
	
	private static String[][] toArray(List<Map<String,String>> list){
		if (list.isEmpty())
			return new String[0][];
		int count=list.size();
		int length=list.get(0).size();
		String[][] array=new String[count+1][length];
		for (int i = 0; i < count; i++) {
			int index=0;
			Map<String, String> map=list.get(i);
			for (String key : map.keySet()) {
				if(i==0)array[0][index]=key;
				array[i+1][index]=map.get(key);
				index++;
			}
		}
		return array;
	}
	private static void setUrlImg(Worksheet sheet,int row,int column,String value) {
		setImg(sheet, row, column, value, false);
	}
	private static void setPathImg(Worksheet sheet,int row,int column,String value) {
		setImg(sheet, row, column, value, true);
	}
	private static void setImg(Worksheet sheet,int row,int column,String value,boolean isPath) {
		int colWidth=12;
		int rowHeight = (int)Math.ceil(colWidth*6.33);
		BufferedImage img = null;
		try {
			if(value.endsWith(".webp")) {
				value=value.substring(0,value.indexOf(".webp")-1);
			}
			img=isPath?ImageIO.read(new File(value)):ImageIO.read(new URL(value));
			ImageIcon icon=isPath?new ImageIcon(value):new ImageIcon(new URL(value));
			int scale = img.getHeight()/img.getWidth();
			img=new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
			rowHeight = (int)Math.ceil(colWidth*6.33*scale);
			Graphics2D g=(Graphics2D) img.getGraphics();
			g.drawImage(icon.getImage(), 0, 0, null);
			g.dispose();
			img=Thumbnails.of(img).scale(0.2).asBufferedImage();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("图片读取失败！");
		}
		if(img==null) {
			//将图片地址设为超链接
			HyperLink url=sheet.getHyperLinks().add(sheet.getCellRange(row, column));
			url.setAddress(value);
			return;
		}
		//设置图片单元格列宽
		sheet.setColumnWidth(column, colWidth);
		//行高
		sheet.setRowHeight(row, rowHeight);
		//工作表添加图片到单元格
		ExcelPicture pic = sheet.getPictures().add(row, column, row, column, img);
		//图片设置超链接
		pic.setHyperLink(value.substring(0,value.indexOf(".jpg")+4), true);
	}
	
	/**
	 * 读取xlsx多列数据
	 * @param filePath 文件绝对路径
	 * @param args 指定列名，返回数据按照改参数所给的顺序排序，可以没有（若无，则返回全部数据）
	 * @return 指定的多列数据
	 */
	public static List<Map<String, String>> readInfo(String filePath,String ... args){
		List<Map<String, String>> list=new ArrayList<>();
		Workbook wb=new Workbook();
		wb.loadFromFile(filePath);
		Worksheet sheet=wb.getWorksheets().get(0);
		int row=sheet.getLastRow();
		int col=sheet.getLastColumn();
		Set<Integer> cols=new LinkedHashSet<>();
		for (int i = 1; i <= col; i++) {
			if(args.length==0) {
				cols.add(i);
			}else {
				String s=sheet.getCellRange(1, col).getValue().trim();
				for (String string : args) {
					if(s.equals(string))cols.add(i);
				}
			}
		}
		for (int i = 2; i <= row; i++) {
			Map<String, String> map=new LinkedHashMap<>();
			for (Integer j : cols) {
				String key=sheet.getCellRange(1, j).getValue().trim();
				String value=sheet.getCellRange(i, j).getValue().trim();
				map.put(key, value);
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 读取xlsx单列数据
	 * @param filePath 文件绝对路径
	 * @param str 列名，若为null或空则读取第一列
	 * @return 指定单列的数据（包含列名）
	 */
	public static List<String> readInfo(String filePath,String str){
		List<String> list=new ArrayList<>();
		Workbook wb=new Workbook();
		wb.loadFromFile(filePath);
		Worksheet sheet=wb.getWorksheets().get(0);
		int row=sheet.getLastRow();
		int col=sheet.getLastColumn();
		int c = 0;
		if(null==str||str.equals("")) {
			c=1;
		}else {
			for (int i = 1; i <= col; i++) {
				String s=sheet.getCellRange(1, i).getValue();
				if(s.equals(str)) {
					c=i;
					break;
				}
			}
		}
		int r=null==str||str.equals("")?1:2;
		for (int i = r; i <= row; i++) {
			list.add(sheet.getCellRange(i, c).getValue());
		}
		return list;
	}
	
	/**
	 * -把多表数据进行归一
	 * @param pathOfDir 所在目录
	 * @param startOfName 文件起始名称（统一后缀为xlsx）
	 * @param newFileName 归一文件名
	 */
	public static void merge(String pathOfDir,String startOfName,String newFileName) {
		File file = new File(pathOfDir);
		if(!file.isDirectory()) {
			System.err.println(pathOfDir+"------不是目录或文件夹!");
			return;
		}
		File[] files = file.listFiles((dir,name)->name.endsWith(".xlsx")&&name.startsWith(startOfName));
		List<Map<String, String>> list = new ArrayList<>();
		for (File f : files) {
			String filePath = pathOfDir+f.getName();
			List<Map<String, String>> list1 = Excel.readInfo(filePath);
			System.out.println(list1.size());
			list.addAll(list1);
		}
		list = Utils.wash(list);
		for (Map<String, String> map : list) {
			write(map, newFileName);
		}
		System.out.println("完成！");
	}
	
	public static void modifyNum(String filePath,int division,String...args) {
		if(args.length<1) {
			System.err.println("请输入修改数据的列序！");
		}
		if(division==0) {
			System.err.println("除数不能为0！");
		}
		Workbook wb=new Workbook();
		wb.loadFromFile(filePath);
		Worksheet sheet=wb.getWorksheets().get(0);
		int row=sheet.getLastRow();
		int col = sheet.getLastColumn();
		LinkedHashSet<Integer> cols = new LinkedHashSet<>();
		for (int i = 1; i <= col; i++) {
			String value = sheet.getCellRange(1, i).getValue().trim();
			for (String str : args) {
				if(str.equals(value))cols.add(i);
			}
		}
		if(!cols.isEmpty()) {
			for (Integer c : cols) {
				for (int i = 2; i <= row; i++) {
					String value = sheet.getCellRange(i, c).getValue().trim();
					if(value==null||value.equals(""))continue;
					System.out.println(value);
					String r = Utils.getCN(value);
					System.out.println(r);
					double d = 0;
					if(r.matches("[\\d.]*")) {
						d = Double.parseDouble(r)/division;
					}else {
						d = Double.parseDouble(value.substring(0, value.indexOf(r)));
						switch (r) {
						case "万":
							d = d*10000/division;
							break;
						default:
							break;
						}
					}
					value = String.valueOf(d);
					sheet.getCellRange(i, c).setValue(value);
				}
			}
		}
		wb.saveToFile(filePath);
		System.out.println("修改完毕！");
	}
	
	
	public static void modifyHypeLinkes(String filePath,String cloName,String oldPart,String newPart) {
		Workbook wb=new Workbook();
		wb.loadFromFile(filePath);
		Worksheet sheet=wb.getWorksheets().get(0);
		int row=sheet.getLastRow();
		int col = sheet.getLastColumn();
		int c = 0;
		for (int i = 1; i <= col; i++) {
			String value = sheet.getCellRange(1, i).getValue();
			if(value.trim().equals(cloName))c=i;
		}
		if(c!=0) {
			for (int i = 2; i <= row; i++) {
				String value = sheet.getCellRange(i, c).getValue();
				value = value.replace(oldPart, newPart);
				setValue(sheet, i, c, value);
			}
		}
		wb.saveToFile(filePath);
		System.out.println("修改完毕！");
	}
	
	/**
	 *- 重置所有内容格式
	 * @param filePath 文件路径
	 */
	public static void resetFormat(String filePath) {
		Workbook wb=new Workbook();
		wb.loadFromFile(filePath);
		Worksheet sheet=wb.getWorksheets().get(0);
		int row = sheet.getLastRow();
		int col = sheet.getLastColumn();
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= col; j++) {
				String value = sheet.getCellRange(i, j).getValue();
				setValue(sheet, i, j, value);
			}
		}
		wb.saveToFile(filePath);
		System.out.println("修改完毕！");
	}
	
}








