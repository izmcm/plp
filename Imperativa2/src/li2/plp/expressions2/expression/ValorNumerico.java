package li2.plp.expressions2.expression;

import java.util.ArrayList;
import java.util.List;

public abstract class ValorNumerico<T> extends ValorConcreto<T> {

    public ValorNumerico(T valor) {
        super(valor);
    }

    public ValorNumerico<?> sum(ValorNumerico<?> outro) {
        if (this instanceof ValorBigFraction || outro instanceof ValorBigFraction) {
            ValorBigFraction bigFractionEsq = this.toBigFraction();
            ValorBigFraction bigFractionDir = outro.toBigFraction();
            return bigFractionEsq.sum(bigFractionDir);
        }

        if (this instanceof ValorBigInt || outro instanceof ValorBigInt) {
            ValorBigInt bigIntEsq = this.toBigInt();
            ValorBigInt bigIntDir = outro.toBigInt();
            return bigIntEsq.sum(bigIntDir);
        }

        if (this instanceof ValorInteiro && outro instanceof ValorInteiro) {
            return ((ValorInteiro) this).sum((ValorInteiro) outro);
        }

        throw new UnsupportedOperationException("Operação de soma não suportada para os tipos fornecidos.");
    }

    public ValorNumerico<?> sub(ValorNumerico<?> outro) {
        if (this instanceof ValorBigFraction || outro instanceof ValorBigFraction) {
            ValorBigFraction bigFractionEsq = this.toBigFraction();
            ValorBigFraction bigFractionDir = outro.toBigFraction();
            return bigFractionEsq.sub(bigFractionDir);
        }

        if (this instanceof ValorBigInt || outro instanceof ValorBigInt) {
            ValorBigInt bigIntEsq = this.toBigInt();
            ValorBigInt bigIntDir = outro.toBigInt();
            return bigIntEsq.sub(bigIntDir);
        }

        if (this instanceof ValorInteiro && outro instanceof ValorInteiro) {
            return ((ValorInteiro) this).sub((ValorInteiro) outro);
        }

        throw new UnsupportedOperationException("Operação de subtração não suportada para os tipos fornecidos.");
    }

    public ValorNumerico<?> multiply(ValorNumerico<?> outro) {
        if (this instanceof ValorBigFraction || outro instanceof ValorBigFraction) {
            ValorBigFraction bigFractionEsq = this.toBigFraction();
            ValorBigFraction bigFractionDir = outro.toBigFraction();
            return bigFractionEsq.multiply(bigFractionDir);
        }

        if (this instanceof ValorBigInt || outro instanceof ValorBigInt) {
            ValorBigInt bigIntEsq = this.toBigInt();
            ValorBigInt bigIntDir = outro.toBigInt();
            return bigIntEsq.multiply(bigIntDir);
        }

        if (this instanceof ValorInteiro && outro instanceof ValorInteiro) {
            return ((ValorInteiro) this).multiply((ValorInteiro) outro);
        }

        throw new UnsupportedOperationException("Operação de multiplicação não suportada para os tipos fornecidos.");
    }

    public ValorNumerico<?> div(ValorNumerico<?> outro) {
        if (this instanceof ValorBigFraction || outro instanceof ValorBigFraction) {
            ValorBigFraction bigFractionEsq = this.toBigFraction();
            ValorBigFraction bigFractionDir = outro.toBigFraction();
            return bigFractionEsq.div(bigFractionDir);
        }

        if (this instanceof ValorBigInt || outro instanceof ValorBigInt) {
            ValorBigInt bigIntEsq = this.toBigInt();
            ValorBigInt bigIntDir = outro.toBigInt();
            return bigIntEsq.div(bigIntDir);
        }

        if (this instanceof ValorInteiro && outro instanceof ValorInteiro) {
            return ((ValorInteiro) this).div((ValorInteiro) outro);
        }

        throw new UnsupportedOperationException("Operação de divisão não suportada para os tipos fornecidos.");
    }

    public ValorBigInt toBigInt() {
        System.out.println("toBigInt() called");
        System.out.println("this: " + this);
        if (this instanceof ValorBigInt) {
            return (ValorBigInt) this;
        } else if (this instanceof ValorInteiro) {
            int valorInteiro = ((ValorInteiro) this).valor();
            String valorString = Integer.toString(valorInteiro);
            List<Integer> listaDigitos = new ArrayList<>();
            for (char c : valorString.toCharArray()) {
                listaDigitos.add(Character.getNumericValue(c));
            }

            ValorBigInt valorBigInt = new ValorBigInt(listaDigitos);
            return valorBigInt;
        } else {
            throw new UnsupportedOperationException("Não é possível converter " + this.getClass().getSimpleName() + " para ValorBigInt.");
        }
    }

    public ValorBigFraction toBigFraction() {
        if (this instanceof ValorBigFraction) {
            return (ValorBigFraction) this;
        } else if (this instanceof ValorInteiro) {
            int valorInteiro = ((ValorInteiro) this).valor();
            List<Integer> listaDigitos = new ArrayList<>();
            for (char c : Integer.toString(valorInteiro).toCharArray()) {
                listaDigitos.add(Character.getNumericValue(c));
            }

            ValorBigInt numerador = new ValorBigInt(listaDigitos);
            ValorBigInt denominador = new ValorBigInt(new ArrayList<>(List.of(1)));
            List<ValorBigInt> valor = new ArrayList<>();
            valor.add(numerador);
            valor.add(denominador);
            return new ValorBigFraction(valor);
        } else if (this instanceof ValorBigInt) {
            ValorBigInt numerador = (ValorBigInt) this;
            ValorBigInt denominador = new ValorBigInt(new ArrayList<>(List.of(1)));
            List<ValorBigInt> valor = new ArrayList<>();
            valor.add(numerador);
            valor.add(denominador);
            return new ValorBigFraction(valor);
        } else {
            throw new UnsupportedOperationException("Não é possível converter " + this.getClass().getSimpleName() + " para ValorBigFraction.");
        }
    }
}