package values;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListValue implements IValue {
	
	public final IValue head;
	public final List<IValue> tail;
	
	public ListValue(IValue head, List<IValue> tail) {
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public String toString() {
		return "[" + head + (!tail.isEmpty() ? ", " : "") + String.join(", ", tail.stream().map(IValue::toString).collect(Collectors.toList())) + "]";
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
		if (!(obj instanceof ListValue))
			return false;
		ListValue other = (ListValue) obj;
		return Objects.equals(head, other.head) && Objects.equals(tail, other.tail);
	}

}
