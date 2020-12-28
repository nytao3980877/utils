package simple.frame;
/**
 *配置映射文件 
 *
 */
public class Mapper {

	//选择器
	private String selector;
	//选择器参数
	private String value;
	//自定义数据名称
	private String name;
	//元素的属性类型
	private String type;

	//元素定位构造方法
	public Mapper(String selector, String value) {
		super();
		this.selector = selector;
		this.value = value;
	}
	//数据定位及信息构造方法
	public Mapper(String selector, String value, String name, String type) {
		super();
		this.selector = selector;
		this.value = value;
		this.name = name;
		this.type = type;
	}

	//之后是get、set、toString方法
	
	public  String getSelector() {
		return selector;
	}
	public  void setSelector(String selector) {
		this.selector = selector;
	}
	public  String getValue() {
		return value;
	}
	public  void setValue(String value) {
		this.value = value;
	}
	public  String getName() {
		return name;
	}
	public  void setName(String name) {
		this.name = name;
	}
	public  String getType() {
		return type;
	}
	public  void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Mapper [selector=" + selector + ", value=" + value + ", name=" + name + ", type=" + type + "]";
	}
	
}
