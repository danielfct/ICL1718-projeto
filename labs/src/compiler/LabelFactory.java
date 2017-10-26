package compiler;

public class LabelFactory {
	
	static int counter = 0;
	
	public String getLabel() {
		return "label"+(counter++);
	}
	
}