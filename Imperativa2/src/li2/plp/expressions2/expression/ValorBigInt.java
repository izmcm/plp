package li2.plp.expressions2.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;

public class ValorBigInt extends ValorNumerico<List<Integer>> {

    public boolean isNegative;

    public ValorBigInt(List<Integer> valor, boolean isNegative) {
        super(valor);
        this.isNegative = isNegative;
    }

    public ValorBigInt(List<Integer> valor) {
        this(valor, false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int digit : this.valor()) {
            sb.append(digit);
        }

        if (isNegative) {
            sb.insert(0, "-");
        }
        return sb.toString();
    }

    public Tipo getTipo(AmbienteCompilacao amb) {
        return TipoPrimitivo.BIGINT;
    }

    @Override
    public ValorBigInt clone() {
        return new ValorBigInt(new ArrayList<>(this.valor()), this.isNegative);
    }

    // [1, 3, 2] + [3, 5, 9]
    // 1. Inverter listas
    // [2, 3, 1]
    // + [9, 5, 3]
    // -------------
    // -> 9 + 2 + 0 = 1 (carry = 1 & result = [1, 0, 0])
    // -> 5 + 3 + 1 = 9 (carry = 0 & result = [1, 9, 0])
    // -> 3 + 1 + 0 = 4 (carry = 0 & result = [1, 9, 4])
    // 2. Inverter lista
    // result = [4, 9, 1]
    public ValorBigInt sum(ValorBigInt other) {
        if (this.isNegative == other.isNegative) {
            List<Integer> result = new ArrayList<>();
            List<Integer> firstValue = this.clone().valor();
            List<Integer> secondValue = other.clone().valor();

            Collections.reverse(firstValue);
            Collections.reverse(secondValue);

            int carry = 0;
            int maxLength = Math.max(firstValue.size(), secondValue.size());

            for (int i = 0; i < maxLength; i++) {
                int firstDigit = i < firstValue.size() ? firstValue.get(i) : 0;
                int secondDigit = i < secondValue.size() ? secondValue.get(i) : 0;

                int sumHelper = firstDigit + secondDigit + carry;
                result.add(sumHelper % 10);
                carry = sumHelper / 10;
            }

            if (carry > 0) {
                result.add(carry);
            }

            Collections.reverse(result);

            return new ValorBigInt(result, this.isNegative);
        } else {
            ValorBigInt positiveValue = this.isNegative ? other : this;
            ValorBigInt negativeValue = this.isNegative ? this : other;

            // remove o sinal para tratar como uma subtração normal
            return positiveValue.sub(new ValorBigInt(negativeValue.valor(), false));
        }
    }

    // [4, 9, 1] - [3, 5, 9]
    // 1. Inverter listas
    // [1, 9, 4]
    // - [9, 5, 3]
    // -------------
    // -> 1 - 9 - 0 = -8
    // if(<0) 8 + 10 = 2 (borrow = 1 & result = [2, 0, 0])
    // -> 9 - 5 - 1 = 3
    // if(>0) (borrow = 0 & result = [2, 3, 0])
    // -> 4 - 3 - 0 = 1
    // if(>0) (borrow = 0 & result = [2, 3, 1])
    // 2. Inverter lista
    // 3. Remover zeros à esquerda
    // result = [1, 3, 2]
    public ValorBigInt sub(ValorBigInt other) {
        // -1 - (+1) = - 1 - 1 = -2
        // 1 - (-1) = 1 + 1 = 2
        if (this.isNegative != other.isNegative) {
            return this.sum(new ValorBigInt(other.valor(), !other.isNegative));
        }

        boolean resultIsNegative = false;
        List<Integer> firstValue = this.clone().valor();
        List<Integer> secondValue = other.clone().valor();

        // se o primeiro for menor que o segundo, inverte os valores
        // e a subtração será negativa
        if (this.abs().compareTo(other.abs()) < 0) {
            resultIsNegative = true;
            List<Integer> temp = firstValue;
            firstValue = secondValue;
            secondValue = temp;
        }

        Collections.reverse(firstValue);
        Collections.reverse(secondValue);

        List<Integer> result = new ArrayList<>();
        int borrow = 0;

        for (int i = 0; i < firstValue.size(); i++) {
            int firstDigit = firstValue.get(i);
            int secondDigit = i < secondValue.size() ? secondValue.get(i) : 0;

            int subtraction = firstDigit - secondDigit - borrow;

            if (subtraction < 0) {
                subtraction += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }

            result.add(subtraction);
        }

        Collections.reverse(result);

        result = removeTrailingZeros(result);

        return new ValorBigInt(result, resultIsNegative);
    }

