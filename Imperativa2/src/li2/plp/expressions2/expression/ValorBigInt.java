package li2.plp.expressions2.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;

public class ValorBigInt extends ValorNumerico<List<Integer>> {
    public ValorBigInt(List<Integer> valor) {
        super(valor);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int digit : this.valor()) {
            sb.append(digit);
        }

        return sb.toString();
    }

    public Tipo getTipo(AmbienteCompilacao amb) {
        return TipoPrimitivo.BIGINT;
    }

    @Override
    public ValorBigInt clone() {
        return new ValorBigInt(new ArrayList<>(this.valor()));
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
        System.out.println("[bigint] [sum] " + this + " + " + other);

        List<Integer> result = new ArrayList<>();
        List<Integer> firstValue = this.valor();
        List<Integer> secondValue = other.valor();

        System.out.println("firstValue: " + firstValue);
        System.out.println("secondValue: " + secondValue);

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

        return new ValorBigInt(result);
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
        System.out.println("[bigint] [sub] " + this + " - " + other);
        List<Integer> result = new ArrayList<>();
        List<Integer> firstValue = new ArrayList<>(this.valor());
        List<Integer> secondValue = new ArrayList<>(other.valor());

        System.out.println("firstValue: " + firstValue);
        System.out.println("secondValue: " + secondValue);

        if (firstValue.size() < secondValue.size() || (firstValue.size() == secondValue.size() &&
                firstValue.toString().compareTo(secondValue.toString()) < 0)) {
            throw new IllegalArgumentException("Subtração resultaria em um número negativo, o que não é suportado.");
        }

        Collections.reverse(firstValue);
        Collections.reverse(secondValue);

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

        return new ValorBigInt(result);
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
        System.out.println("[bigint] [mul] " + this + " * " + other);

        List<Integer> result = new ArrayList<>();
        List<Integer> firstValue = new ArrayList<>(this.valor());
        List<Integer> secondValue = new ArrayList<>(other.valor());

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

        return new ValorBigInt(result);
    }

    // [6, 0] / [1, 2]
    // 1. Checa se divisor é zero
    // 2. Checa se divisor é maior que dividendo
    // -----------
    // this = [6, 0]
    // other = [1, 2]
    //
    // remaider = [6, 0]
    // while (remaider >= other) [6, 0] >= [1, 2]?
    // -> [6, 0] - [1, 2] = [4, 8] | quocient = 1
    // -> [4, 8] >= [1, 2]?
    // -> [4, 8] - [1, 2] = [3, 6] | quocient = 2
    // -> [3, 6] >= [1, 2]?
    // -> [3, 6] - [1, 2] = [2, 4] | quocient = 3
    // -> [2, 4] >= [1, 2]?
    // -> [2, 4] - [1, 2] = [1, 2] | quocient = 4
    // -> [1, 2] >= [1, 2]?
    // -> [1, 2] - [1, 2] = [0] | quocient = 5
    // -> [0] >= [1, 2]?
    // quotient = 5
    // remainder = [0]
    public ValorBigFraction div(ValorBigInt other) {
        System.out.println("[bigint] [div] " + this + " / " + other);

        if (other.toString().equals("0")) {
            throw new ArithmeticException("Divisão por zero não é permitida.");
        }

        // se divisor for maior que dividendo
        if (this.compareTo(other) < 0) {
            return new ValorBigFraction(List.of(this, other));
        }

        ValorBigInt remainder = this.clone();
        ValorBigInt divisor = other.clone();

        ValorBigInt quocient = new ValorBigInt(Collections.singletonList(0));
        // conta quantas vezes o divisor cabe no dividendo
        while (remainder.compareTo(divisor) >= 0) {
            remainder = remainder.sub(divisor);
            quocient = quocient.sum(new ValorBigInt(Collections.singletonList(1)));
        }

        // retorna uma fração se houver resto
        if (!remainder.toString().equals("0")) {
            ValorBigFraction result = new ValorBigFraction(List.of(this, other));
            return result.simplify();
        }

        return new ValorBigFraction(
                List.of(
                        quocient,
                        new ValorBigInt(Collections.singletonList(1))));
    }

    // Retorna
    // 0 -> se os dois valores forem iguais
    // 1 -> se firstValue for maior que secondValue
    // -1 -> se valor 1 for menor que secondValue
    public int compareTo(ValorBigInt other) {
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

    public ValorBigInt gcd(ValorBigInt other) {
        ValorBigInt a = this;
        ValorBigInt b = other;

        if (this.compareTo(other) < 0) {
            ValorBigInt temp = a;
            a = b;
            b = temp;
        }

        while (!b.toString().equals("0")) {
            ValorBigInt remainder = a.mod(b);
            a = b;
            b = remainder;
        }

        return a;
    }

    public ValorBigInt mod(ValorBigInt other) {
        if (other.toString().equals("0")) {
            throw new ArithmeticException("Divisão por zero não é permitida.");
        }

        ValorBigInt remainder = this.clone();

        while (remainder.compareTo(other) >= 0) {
            remainder = remainder.sub(other);
        }

        return remainder;
    }

    // [0, 0, 1, 2, 3] -> [1, 2, 3]
    private List<Integer> removeTrailingZeros(List<Integer> list) {
        while (list.size() > 1 && list.get(0) == 0) {
            list.remove(0);
        }
        return list;
    }
}