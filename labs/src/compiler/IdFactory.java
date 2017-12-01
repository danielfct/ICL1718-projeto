package compiler;

public class IdFactory {

	private int labelCounter;
	private int referenceCounter;
	private int frameCounter;
	private int typeSignatureCounter;
	private int closureCounter;

	private IdFactory() {
		this.init();
	}

	public int label() {
		return labelCounter++;
	}

	public int frame() {
		return frameCounter++;
	}

	public int reference() {
		return referenceCounter++;
	}

	public int typeSignature() {
		return typeSignatureCounter++;
	}

	public int closure() {
		return closureCounter++;
	}

	public void init() {
		this.labelCounter = 0;
		this.referenceCounter = 0;
		this.typeSignatureCounter = 0;
		this.closureCounter = 0;
	}

	public final static IdFactory singleton = new IdFactory();

}