    public ValorBigInt abs() {
        return new ValorBigInt(this.valor(), false);
    }

    // [4, 9] * [1, 2]
    // 1. Inverter listas
    // [9, 4]
    // * [2, 1]
    // -------------
    // product = [0, 0, 0, 0]
    // -> Para cada índice em firstValue (i) e secondValue (j), calcular product[i +
    // j]:
    // - i = 0, j = 0 -> 9 * 2 = 18 (product = [8, 1, 0, 0, 0, 0])
    // - i = 0, j = 1 -> 9 * 1 = 9 (product = [8, 0, 1, 0, 0, 0])
    // - i = 1, j = 0 -> 4 * 2 = 8 (product = [8, 8, 1, 0, 0, 0])
    // - i = 1, j = 1 -> 4 * 1 = 4 + 1 (product = [8, 8, 5, 0, 0, 0])
    // -------------
    // product = [8, 8, 5, 0, 0, 0]
    // 2. Remover zeros à esquerda (se houver)
    // 3. Inverter lista
    // result = [5, 8, 8]
    public ValorBigInt multiply(ValorBigInt other) {
        List<Integer> result = new ArrayList<>();
        List<Integer> firstValue = this.clone().valor();
        List<Integer> secondValue = other.clone().valor();

        Collections.reverse(firstValue);
        Collections.reverse(secondValue);

        int[] product = new int[firstValue.size() + secondValue.size()];

        for (int i = 0; i < firstValue.size(); i++) {
            for (int j = 0; j < secondValue.size(); j++) {
                product[i + j] += firstValue.get(i) * secondValue.get(j);
                product[i + j + 1] += product[i + j] / 10;
                product[i + j] %= 10;
            }
        }

        boolean leadingZero = true;
        for (int i = product.length - 1; i >= 0; i--) {
            if (product[i] == 0 && leadingZero) {
                continue;
            }
            leadingZero = false;
            result.add(product[i]);
        }

        if (result.isEmpty()) {
            result.add(0);
        }

        boolean isResultNegative = this.isNegative != other.isNegative;

        return new ValorBigInt(result, isResultNegative);
    }

    // [1, 0, 0, 0] / [2, 5]
    // 1. Checa se divisor é zero
    // 2. Checa se divisor é maior que dividendo
    // ------------------------------------------
    // this (dividendo) = [1, 0, 0, 0]
    // other (divisor) = [2, 5]
    // 
    // remainder = []
    // quotientDigits = []
    //
    // 1. Primeiro dígito do dividendo
    // remainder = [1]
    // 1 < 25? -> quotientDigits = [0]
    //
    // 2. Adiciona próximo dígito
    // remainder = [1, 0] = 10
    // 10 < 25? -> quotientDigits = [0, 0]
    //
    // 3. Adiciona próximo dígito
    // remainder = [1, 0, 0] = 100
    // 100 >= 25?
    // -> [100] - [25] = [75]   | count = 1
    // -> [75] - [25] = [50]    | count = 2
    // -> [50] - [25] = [25]    | count = 3
    // -> [25] - [25] = [0]     | count = 4
    // -> [0] < [25] -> fim
    // quotientDigits = [0, 0, 4]
    // remainder = [0]
    //
    // 4. Adiciona último dígito
    // remainder = [0, 0]
    // 0 < 25? -> quotientDigits = [0, 0, 4, 0] 
    //
    // Remover zeros à esquerda:
    // quotient = [4, 0]
    // remainder = [0]
    public ValorBigFraction div(ValorBigInt other) {
        if (other.toString().equals("0")) {
            throw new ArithmeticException("Divisão por zero não é permitida.");
        }

        if(other.toString().equals("1")) {
            return new ValorBigFraction(List.of(this.clone(), new ValorBigInt(List.of(1))));
        }

        if (this.abs().compareTo(other.abs()) < 0) {
            return new ValorBigFraction(List.of(this.clone(), other.clone())).simplify();
        }

        boolean isQuotientNegative = this.isNegative != other.isNegative;

        ValorBigInt dividend = this.clone();
        dividend.isNegative = false;
        ValorBigInt divisor = other.clone();
        divisor.isNegative = false;

        List<Integer> quotientDigits = new ArrayList<>();
        List<Integer> remainder = new ArrayList<>();

        for (int digit : dividend.valor()) {
            remainder.add(digit);
            remainder = removeTrailingZeros(remainder);

            int count = 0;
            while ((new ValorBigInt(remainder)).compareTo(divisor) >= 0) {
                remainder = (new ValorBigInt(remainder)).sub(divisor).valor();
                count++;
            }
            quotientDigits.add(count);
        }

        remainder = removeTrailingZeros(remainder);
        quotientDigits = removeTrailingZeros(quotientDigits);

        ValorBigInt quotient = new ValorBigInt(quotientDigits, isQuotientNegative);

        if (remainder.isEmpty() || (remainder.size() == 1 && remainder.get(0) == 0)) {
            return new ValorBigFraction(List.of(quotient, new ValorBigInt(List.of(1))));
        } else {
            return new ValorBigFraction(List.of(this.clone(), other.clone()));
        }
    }

