package li2.plp.expressions2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;

/**
 * Objetos desta classe encapsulam valor inteiro.
 */
public class ValorInteiro extends ValorNumerico<Integer> {
	public ValorInteiro(Integer valor) {
		super(valor);
	}

	public Tipo getTipo(AmbienteCompilacao amb) {
		return TipoPrimitivo.INTEIRO;
	}

	public ValorInteiro clone(){
		return new ValorInteiro(this.valor());
	}

    public ValorInteiro sum(ValorInteiro outro) {
        return new ValorInteiro(this.valor() + ((ValorInteiro) outro).valor());
    }

    public ValorInteiro sub(ValorInteiro outro) {
        return new ValorInteiro(this.valor() - ((ValorInteiro) outro).valor());
    }

    public ValorInteiro multiply(ValorInteiro outro) {
        return new ValorInteiro(this.valor() * ((ValorInteiro) outro).valor());
    }

    public ValorInteiro div(ValorInteiro outro) {
        return new ValorInteiro(this.valor() / ((ValorInteiro) outro).valor());
    }
}
