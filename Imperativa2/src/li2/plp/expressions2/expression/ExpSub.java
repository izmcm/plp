package li2.plp.expressions2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;
import li2.plp.expressions2.memory.AmbienteExecucao;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;

public class ExpSub extends ExpBinaria {
    public ExpSub(Expressao esq, Expressao dir) {
        super(esq, dir, "-");
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Valor valorEsq = getEsq().avaliar(amb);
        Valor valorDir = getDir().avaliar(amb);

        System.out.println("valorEsq: " + valorEsq);
        System.out.println("valorDir: " + valorDir);

        if (valorEsq instanceof ValorNumerico && valorDir instanceof ValorNumerico) {
            return ((ValorNumerico<?>) valorEsq).sub((ValorNumerico<?>) valorDir);
        }

        throw new UnsupportedOperationException("Operação de subtração não suportada para os tipos fornecidos.");
    }

    @Override
    protected boolean checaTipoElementoTerminal(AmbienteCompilacao ambiente)
            throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Tipo tipoEsq = getEsq().getTipo(ambiente);
        Tipo tipoDir = getDir().getTipo(ambiente);

        System.out.println("checaTipoElementoTerminal in ExpSub");
        System.out.println("tipoEsq: " + tipoEsq);
        System.out.println("tipoDir: " + tipoDir);

        return tipoEsq.eNumerico() && tipoDir.eNumerico();
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao ambiente) {
        Tipo tipoEsq = getEsq().getTipo(ambiente);
        Tipo tipoDir = getDir().getTipo(ambiente);

        System.out.println("tipoEsq: " + tipoEsq);
        System.out.println("tipoDir: " + tipoDir);

        if (tipoEsq.eIgual(tipoDir)) {
            return tipoEsq;
        }

        if (tipoEsq.eBigFraction() || tipoDir.eBigFraction()) {
            return TipoPrimitivo.BIGFRACTION;
        }

        if (tipoEsq.eBigInt() || tipoDir.eBigInt()) {
            return TipoPrimitivo.BIGINT;
        }

        throw new RuntimeException("Tipos incompatíveis na subtração: " + tipoEsq + " e " + tipoDir);
    }

    @Override
    public ExpBinaria clone() {
        return new ExpSub(esq.clone(), dir.clone());
    }
}