    // Retorna
    // 0 -> se os dois valores forem iguais
    // 1 -> se firstValue for maior que secondValue
    // -1 -> se valor 1 for menor que secondValue
    public int compareTo(ValorBigInt other) {
        if (this.isNegative && !other.isNegative) {
            return -1;
        } else if (!this.isNegative && other.isNegative) {
            return 1;
        }

        List<Integer> firstValue = this.valor();
        List<Integer> secondValue = other.valor();

        if (firstValue.size() > secondValue.size()) {
            return 1;
        } else if (firstValue.size() < secondValue.size()) {
            return -1;
        }

        for (int i = 0; i < firstValue.size(); i++) {
            if (firstValue.get(i) > secondValue.get(i)) {
                return 1;
            } else if (firstValue.get(i) < secondValue.get(i)) {
                return -1;
            }
        }

        return 0;
    }

    // Stein's Algorithm
    // gcd(x, 0) = x
    // gcd(2x, 2y) = 2*gcd(x, y)
    // gcd(x, 2y) = gcd(x, y)
    // Se x e y forem ímpares, gcd(x, y) = gcd(|x - y|, min(x, y))
    //
    // [4, 8] / [2, 4]  ← GCD([4, 8], [2, 4])
    // 1. this = [4, 8]  -> 48
    // 2. other = [2, 4] -> 24
    //
    // Remove sinais negativos (se houver)
    //
    // shift = 0
    //
    // 1. Enquanto os dois forem pares, divide por 2:
    // x % 2 == 0 && y % 2 == 0:
    // -> x = x / 2 = [2, 4] = 24
    // -> y = y / 2 = [1, 2] = 12
    // -> shift++ -> shift = 1
    //
    // 2. Enquanto X for par, divide por 2:
    // x já é par:
    // -> x = x / 2 = [6]
    //
    // 3. Laço principal (enquanto y != 0):
    //  a. Enquanto Y for par, divide por 2:
    //     y = [1, 2] = 12
    //     -> y = y / 2 = [6]
    //     -> y = y / 2 = [3]
    //
    //  b. Compara x e y:
    //     x = [6]
    //     y = [3]
    //     x > y -> troca:
    //     -> x = [3]
    //     -> y = [6]
    //
    //  c. Subtrai:
    //     y = y - x = [6] - [3] = [3]
    //
    //  d. y é ímpar -> próxima iteração
    //     x = [3]
    //     y = [3]
    //     x == y -> próxima subtração:
    //     -> y = y - x = [3] - [3] = [0]
    //
    // y == 0 -> fim do loop
    //
    // 4. Restaura fatores comuns de 2 removidos no início:
    // x = x * 2^shift = [3] * 2 = [6]
    //
    // Resultado final: GCD = [6]
    public ValorBigInt gcd(ValorBigInt other) {
        ValorBigInt x = this.clone();
        ValorBigInt y = other.clone();
        x.isNegative = false;
        y.isNegative = false;

        if (x.toString().equals("0")) {
            return y;
        }
        if (y.toString().equals("0")) {
            return x;
        }

        int shift = 0;

        while (x.isEven() && y.isEven()) {
            x = x.div(new ValorBigInt(List.of(2))).valor().get(0);
            y = y.div(new ValorBigInt(List.of(2))).valor().get(0);
            shift++;
        }

        while (x.isEven()) {
            x = x.div(new ValorBigInt(List.of(2))).valor().get(0);
        }

        while (!y.toString().equals("0")) {
            while (y.isEven()) {
                y = y.div(new ValorBigInt(List.of(2))).valor().get(0);
            }

            if (x.compareTo(y) > 0) {
                ValorBigInt temp = x;
                x = y;
                y = temp;
            }

            y = y.sub(x);
        }

        for (int i = 0; i < shift; i++) {
            x = x.multiply(new ValorBigInt(List.of(2)));
        }

        return x;
    }

    public boolean isEven() {
        return this.valor().get(this.valor().size() - 1) % 2 == 0;
    }

    // [0, 0, 1, 2, 3] -> [1, 2, 3]
    private List<Integer> removeTrailingZeros(List<Integer> list) {
        while (list.size() > 1 && list.get(0) == 0) {
            list.remove(0);
        }
        return list;
    }
}
