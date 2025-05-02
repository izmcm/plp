package li2.plp.imperative1.command;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;

public class Atribuicao implements Comando {

	private Id id;

	private Expressao expressao;

	public Atribuicao(Id id, Expressao expressao) {
		this.id = id;
		this.expressao = expressao;
	}

	/**
	 * Executa a atribui��o.
	 * 
	 * @param ambiente
	 *            o ambiente que contem o mapeamento entre identificadores e
	 *            valores.
	 * 
	 * @return o ambiente modificado pela execu��o da atribui��o.
	 * 
	 */
	public AmbienteExecucaoImperativa executar(
			AmbienteExecucaoImperativa ambiente)
			throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException {
		ambiente.changeValor(id, expressao.avaliar(ambiente));
		return ambiente;
	}

	/**
	 * Um comando de atribui��o est� bem tipado, se o tipo do identificador � o
	 * mesmo da express�o. O tipo de um identificador � determinado pelo tipo da
	 * express�o que o inicializou (na declara��o).
	 * 
	 * @param ambiente
	 *            o ambiente que contem o mapeamento entre identificadores e
	 *            valores.
	 * 
	 * @return <code>true</code> se os tipos da atribui��o s�o v�lidos;
	 *         <code>false</code> caso contrario.
	 * 
	 */
	public boolean checaTipo(AmbienteCompilacaoImperativa ambiente)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		System.out.println("Verificando tipos em Atribuicao...");
		System.out.println("Expressao: " + expressao);

		boolean expressaoValida = expressao.checaTipo(ambiente);
		System.out.println("Expressao válida? " + expressaoValida);
		System.out.println("Id: " + id);

		Tipo tipoId = id.getTipo(ambiente);
		System.out.println("Tipo do id: " + tipoId);
		Tipo tipoExpressao = expressao.getTipo(ambiente);
		System.out.println("Tipo da expressao: " + tipoExpressao);
		System.out.println("Comparando tipos...");
		boolean tiposIguais = tipoId.eIgual(tipoExpressao);
		System.out.println("Tipos iguais? " + tiposIguais);
		
		boolean resposta = expressaoValida && tiposIguais;
		System.out.println("Resultado final de checaTipo em Atribuicao: " + resposta);
		return resposta;
	}

}
