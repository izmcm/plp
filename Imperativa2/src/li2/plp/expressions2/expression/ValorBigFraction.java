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
        return this.valor().get(0).toString() + "/" + this.valor().get(1).toString();
    }

    @Override
    public ValorBigFraction clone() {
        return new ValorBigFraction(this.valor());
    }

    public ValorNumerico sum(ValorBigFraction other) {
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

    public ValorNumerico sub(ValorBigFraction other) {
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

    public ValorNumerico multiply(ValorBigFraction other) {
        ValorBigInt firstNumerator = this.valor().get(0);
        ValorBigInt firstDenominator = this.valor().get(1);

        ValorBigInt secondNumerator = other.valor().get(0);
        ValorBigInt secondDenominator = other.valor().get(1);

        ValorBigInt resultNumerator = firstNumerator.multiply(secondNumerator);
        ValorBigInt resultDenominator = firstDenominator.multiply(secondDenominator);

        ValorBigFraction result = new ValorBigFraction(List.of(resultNumerator, resultDenominator));
        return result.simplify();
    }

    public ValorNumerico div(ValorBigFraction other) {
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

    public ValorNumerico simplify() {
        ValorBigInt numerator = this.clone().valor().get(0);
        ValorBigInt denominator = this.clone().valor().get(1);

        // Calcula o MDC (Máximo Divisor Comum) entre o numerador e o denominador
        ValorBigInt gcd = numerator.abs().gcd(denominator.abs());
    
        // Divide o numerador e o denominador pelo MDC para simplificar a fração
        // Como é o MDC, o resultado é sempre um ValorBigInt
        ValorBigInt simplifiedNumerator = ((ValorBigInt) numerator.div(gcd));
        ValorBigInt simplifiedDenominator = ((ValorBigInt) denominator.div(gcd));

        // O denominador deve ser sempre positivo
        if (simplifiedDenominator.isNegative) {
            simplifiedNumerator.isNegative = !simplifiedNumerator.isNegative;
            simplifiedDenominator.isNegative = false;
        }

        if(simplifiedDenominator.compareTo((new ValorBigInt(List.of(1)))) == 0) {
            System.err.println("ValorBigFraction: denominador igual a 1");
            return simplifiedNumerator.toBigInt();
        }

        return new ValorBigFraction(List.of(simplifiedNumerator, simplifiedDenominator));
    }
}
