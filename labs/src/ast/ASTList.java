package ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import compiler.ICodeBuilder;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.BoolType;
import types.IType;
import types.IntType;
import types.ListType;
import types.TypingException;
import values.IValue;
import values.ListValue;
import values.TypeMismatchException;

public class ASTList extends ASTNode {

	public final IASTNode head;
	public final List<IASTNode> tail;
	private IType type;

	public ASTList(IASTNode head, List<IASTNode> tail) {
		this.head = head;
		this.tail = tail;
		this.type = null;
	}
	
	public int size() {
		return head != null ? 1 + tail.size() : 0;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue headValue = null;
		List<IValue> tailValue = new LinkedList<IValue>();
		if (head != null) {
			headValue = head.eval(env);
			for (IASTNode n : tail) {
				IValue value = n.eval(env);
				if (headValue.getClass() == value.getClass())
					tailValue.add(value);
				else
					throw new TypeMismatchException("Types on list do not match.");
			}
		}
		return new ListValue(headValue, tailValue);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType headType = null;
		List<IType> tailType = new LinkedList<IType>();
		if (head != null) {
			headType = head.typecheck(env);
			for (IASTNode n : tail) {
				IType type = n.typecheck(env);
				if (headType.equals(type))
					tailType.add(type);
				else
					throw new TypingException("Types on list do not match.");
			}
		}
		type = new ListType(headType, tailType);

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// Initialize a new array
		code.emit_push(size());
		if (size() == 0)
			code.emit_newarray(IntType.singleton);
		else {
			// Initialize array positions
			IType t = ((ListType)type).head;
			if (t == IntType.singleton || t == BoolType.singleton)
				code.emit_newarray(t);
			else 
				code.emit_anewarray(t);
			code.emit_dup();
			code.emit_ldc(0);
			head.compile(code, env);
			if (t == IntType.singleton)
				code.emit_iastore();
			else if (t == BoolType.singleton)
				code.emit_bastore();
			else
				code.emit_aastore();	
			int index = 1;
			for (IASTNode n : tail) {
				code.emit_dup();
				code.emit_ldc(index++);
				n.compile(code, env);
				if (t == IntType.singleton)					
					code.emit_iastore();
				else if (t == BoolType.singleton)
					code.emit_bastore();
				else
					code.emit_aastore();
			}
		}
	}

	@Override
	public IType getType() {
		return type;
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
		if (!(obj instanceof ASTList))
			return false;
		ASTList other = (ASTList) obj;
		return Objects.equals(head, other.head) && Objects.equals(tail, other.tail);
	}

	@Override
	public String toString() {
		return head + ", " + tail;
	}

}
