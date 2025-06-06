package li2.plp.expressions1.util;

/**
 * Enum que representa os possiveis tipos primitivo de uma expressao.
 * Objetos desta classe sao imutaveis, portanto as vezes as instancias sao
 * compartilhadas.
 * 
 * Modificado em 11/07/2005 por Leonardo Lucena para usar tipos enumerados
 */
public enum TipoPrimitivo implements Tipo {

	INTEIRO("INTEIRO"),
	BOOLEANO("BOOLEANO"),
	STRING("STRING"),
    BIGINT("BIGINT"),
    BIGFRACTION("BIGFRACTION");

	protected String nome;

	/**
	 * Construtor da classe que representa um tipo qualquer.
	 */
	private TipoPrimitivo(String nome) {
		this.nome = nome;
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#getNome()
	 */
	public String getNome() {
		return nome;
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eNumerico()
	 */
	public boolean eNumerico() {
		return this.eIgual(INTEIRO) || this.eIgual(BIGINT) || this.eIgual(BIGFRACTION);
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eInteiro()
	 */
	public boolean eInteiro() {
		return this.eIgual(INTEIRO);
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eBooleano()
	 */
	public boolean eBooleano() {
		return this.eIgual(BOOLEANO);
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eString()
	 */
	public boolean eString() {
		return this.eIgual(STRING);
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eBigInt()
	 */
	public boolean eBigInt() {
		return this.eIgual(BIGINT);
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eBigFraction()
	 */
	public boolean eBigFraction() {
		return this.eIgual(BIGFRACTION);
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eIgual(li2.plp.expressions1.util.Tipo)
	 */
	public boolean eIgual(Tipo tipo) {
		System.out.println("TipoPrimitivo.eIgual");
		boolean ret = false;
		if (eValido()) {
			if (tipo.eValido()) {
				System.out.println("TipoPrimitivo.eIgual: " + this.nome + " == " + tipo.getNome() + "?");
				if(this.nome.equals(tipo.getNome())) return true;
			} else {
				ret = tipo.eIgual(this);
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#eValido()
	 */
	public boolean eValido() {
		return this.nome != null && nome.length() > 0;
	}

	/* (non-Javadoc)
	 * @see li2.plp.expressions1.util.Tipo#intersecao(li2.plp.expressions1.util.Tipo)
	 */
	public TipoPrimitivo intersecao(Tipo outroTipo) {
		if (outroTipo.eIgual(this)) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.nome;
	}

}
