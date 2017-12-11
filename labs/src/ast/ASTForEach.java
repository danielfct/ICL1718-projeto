package ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import compiler.ICodeBuilder;
import compiler.StackFrame;
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

public class ASTForEach extends ASTNode {

	public final String id;
	public final IASTNode list;
	public final IASTNode exp;
	private IType type;

	public ASTForEach(String id, IASTNode list, IASTNode exp) {
		this.id = id;
		this.list = list;
		this.exp = exp;
		this.type = null;
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue l = force(list.eval(env));

		if (l instanceof ListValue) {
			ListValue values = (ListValue)l;
			IValue mappedHead = null;
			List<IValue> mappedTail = new LinkedList<IValue>();
			IEnvironment<IValue> newEnv = env.beginScope();
			if (values.head != null) {
				newEnv.assoc(id, values.head);
				mappedHead = force(exp.eval(newEnv));
				for (IValue v : values.tail) {
					newEnv.update(id, v);
					mappedTail.add(force(exp.eval(newEnv)));
				}
			}	
			newEnv.endScope();
			return new ListValue(mappedHead, mappedTail);
		}
		else
			throw new TypeMismatchException(l + " is not a list");
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType l = list.typecheck(env);

		if (l instanceof ListType) {
			ListType types = (ListType)l;
			IType mappedHead = null;
			List<IType> mappedTail = new LinkedList<IType>();
			IEnvironment<IType> newEnv = env.beginScope();
			if (types.head != null) {
				newEnv.assoc(id, types.head);
				mappedHead = exp.typecheck(newEnv);
				if (!types.head.equals(mappedHead))
					throw new TypingException("map function returns wrong type. expected " + types.head + ", got " + mappedHead);
				for (IType t : types.tail) {
					newEnv.update(id, t);
					IType mappedT = exp.typecheck(newEnv);
					if (!t.equals(mappedT))
						throw new TypingException("map function returns wrong type. expected " + t + ", got " + mappedT);
					mappedTail.add(mappedT);
				}
			}	
			newEnv.endScope();
			type = new ListType(mappedHead, mappedTail);

			return type;
		}
		else
			throw new TypingException(l + " is not of list type");
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
		code.emit_comment("start a new frame to hold each list's value");
		StackFrame frame = code.createFrame();
		code.emit_new(frame.name);
		code.emit_dup();
		code.emit_invokespecial(frame.name, "<init>", "()V");
		if (frame.ancestor != null) {
			// Initialize Static Linker
			code.emit_dup();
			code.emit_aload(code.getCurrentSP());
			code.emit_putfield(frame.name, "SL", frame.ancestor.toJasmin());
		}
		ICompilationEnvironment newEnv = env.beginScope();
		code.emit_comment("compile list to get an array on stack");
		code.emit_astore(code.getCurrentSP());
		list.compile(code, env);
		code.pushFrame(frame);
		final int listPosition = code.getCurrentSP()+1;
		IType t = null;
		String type = null;
		if (((ASTList)list).size() > 0) {
			newEnv.assoc(id, 0);
			t = ((ASTList)list).head.getType();
			type = code.toJasmin(t);
			frame.addLocation(type);
		}
		for (int index = 0; index < ((ASTList)list).size(); index++) {
			code.emit_astore(listPosition);
			code.emit_comment("mapping index " + index);
			code.emit_aload(code.getCurrentSP());
			code.emit_dup();
			
			// get value from array at index 'position'
			code.emit_aload(listPosition);
			code.emit_dup();
			code.emit_astore(listPosition);
			code.emit_ldc(index);
			if (t == IntType.singleton)
				code.emit_iaload();
			else if (t == BoolType.singleton)
				code.emit_baload();
			else {
				code.emit_aaload();
				final String name = type.substring(1, type.length()-1);
				code.emit_checkcast(name);
			}
			// add value to new environment
			code.emit_putfield(frame.name, "loc_0", type);
			code.emit_astore(code.getCurrentSP());
			// now compile the expression on the new environment and put the result on array
			code.emit_aload(listPosition);
			code.emit_dup();
			code.emit_ldc(index);		
			exp.compile(code, newEnv);
			if (t == IntType.singleton)
				code.emit_iastore();
			else if (t == BoolType.singleton)
				code.emit_bastore();
			else
				code.emit_aastore();
		}	
		code.emit_comment("end");
		StackFrame currentFrame = code.getCurrentFrame();
		if (currentFrame.ancestor != null) {
			code.emit_aload(code.getCurrentSP());
			code.emit_checkcast(currentFrame.name);
			code.emit_getfield(currentFrame.name, "SL", currentFrame.ancestor.toJasmin());
		} else {
			code.emit_null();
		}
		code.emit_astore(code.getCurrentSP());
		code.pushFrame(currentFrame.ancestor);
		newEnv.endScope();
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, list, exp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTForEach))
			return false;
		ASTForEach other = (ASTForEach) obj;
		return Objects.equals(id, other.id) && Objects.equals(list, other.list) && Objects.equals(exp, other.exp);
	}

	@Override
	public String toString() {
		return "foreach " + id + " in " + list + " do " + exp;
	}

}
