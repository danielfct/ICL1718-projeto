package environment;

import java.util.*;

public class Environment<T> implements IEnvironment<T> {

	static class Assoc<T> {
		String id;
		T value;

		Assoc(String id, T value) {
			this.id = id;
			this.value = value;
		}

		@Override
		public String toString() {
			return id + "=" + value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, value);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Assoc<?>))
				return false;
			Assoc<?> other = (Assoc<?>) obj;
			return Objects.equals(id, other.id) &&
					Objects.equals(value, other.value);
		}

	}

	Environment<T> up;
	List<Assoc<T>> assocs;

	public Environment() {
		this(null);
	}

	private Environment(Environment<T> up) {
		this.up = up;
		this.assocs = new ArrayList<Assoc<T>>();
	}

	@Override
	public Environment<T> beginScope() {
		return new Environment<T>(this);
	}

	@Override
	public Environment<T> endScope() {
		return up;
	}

	@Override
	public void assoc(String id, T value) throws DuplicateIdentifierException {
		for (Assoc<T> assoc : assocs)
			if (assoc.id.equals(id))
				throw new DuplicateIdentifierException(id);
		assocs.add(new Assoc<T>(id, value));
	}

	@Override
	public IEnvironment<T> update(String id, T value) throws UndeclaredIdentifierException {
		for (Assoc<T> assoc : assocs)
			if (assoc.id.equals(id)) {
				assoc.value = value;
				return this; 
			}
		throw new UndeclaredIdentifierException(id);
	}

	@Override
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

	@Override
	public String toString() {
		return (up != null ? up.toString() + "," : "") + assocs.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(up, assocs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Environment<?>))
			return false;
		Environment<?> other = (Environment<?>) obj;
		return Objects.equals(up, other.up) &&
				Objects.equals(assocs, other.assocs);
	}

}