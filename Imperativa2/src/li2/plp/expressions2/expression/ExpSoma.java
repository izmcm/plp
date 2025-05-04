package li2.plp.expressions2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

/**
 * Um objeto desta classe representa uma Expressao de Soma.
 */
public class ExpSoma extends ExpBinaria {

	/**
	 * Controi uma Expressao de Soma com as sub-expressoes especificadas.
	 * Assume-se que estas sub-expressoes resultam em <code>ValorInteiro</code> 
	 * quando avaliadas.
	 * @param esq Expressao da esquerda
	 * @param dir Expressao da direita
	 */
	public ExpSoma(Expressao esq, Expressao dir) {
		super(esq, dir, "+");
	}

	/**
	 * Retorna o valor da Expressao de Soma
	 */
	@Override
	public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Valor valorEsq = getEsq().avaliar(amb);
		Valor valorDir = getDir().avaliar(amb);
	
		System.out.println("valorEsq: " + valorEsq);
		System.out.println("valorDir: " + valorDir);

		// Caso ambos sejam BigFraction ou um deles seja BigFraction
		if (valorEsq instanceof ValorBigFraction || valorDir instanceof ValorBigFraction) {
			ValorBigFraction bigFractionEsq = valorEsq instanceof ValorBigFraction
					? (ValorBigFraction) valorEsq
					: valorEsq.toBigFraction();
			ValorBigFraction bigFractionDir = valorDir instanceof ValorBigFraction
					? (ValorBigFraction) valorDir
					: valorDir.toBigFraction();
			return bigFractionEsq.add(bigFractionDir);
		}
	
		// Caso ambos sejam BigInt ou um deles seja BigInt
		if (valorEsq instanceof ValorBigInt || valorDir instanceof ValorBigInt) {
			ValorBigInt bigIntEsq = valorEsq instanceof ValorBigInt
					? (ValorBigInt) valorEsq
					: (ValorBigInt) valorEsq.toBigInt();
			ValorBigInt bigIntDir = valorDir instanceof ValorBigInt
					? (ValorBigInt) valorDir
					: (ValorBigInt) valorDir.toBigInt() ;
			return bigIntDir.add(bigIntEsq);
		}
	
		// Caso ambos sejam Inteiros
		return new ValorInteiro(
				((ValorInteiro) valorEsq).valor() + ((ValorInteiro) valorDir).valor());
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
		return new ExpSoma(esq.clone(), dir.clone());
	}
}
