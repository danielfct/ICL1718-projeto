package util;
import java.util.*;

public class Environment<T> implements IEnvironment<T> {

	static class Assoc<T> {
		String id;
		T value;

		Assoc(String id, T value) {
			this.id = id;
			this.value = value;
		}
	}

	Environment<T> up;
	ArrayList<Assoc<T>> assocs;

	public Environment() {
		this(null);
	}

	private Environment(Environment<T> up) {
		this.up = up;
		this.assocs = new ArrayList<Assoc<T>>();
	}

	public T find(String id) throws UndeclaredIdentifierException {
		Environment<T> current = this;
		while (current != null) {
			for (Assoc<T> assoc : current.assocs)
				if (assoc.id.equals(id))
					return assoc.value;
			current = current.up;
		}
		throw new UndeclaredIdentifierException(id);
	}

	public Environment<T> beginScope() {
		return new Environment<T>(this);
	}

	public Environment<T> endScope() {
		return up;
	}

	public void assoc(String id, T value) throws DuplicateIdentifierException {
		for (Assoc<T> assoc : assocs)
			if (assoc.id.equals(id))
				throw new DuplicateIdentifierException(id);
		assocs.add(new Assoc<T>(id,value));

	}
}