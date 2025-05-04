package li2.plp.expressions2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

/**
* Um objeto desta classe representa uma Expressao de Subtracao.
*/
public class ExpSub extends ExpBinaria {

	/**
	 * Controi uma Expressao de Subtracao com as sub-expressoes especificadas.
	 * Assume-se que estas expressoes resultam em <code>ValorInteiro</code>
	 * quando avaliadas.
	 *
	 * @param esq Expressao da esquerda
	 * @param dir Expressao da direita
	 */
	public ExpSub(Expressao esq, Expressao dir) {
		super(esq, dir, "-");
	}

	/**
	 * Retorna o valor da Expressao de Subtracao.
	 */
	public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		return new ValorInteiro(
				((ValorInteiro)getEsq().avaliar(amb)).valor() -
				((ValorInteiro)getDir().avaliar(amb)).valor()
		);
	}

	/**
	 * Realiza a verificacao de tipos desta expressao.
	 *
	 * @param ambiente o ambiente de compila��o.
	 * @return <code>true</code> se os tipos da expressao sao validos;
	 *          <code>false</code> caso contrario.
	 * @exception VariavelNaoDeclaradaException se existir um identificador
	 *          nao declarado no ambiente.
	 * @exception VariavelNaoDeclaradaException se existir um identificador
	 *          declarado mais de uma vez no mesmo bloco do ambiente.
	 */
	@Override
	protected boolean checaTipoElementoTerminal(AmbienteCompilacao ambiente)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		System.out.println("checaTipoElementoTerminal in ExpSoma");
		Tipo tipoEsq = getEsq().getTipo(ambiente);
		Tipo tipoDir = getDir().getTipo(ambiente);
	
		System.out.println("Tipo do operando esquerdo: " + tipoEsq);
		System.out.println("Tipo do operando direito: " + tipoDir);
	
		boolean tipoEsqValido = tipoEsq.eBigFraction() || tipoEsq.eBigInt() || tipoEsq.eInteiro();
		boolean tipoDirValido = tipoDir.eBigFraction() || tipoDir.eBigInt() || tipoDir.eInteiro();

		System.out.println("checaTipoElementoTerminal retorno: " + (tipoEsqValido && tipoDirValido));
		return tipoEsqValido && tipoDirValido;
	}

	/**
	 * Retorna os tipos possiveis desta expressao.
	 *
	 * @param ambiente o ambiente de compila��o.
	 * @return os tipos possiveis desta expressao.
	 */
	@Override
	public Tipo getTipo(AmbienteCompilacao ambiente) {
		System.out.println("getTipo in ExpSoma");
		Tipo tipoEsq = getEsq().getTipo(ambiente);
		Tipo tipoDir = getDir().getTipo(ambiente);

		System.out.println("Tipo do operando esquerdo: " + tipoEsq);
		System.out.println("Tipo do operando direito: " + tipoDir);
	
		if (tipoEsq.eIgual(tipoDir)) {
			return tipoEsq;
		}
	
		if (tipoEsq.eBigFraction() || tipoDir.eBigFraction()) {
			return TipoPrimitivo.BIGFRACTION;
		}

		if (tipoEsq.eBigInt() || tipoDir.eBigInt()) {
			return TipoPrimitivo.BIGINT;
		}

		throw new RuntimeException("Tipos incompatíveis na soma: " + tipoEsq + " e " + tipoDir);
	}
	
	@Override
	public ExpBinaria clone() {
		return new ExpSub(esq.clone(), dir.clone());
	}
}
