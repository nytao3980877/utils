package simple.frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ConfigEntity {

	private String file ;
	private Element root;
	public ConfigEntity(String file) {
		this.file=file;
		root();
	}
	
	public String getUrl() {
		return root.attributeValue("url");
	}
	
	public String getFileName() {
		return root.attributeValue("fileName");
	}
	
	public Mapper getNext() {
		Element next = root.element("next");
		return getSelectorMapper(next);
	}
	
	public String fileName() {
		return root.element("excel").attributeValue("name");
	}
	
	public Mapper getLis() {
		Element next = root.element("lis");
		return getSelectorMapper(next);
	}
	
	public List<Mapper> getLi(){
		Element lis = root.element("lis");
		return li(lis);
	}

	public Mapper getLink() {
		Element lis = root.element("lis");
		Element link = lis.element("link");
		if(link!=null) {
			return getSelectorMapper(link);
		}
		return null;
	}
	
	public List<Mapper> getLinkLi(){
		Element lis = root.element("lis");
		Element link = lis.element("link");
		if(link!=null) {
			return li(link);
		}
		return null;
	}
	
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
	
	private Mapper getSelectorMapper(Element el) {
		return new Mapper(el.attributeValue("selector"), el.attributeValue("value"));
	}
	
	private Mapper getValueMapper(Element el) {
		return new Mapper(el.attributeValue("selector"), el.attributeValue("value"), el.attributeValue("name"), el.attributeValue("type"));
	}
}
