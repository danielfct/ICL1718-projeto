package environment;

import java.util.ArrayList;
import ast.ASTId.Address;

public class CompilationEnvironment extends Environment<Integer> implements ICompilationEnvironment {

	private CompilationEnvironment up;

	public CompilationEnvironment() {
		this(null);
	}

	public CompilationEnvironment(CompilationEnvironment up) {
		this.up = up;
		this.assocs = new ArrayList<Assoc<Integer>>();
	}

	@Override
	public CompilationEnvironment beginScope() {
		return new CompilationEnvironment(this);
	}

	@Override
	public CompilationEnvironment endScope() {
		return up;
	}

	@Override
	public Address lookup(String id) throws UndeclaredIdentifierException {
		int jumps = 0;
		CompilationEnvironment current = this;
		while (current != null) {
			for (Assoc<Integer> assoc : current.assocs)
				if (assoc.id.equals(id))
					return new Address(jumps, assoc.value);
			current = current.up;
			jumps++;
		}
		throw new UndeclaredIdentifierException(id);
	}

}
