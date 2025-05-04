package li2.plp.expressions2.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;

public class ValorBigInt extends ValorConcreto<List<Integer>> {
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

    public ValorBigInt add(ValorBigInt outro) {
        List<Integer> resultado = new ArrayList<>();
        List<Integer> valor1 = this.valor();
        List<Integer> valor2 = outro.valor();

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

    public ValorBigInt multiply(ValorBigInt outro) {
        List<Integer> resultado = new ArrayList<>();
        List<Integer> valor1 = new ArrayList<>(this.valor());
        List<Integer> valor2 = new ArrayList<>(outro.valor());

        Collections.reverse(valor1);
        Collections.reverse(valor2);

        int[] produto = new int[valor1.size() + valor2.size()];

        for (int i = 0; i < valor1.size(); i++) {
            for (int j = 0; j < valor2.size(); j++) {
                produto[i + j] += valor1.get(i) * valor2.get(j);
                if (produto[i + j] >= 10) {
                    produto[i + j + 1] += produto[i + j] / 10;
                    produto[i + j] %= 10;
                }
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
}