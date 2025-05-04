package li2.plp.expressions2.expression;

import java.util.List;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;

// Representa uma fração com numerador e denominador como listas de dígitos
// Exemplo: 123/456 será representado como [[1, 2, 3], [4, 5, 6]]
public class ValorBigFraction extends ValorConcreto<List<ValorBigInt>> {
    public ValorBigFraction(List<ValorBigInt> valor) {
        super(valor);

        if (valor.size() != 2) {
            throw new IllegalArgumentException(
                    "A fração deve conter exatamente dois elementos: numerador e denominador.");
        }
    }

    public Tipo getTipo(AmbienteCompilacao amb) {
        return TipoPrimitivo.BIGFRACTION;
    }

    @Override
    public String toString() {
        return this.valor().get(0).toString() + "/" + this.valor().get(1).toString();
    }

    @Override
    public ValorBigFraction clone() {
        return new ValorBigFraction(this.valor());
    }

    // TODO: simplificar a fração
    public ValorBigFraction add(ValorBigFraction outro) {
        ValorBigInt numerador1 = this.valor().get(0);
        ValorBigInt denominador1 = this.valor().get(1);

        ValorBigInt numerador2 = outro.valor().get(0);
        ValorBigInt denominador2 = outro.valor().get(1);

        ValorBigInt novoNumerador1 = numerador1.multiply(denominador2);
        ValorBigInt novoNumerador2 = numerador2.multiply(denominador1);
        ValorBigInt novoDenominador = denominador1.multiply(denominador2);

        return new ValorBigFraction(List.of(novoNumerador1.add(novoNumerador2), novoDenominador));
    }
}