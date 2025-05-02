package li2.plp.imperative1.declaration;

import li2.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li2.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative1.memory.EntradaVaziaException;

public class DeclaracaoComposta extends Declaracao {

	private Declaracao declaracao1;
	private Declaracao declaracao2;

	public DeclaracaoComposta(Declaracao parametro1, Declaracao parametro2) {
		super();
		this.declaracao1 = parametro1;
		this.declaracao2 = parametro2;
	}

	@Override
	public AmbienteExecucaoImperativa elabora(
			AmbienteExecucaoImperativa ambiente)
			throws IdentificadorJaDeclaradoException,
			IdentificadorNaoDeclaradoException, EntradaVaziaException {
		return declaracao2.elabora(declaracao1.elabora(ambiente));
	}

	@Override
	public boolean checaTipo(AmbienteCompilacaoImperativa ambiente)
			throws IdentificadorJaDeclaradoException,
			IdentificadorNaoDeclaradoException, EntradaVaziaException {
		System.err.println("Verificando tipos em DeclaracaoComposta...");
		System.err.println("Declaracao 1: " + declaracao1);
		boolean declaracao1Valida = declaracao1.checaTipo(ambiente);
		System.err.println("Declaracao 1 válida? " + declaracao1Valida);

		System.err.println("Declaracao 2: " + declaracao2);
		boolean declaracao2Valida = declaracao2.checaTipo(ambiente);
		System.err.println("Declaracao 2 válida? " + declaracao2Valida);

		boolean resultado = declaracao1Valida && declaracao2Valida;
		System.err.println("Resultado final de checaTipo em DeclaracaoComposta: " + resultado);
		return resultado;
	}
}
