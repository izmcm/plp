package li2.plp.expressions2.expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>Valor</code> agrupa valores concretos e abstratos
*/

public interface Valor extends Expressao {
    /**
     * Realiza um cast para ValorBigInt.
     * 
     * @return o valor convertido para ValorBigInt.
     * @throws UnsupportedOperationException se o valor não puder ser convertido para BigInt.
     */
    default ValorBigInt toBigInt() {
        if (this instanceof ValorBigInt) {
            return (ValorBigInt) this;
        } else if (this instanceof ValorInteiro) {
            int valorInteiro = ((ValorInteiro) this).valor();
            String valorString = Integer.toString(valorInteiro);
            List<Integer> listaDigitos = new ArrayList<>();
            for (char c : valorString.toCharArray()) {
                listaDigitos.add(Character.getNumericValue(c));
            }
            return new ValorBigInt(listaDigitos);
        } else {
            throw new UnsupportedOperationException("Não é possível converter " + this.getClass().getSimpleName() + " para ValorBigInt.");
        }
    }
}