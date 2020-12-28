package simple.frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 解析配置文件
 *
 */
public class ConfigEntity {

	//配置文件
	private String file ;
	//根元素
	private Element root;
	
	public ConfigEntity(String file) {
		this.file=file;
		root();
	}
	//数据页面路径
	public String getUrl() {
		return root.attributeValue("url");
	}
	//功能名，生成的文件名前缀
	public String getFileName() {
		return root.attributeValue("fileName");
	}
	//翻页元素
	public Mapper getNext() {
		Element next = root.element("next");
		return getSelectorMapper(next);
	}
	//数据列表定位元素
	public Mapper getLis() {
		Element lis = root.element("lis");
		return getSelectorMapper(lis);
	}
	//所需数据集合
	public List<Mapper> getLi(){
		Element lis = root.element("lis");
		return li(lis);
	}

	//新页面数据链接元素
	public Mapper getLink() {
		Element lis = root.element("lis");
		Element link = lis.element("link");
		if(link!=null) {
			return getSelectorMapper(link);
		}
		return null;
	}
	
	//新页面数据集合
	public List<Mapper> getLinkLi(){
		Element lis = root.element("lis");
		Element link = lis.element("link");
		if(link!=null) {
			return li(link);
		}
		return null;
	}
	
	//根元素
	private void root() {
		SAXReader read = new SAXReader();
		Document doc = null;
		try {
			doc = read.read(new File(file));
			root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	//解析数据集合，配置Mapper类型数据
	private List<Mapper> li(Element lis) {
		List<Mapper> list = new ArrayList<>();
		List<?> li = lis.elements("li");
		if(li!=null) {
			for (Object el : li) {
				Element e = (Element)el;
				list.add(getValueMapper(e));
			}
		}
		return list;
	}
	//为定位元素映射Mapper类型
	private Mapper getSelectorMapper(Element el) {
		return new Mapper(el.attributeValue("selector"), el.attributeValue("value"));
	}
	//为数据元素映射Mapper类型
	private Mapper getValueMapper(Element el) {
		return new Mapper(el.attributeValue("selector"), el.attributeValue("value"), el.attributeValue("name"), el.attributeValue("type"));
	}
}
