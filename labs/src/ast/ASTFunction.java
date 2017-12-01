package ast;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import compiler.CodeBlock;
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

public class ASTFunction implements ASTNode {

	public static class Parameter {
		private final String id;
		private final IType type;

		public Parameter(String id, IType type) {
			this.id = id;
			this.type = type;
		}

		public String getId() {
			return id;
		}

		public IType getType() {
			return type;
		}

		@Override
		public String toString() {
			return id + ":" + type;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, type);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Parameter))
				return false;
			Parameter other = (Parameter) obj;
			return Objects.equals(id, other.getId()) && Objects.equals(type, other.getType());
		}

	}

	final Collection<Parameter> parameters;
	final ASTNode body;
	private IType type;

	public ASTFunction(Collection<Parameter> parameters, ASTNode body) {
		this.parameters = parameters;
		this.body = body;
		this.type = null;
	}

	@Override
	public String toString() {
		return "fun " + String.join(", ", parameters.stream().map(Parameter::toString).collect(Collectors.toList())) + " -> " + body + " end";
	}

	@Override
	public IValue eval(IEnvironment<IValue> env) throws TypeMismatchException, DuplicateIdentifierException, UndeclaredIdentifierException {
		return new ClosureValue(parameters, body, env);
	}

	@Override
	public IType typecheck(IEnvironment<IType> env) throws TypingException, DuplicateIdentifierException, UndeclaredIdentifierException {
		IEnvironment<IType> newEnv = env.beginScope();
		for (Parameter param : parameters)
			newEnv.assoc(param.id, param.type);
		IType tr = body.typecheck(newEnv);
		type = new FunType(parameters.stream().map(Parameter::getType).collect(Collectors.toList()), tr);

		return type;
	}

	@Override
	public void compile(CodeBlock code, ICompilationEnvironment env)
			throws DuplicateIdentifierException, UndeclaredIdentifierException {
		// TODO Auto-generated method stub

	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameters, body);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ASTFunction))
			return false;
		ASTFunction other = (ASTFunction) obj;
		return Objects.equals(parameters, other.parameters) && Objects.equals(body, other.body);
	}

}
