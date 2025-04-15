package applet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

import li2.plp.imperative2.memory.ContextoExecucaoImperativa2;
import li2.plp.imperative2.parser.Imp2Parser;

public class MultiInterpretador {

	private static final int IMP2 = 0;

	private JTextArea messageBoard;

	private Imp2Parser imp2Parser = null;

	public MultiInterpretador(JTextArea textAreaMensagens) {
		super();
		messageBoard = textAreaMensagens;
	}

	public void interpretarCodigo(String sourceCode, String listaEntrada,
			int selectedIndex) {
		try {
			ByteArrayInputStream fis = new ByteArrayInputStream(sourceCode
					.getBytes());

			switch (selectedIndex) {
			case IMP2:
				interpretarImp2(fis, listaEntrada);
				break;
			default:
				System.err.println("Error");
				break;
			}
		} catch (Exception e1) {
			messageBoard.setText(e1.getMessage());
			e1.printStackTrace();
		} catch (Throwable t) {
			messageBoard.setText(t.getMessage());
			t.printStackTrace();
		}

	}

	private void interpretarImp2(InputStream fis, String entradaStr)
			throws Exception {
		li2.plp.imperative2.Programa prog;
		if (imp2Parser == null) {
			imp2Parser = new Imp2Parser(fis);
		} else {
			Imp2Parser.ReInit(fis);
		}

		prog = Imp2Parser.Input();

		messageBoard.setText("sintaxe verificada com sucesso!\n");
		li2.plp.imperative1.memory.ListaValor entrada = obterListaEntradaImp2(entradaStr);
		if (prog.checaTipo(new li2.plp.imperative1.memory.ContextoCompilacaoImperativa(entrada))) {
			messageBoard.append("resultado = "
					+ prog.executar(new li2.plp.imperative2.memory.ContextoExecucaoImperativa2(entrada))
							.toString());
		} else {
			messageBoard.append("erro de tipos!");
		}
	}

	@SuppressWarnings("unchecked")
	private li2.plp.imperative1.memory.ListaValor obterListaEntradaImp2(String texto) {
		@SuppressWarnings("rawtypes")
		List valores = new LinkedList<li2.plp.expressions2.expression.ValorConcreto>();
		li2.plp.imperative1.memory.ListaValor entrada = new li2.plp.imperative1.memory.ListaValor();
		StringTokenizer parser = new StringTokenizer(texto);

		while (parser.hasMoreTokens()) {
			String parametro = parser.nextToken();
			
			try {
				Integer inteiro = Integer.valueOf(parametro);
				valores.add(new li2.plp.expressions2.expression.ValorInteiro(inteiro.intValue()));
				continue;
			} catch (NumberFormatException e) {

			}

			if (parametro.equalsIgnoreCase("true")
					|| parametro.equalsIgnoreCase("false")) {
				Boolean booleano = Boolean.valueOf(parametro);
				valores.add(new li2.plp.expressions2.expression.ValorBooleano(booleano.booleanValue()));
			} else {
				valores.add(new li2.plp.expressions2.expression.ValorString(parametro));
			}
		}
		entrada = Imp2Parser.criaListaValor(valores);
		return entrada;
	}
}