package types;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListType implements IType {

	public final IType head;
	public final List<IType> tail;
	
	public ListType(IType head, List<IType> tail) {
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public String toJasmin() {
		return "[" + (head == null ? "I" : head.toJasmin());
	}
	
	@Override
	public String toString() {
		return "[" + head + (!tail.isEmpty() ? ", " : "") + String.join(", ", tail.stream().map(IType::toString).collect(Collectors.toList())) + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(head, tail);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ListType))
			return false;
		ListType other = (ListType) obj;
		return Objects.equals(head, other.head) && Objects.equals(tail, other.tail);
	}

}
