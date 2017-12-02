package ast;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

import compiler.ICodeBuilder;
import compiler.TypeSignature;
import environment.DuplicateIdentifierException;
import environment.ICompilationEnvironment;
import environment.IEnvironment;
import environment.UndeclaredIdentifierException;
import types.FunType;
import types.IType;
import types.TypingException;
import values.ClosureValue;
import values.IValue;
import values.TypeMismatchException;

public class ASTCall implements ASTNode {

	final ASTNode function;
	final Collection<ASTNode> arguments;
	private IType type;

	public ASTCall(ASTNode function, Collection<ASTNode> arguments) {
		this.function = function;
		this.arguments = arguments;
		this.type = null;
	}

	@Override
	public String toString() {
		return function + " (" + String.join(",", arguments.stream().map(ASTNode::toString).collect(Collectors.toList())) + ")";
	}
	
	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IValue f = function.eval(env);

		if (f instanceof ClosureValue) {
			ClosureValue closure = (ClosureValue) f;
			IEnvironment<IValue> newEnv = closure.env.beginScope();
			Iterator<String> params = closure.params.stream().map(p -> p.id).collect(Collectors.toList()).iterator();
			Iterator<ASTNode> args = arguments.iterator();
			while (params.hasNext() && args.hasNext()) {
				newEnv.assoc(params.next(), args.next().eval(env)); // TODO
																	// verificar
																	// tipo dos
																	// argumentos
			}
			IValue value = closure.body.eval(newEnv);
			newEnv.endScope();
			return value;
		} else {
			throw new TypeMismatchException(f + " is not a function.");
		}
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IType f = function.typecheck(env);

		if (f instanceof FunType) {
			FunType fun = (FunType) f;
			Iterator<IType> params = fun.paramsType.iterator();
			Iterator<ASTNode> args = arguments.iterator();
			while (params.hasNext() && args.hasNext()) {
				if (!params.next().equals(args.next().typecheck(env)))
					throw new TypingException("Function expecting arguments of type " + fun.paramsType + " and got " + arguments + ".");
			}
			type = fun.getRetType();
		} else {
			throw new TypingException(f + " is not a function.");
		}

		return type;
	}

	@Override
	public void compile(ICodeBuilder code, ICompilationEnvironment env) throws DuplicateIdentifierException, UndeclaredIdentifierException {
//		fun x -> x + 1 end (1)
		
//		interface Type_00 { int call (int); }
//		.source type_00.j
//		.interface public type_00
//		.super java/lang/Object
//		.method public abstract call(I)I
//		.end method
		
//		class Closure_00 implements Type_00 { int call(int x) { return x+1; } }
// 		.source closure_00.j
//		.class closure_00.j
//		.super java/lang/Object"
//		.implements Type_00
//		.field public SL LFrame_1; // StackFrame -> env
//		.method public call(I)I
//			.limit locals 3
//			.limit stack 256
//			; initialize new stackframe frame_1
//			new frame_1
//			dup
//			invokespecial frame_1/<init>()V
//			dup
//			iload 1
//			putfield frame_1/loc_00 I
//			astore 2
//		.end method
		
		
		
		
		
//		decl x = 1 in fun x:int -> x+1 end (1) end
		
		// decl
//		new Frame_1
//		dup
//		invokespecial Frame_1/<init>()V
//		dup
//		sipush 1
//		putfield Frame_1/loc_00 I
//		astore 1 ; SP is now at local variable 1, 0 is reserved for “this”   //astore nArgs+1
		
		// fun
//		; initialize a new frame
//		new Frame_2
//		dup
//		invokespecial Frame_2/<init>()V
//		dup
//		iload 1
//		putfield Frame_2/loc_00 I
//		astore 2
//		; initialize a new closure
//		new closure_01
//		dup
//		invokespecial closure_01/<init>()V
//		dup
//		aload 1 ; SP
//		putfield closure_01/SL Lframe_1;
		
		// call
//		checkcast type_00
//		sipush 1
//		invokeinterface type_00/call(I)I      2
		System.out.println("compiling: " + this);
		
		function.compile(code, env);
		FunType type = (FunType)function.getType();
		TypeSignature signature = code.getSignature(type);
		code.emit_checkcast(signature.name);
		for (ASTNode argument : arguments)
			argument.compile(code, env);
		code.emit_invokeinterface(signature.name, "call", signature.toJasmin());
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(function, arguments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTCall))
			return false;
		ASTCall other = (ASTCall) obj;
		return Objects.equals(function, other.function) && Objects.equals(arguments, other.arguments);
	}

}
