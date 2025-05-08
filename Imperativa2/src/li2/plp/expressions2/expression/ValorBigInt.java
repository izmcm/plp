package li2.plp.expressions2.expression;

import java.util.ArrayList;
import java.util.Arrays;
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
    // -> 9 + 2 + 0 = 1 (carry = 1 & resultado = [1, 0, 0])
    // -> 5 + 3 + 1 = 9 (carry = 0 & resultado = [1, 9, 0])
    // -> 3 + 1 + 0 = 4 (carry = 0 & resultado = [1, 9, 4])
    // 2. Inverter lista
    // resultado = [4, 9, 1]
    public ValorBigInt sum(ValorBigInt outro) {
        System.out.println("[bigint] [sum] " + this + " + " + outro);

        List<Integer> resultado = new ArrayList<>();
        List<Integer> valor1 = this.valor();
        List<Integer> valor2 = outro.valor();

        System.out.println("valor1: " + valor1);
        System.out.println("valor2: " + valor2);

        Collections.reverse(valor1);
        Collections.reverse(valor2);

        int carry = 0;
        int maxLength = Math.max(valor1.size(), valor2.size());

        for (int i = 0; i < maxLength; i++) {
            int digito1 = i < valor1.size() ? valor1.get(i) : 0;
            int digito2 = i < valor2.size() ? valor2.get(i) : 0;

            int soma = digito1 + digito2 + carry;
            resultado.add(soma % 10);
            carry = soma / 10;
        }

        if (carry > 0) {
            resultado.add(carry);
        }

        Collections.reverse(resultado);

        return new ValorBigInt(resultado);
    }

    // [4, 9, 1] - [3, 5, 9]
    // 1. Inverter listas
    // [1, 9, 4]
    // - [9, 5, 3]
    // -------------
    // -> 1 - 9 - 0 = -8
    // if(<0) 8 + 10 = 2 (borrow = 1 & resultado = [2, 0, 0])
    // -> 9 - 5 - 1 = 3
    // if(>0) (borrow = 0 & resultado = [2, 3, 0])
    // -> 4 - 3 - 0 = 1
    // if(>0) (borrow = 0 & resultado = [2, 3, 1])
    // 2. Inverter lista
    // 3. Remover zeros à esquerda
    // resultado = [1, 3, 2]
    public ValorBigInt sub(ValorBigInt outro) {
        System.out.println("[bigint] [sub] " + this + " - " + outro);
        List<Integer> resultado = new ArrayList<>();
        List<Integer> valor1 = new ArrayList<>(this.valor());
        List<Integer> valor2 = new ArrayList<>(outro.valor());

        System.out.println("valor1: " + valor1);
        System.out.println("valor2: " + valor2);

        if (valor1.size() < valor2.size() ||
                (valor1.size() == valor2.size() && valor1.toString().compareTo(valor2.toString()) < 0)) {
            throw new IllegalArgumentException("Subtração resultaria em um número negativo, o que não é suportado.");
        }

        Collections.reverse(valor1);
        Collections.reverse(valor2);

        int borrow = 0;

        for (int i = 0; i < valor1.size(); i++) {
            int digito1 = valor1.get(i);
            int digito2 = i < valor2.size() ? valor2.get(i) : 0;

            int subtracao = digito1 - digito2 - borrow;

            if (subtracao < 0) {
                subtracao += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }

            resultado.add(subtracao);
        }

        Collections.reverse(resultado);

        resultado = removeTrailingZeros(resultado);

        return new ValorBigInt(resultado);
    }

    // [4, 9] * [1, 2]
    // 1. Inverter listas
    // [9, 4]
    // * [2, 1]
    // -------------
    // produto = [0, 0, 0, 0]
    // -> Para cada índice em valor1 (i) e valor2 (j), calcular produto[i + j]:
    // - i = 0, j = 0 -> 9 * 2 = 18 (produto = [8, 1, 0, 0, 0, 0])
    // - i = 0, j = 1 -> 9 * 1 = 9 (produto = [8, 0, 1, 0, 0, 0])
    // - i = 1, j = 0 -> 4 * 2 = 8 (produto = [8, 8, 1, 0, 0, 0])
    // - i = 1, j = 1 -> 4 * 1 = 4 + 1 (produto = [8, 8, 5, 0, 0, 0])
    // -------------
    // produto = [8, 8, 5, 0, 0, 0]
    // 2. Remover zeros à esquerda (se houver)
    // 3. Inverter lista
    // resultado = [5, 8, 8]
    public ValorBigInt multiply(ValorBigInt outro) {
        System.out.println("[bigint] [mul] " + this + " * " + outro);

        List<Integer> resultado = new ArrayList<>();
        List<Integer> valor1 = new ArrayList<>(this.valor());
        List<Integer> valor2 = new ArrayList<>(outro.valor());

        Collections.reverse(valor1);
        Collections.reverse(valor2);

        int[] produto = new int[valor1.size() + valor2.size()];

        for (int i = 0; i < valor1.size(); i++) {
            for (int j = 0; j < valor2.size(); j++) {
                produto[i + j] += valor1.get(i) * valor2.get(j);
                produto[i + j + 1] += produto[i + j] / 10;
                produto[i + j] %= 10;
            }
        }

        boolean leadingZero = true;
        for (int i = produto.length - 1; i >= 0; i--) {
            if (produto[i] == 0 && leadingZero) {
                continue;
            }
            leadingZero = false;
            resultado.add(produto[i]);
        }

        if (resultado.isEmpty()) {
            resultado.add(0);
        }

        return new ValorBigInt(resultado);
    }

    public ValorBigFraction div(ValorBigInt outro) {
        System.out.println("[bigint] [div] " + this + " / " + outro);

        if (outro.toString().equals("0")) {
            throw new ArithmeticException("Divisão por zero não é permitida.");
        }

        // se o divisor for maior que o dividendo
        if (this.compareTo(outro) < 0) {
            return new ValorBigFraction(List.of(this, outro));
        }

        List<Integer> resultado = new ArrayList<>();
        List<Integer> resto = new ArrayList<>();

        List<Integer> valor1 = new ArrayList<>(this.valor());
        for (int i = 0; i < valor1.size(); i++) {
            resto.add(valor1.get(i));

            resto = removeTrailingZeros(resto);

            int quociente = 0;
            ValorBigInt restoBigInt = new ValorBigInt(new ArrayList<>(resto));

            while (restoBigInt.compareTo(outro) >= 0) {
                restoBigInt = restoBigInt.sub(outro);
                quociente++;
            }

            resultado.add(quociente);
            resto = restoBigInt.valor();
        }

        resultado = removeTrailingZeros(resultado);

        // Retorna uma fração se houver resto
        if (!resto.isEmpty() && !new ValorBigInt(resto).toString().equals("0")) {
            return new ValorBigFraction(List.of(new ValorBigInt(valor1), outro));
        }

        return new ValorBigFraction(
            List.of(
                new ValorBigInt(resultado), 
                new ValorBigInt(Collections.singletonList(1))
            )
        );
    }

    // Retorna
    // 0 -> se os dois valores forem iguais
    // 1 -> se valor1 for maior que valor2
    // -1 -> se valor 1 for menor que valor2
    public int compareTo(ValorBigInt outro) {
        List<Integer> valor1 = this.valor();
        List<Integer> valor2 = outro.valor();

        if (valor1.size() > valor2.size()) {
            return 1;
        } else if (valor1.size() < valor2.size()) {
            return -1;
        }

        for (int i = 0; i < valor1.size(); i++) {
            if (valor1.get(i) > valor2.get(i)) {
                return 1;
            } else if (valor1.get(i) < valor2.get(i)) {
                return -1;
            }
        }

        return 0;
    }

    // [0, 0, 1, 2, 3] -> [1, 2, 3]
    private List<Integer> removeTrailingZeros(List<Integer> list) {
        while (list.size() > 1 && list.get(0) == 0) {
            list.remove(0);
        }
        return list;
    }
}