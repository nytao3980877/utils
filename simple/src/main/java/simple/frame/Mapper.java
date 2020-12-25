package simple.frame;

public class Mapper {

	private String selector;
	private String value;
	private String name;
	private String type;

	public Mapper(String selector, String value) {
		super();
		this.selector = selector;
		this.value = value;
	}
	
	public Mapper(String selector, String value, String name, String type) {
		super();
		this.selector = selector;
		this.value = value;
		this.name = name;
		this.type = type;
	}

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
