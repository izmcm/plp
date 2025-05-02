package li2.plp.imperative2.declaration;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li2.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li2.plp.imperative1.declaration.Declaracao;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative1.memory.EntradaVaziaException;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class DeclaracaoProcedimento extends Declaracao {

	private Id id;
	private DefProcedimento defProcedimento;

	public DeclaracaoProcedimento(Id id, DefProcedimento defProcedimento) {
		super();
		this.id = id;
		this.defProcedimento = defProcedimento;
	}

	@Override
	public AmbienteExecucaoImperativa elabora(
			AmbienteExecucaoImperativa ambiente)
			throws IdentificadorJaDeclaradoException,
			IdentificadorNaoDeclaradoException, EntradaVaziaException {
		((AmbienteExecucaoImperativa2) ambiente).mapProcedimento(getId(),
				getDefProcedimento());
		return ambiente;
	}

	private Id getId() {
		return this.id;
	}

	@Override
	public boolean checaTipo(AmbienteCompilacaoImperativa ambiente)
			throws IdentificadorJaDeclaradoException,
			IdentificadorNaoDeclaradoException, EntradaVaziaException {
		System.err.println("Verificando tipos em DeclaracaoProcedimento...");
		System.err.println("ID do procedimento: " + id);
		System.err.println("Tipo do procedimento: " + defProcedimento.getTipo());

		ambiente.map(id, defProcedimento.getTipo());

		ListaDeclaracaoParametro parametrosFormais = getDefProcedimento().getParametrosFormais();
		System.err.println("Parâmetros formais: " + parametrosFormais);

		boolean parametrosValidos = parametrosFormais.checaTipo(ambiente);
		System.err.println("Parâmetros válidos? " + parametrosValidos);

		boolean resposta;
		if (parametrosValidos) {
			ambiente.incrementa();
			ambiente = parametrosFormais.elabora(ambiente);

			System.err.println("Verificando comando do procedimento...");
			boolean comandoValido = getDefProcedimento().getComando().checaTipo(ambiente);
			System.err.println("Comando: " + getDefProcedimento().getComando());
			System.err.println("Procedimento: " + getDefProcedimento());

			System.err.println("DeclaracaoProcedimento - Comando válido? " + comandoValido);

			resposta = comandoValido;
			ambiente.restaura();
		} else {
			resposta = false;
		}

		System.err.println("Resultado final de checaTipo em DeclaracaoProcedimento: " + resposta);
		return resposta;
	}

	private DefProcedimento getDefProcedimento() {
		return this.defProcedimento;
	}
}
