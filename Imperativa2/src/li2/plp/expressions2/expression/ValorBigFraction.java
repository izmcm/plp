package li2.plp.expressions2.expression;

import java.util.List;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;

// Representa uma fração com numerador e denominador como listas de dígitos
// Exemplo: 123/456 será representado como [[1, 2, 3], [4, 5, 6]]
public class ValorBigFraction extends ValorNumerico<List<ValorBigInt>> {
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
        System.out.println("ValorBigFraction this.valor(): " + this.valor());
        return this.valor().get(0).toString() + "/" + this.valor().get(1).toString();
    }

    @Override
    public ValorBigFraction clone() {
        return new ValorBigFraction(this.valor());
    }

    public ValorBigFraction sum(ValorBigFraction other) {
        ValorBigInt firstNumerator = this.valor().get(0);
        ValorBigInt firstDenominator = this.valor().get(1);

        ValorBigInt secondNumerator = other.valor().get(0);
        ValorBigInt secondDenominator = other.valor().get(1);

        ValorBigInt newFirstNumerator = firstNumerator.multiply(secondDenominator);
        ValorBigInt newSecondNumerator = secondNumerator.multiply(firstDenominator);

        ValorBigInt resultNumerator = newFirstNumerator.sum(newSecondNumerator);
        ValorBigInt resultDenominator = firstDenominator.multiply(secondDenominator);

        ValorBigFraction result = new ValorBigFraction(List.of(resultNumerator, resultDenominator));
        return result.simplify();
    }

    public ValorBigFraction sub(ValorBigFraction other) {
        ValorBigInt firstNumerator = this.valor().get(0);
        ValorBigInt firstDenominator = this.valor().get(1);

        ValorBigInt secondNumerator = other.valor().get(0);
        ValorBigInt secondDenominator = other.valor().get(1);

        ValorBigInt newFirstNumerator = firstNumerator.multiply(secondDenominator);
        ValorBigInt newSecondNumerator = secondNumerator.multiply(firstDenominator);

        ValorBigInt resultNumerator = newFirstNumerator.sub(newSecondNumerator);
        ValorBigInt resultDenominator = firstDenominator.multiply(secondDenominator);

        ValorBigFraction result = new ValorBigFraction(List.of(resultNumerator, resultDenominator));
        return result.simplify();
    }

    public ValorBigFraction multiply(ValorBigFraction other) {
        ValorBigInt firstNumerator = this.valor().get(0);
        ValorBigInt firstDenominator = this.valor().get(1);

        ValorBigInt secondNumerator = other.valor().get(0);
        ValorBigInt secondDenominator = other.valor().get(1);

        ValorBigInt resultNumerator = firstNumerator.multiply(secondNumerator);
        ValorBigInt resultDenominator = firstDenominator.multiply(secondDenominator);

        ValorBigFraction result = new ValorBigFraction(List.of(resultNumerator, resultDenominator));
        return result.simplify();
    }

    public ValorBigFraction div(ValorBigFraction other) {
        ValorBigInt firstNumerator = this.valor().get(0);
        ValorBigInt firstDenominator = this.valor().get(1);

        // inverte a fração
        ValorBigInt secondNumerator = other.valor().get(1);
        ValorBigInt secondDenominator = other.valor().get(0);

        ValorBigInt resultNumerator = firstNumerator.multiply(secondNumerator);
        ValorBigInt resultDenominator = firstDenominator.multiply(secondDenominator);

        ValorBigFraction result = new ValorBigFraction(List.of(resultNumerator, resultDenominator));
        return result.simplify();
    }

    public ValorBigFraction simplify() {
        ValorBigInt numerator = this.valor().get(0);
        ValorBigInt denominator = this.valor().get(1);
    
        // Calcula o MDC (Máximo Divisor Comum) entre o numerador e o denominador
        ValorBigInt gcd = numerator.gcd(denominator);
    
        // Divide o numerador e o denominador pelo MDC para simplificar a fração
        // Pega apenas o primeiro elemento da lista retornada já que o segundo é sempre 1
        // Porque temos garantia que a divisão é exata
        ValorBigInt simplifiedNumerator = numerator.div(gcd).valor().get(0);
        ValorBigInt simplifiedDenominator = denominator.div(gcd).valor().get(0);
    
        return new ValorBigFraction(List.of(simplifiedNumerator, simplifiedDenominator));
    }
}