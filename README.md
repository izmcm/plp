# Paradigmas de Linguagens Computacionais

- [Paradigmas de Linguagens Computacionais](#paradigmas-de-linguagens-computacionais)
  - [Equipe](#equipe)
  - [Contextualização](#contextualização)
  - [BNF](#bnf)
  - [Como usar](#como-usar)

## Equipe
Izabella Melo (imcm)

## Contextualização
Este projeto estende a linguagem Imperativa 2 com suporte nativo a dois novos tipos numéricos:
* BigInt: inteiros de tamanho arbitrário.
* BigFraction: frações compostas por dois BigInts.

E operações como soma, subtração, multiplicação e divisão com uso de BigInt/BigFraction

## BNF
Extensão da Linguagem Imperativa 2 disponível em https://www.cin.ufpe.br/~in1007/linguagens/Imperativa2/imperativa2.html

```
Programa ::= Comando

Comando ::= Atribuicao
          | ComandoDeclaracao
          | While
          | IfThenElse
          | IO
          | Comando ";" Comando
          | Skip 
          | ChamadaProcedimento

Skip ::= 

Atribuicao ::= Id ":=" Expressao

Expressao ::= Valor 
            | ExpUnaria 
            | ExpBinaria 
            | Id 

Valor ::= ValorConcreto

ValorConcreto ::= ValorInteiro 
                | ValorBooleano 
                | ValorString 
                | ValorBigInt 
                | ValorBigFraction

ValorBigInt ::= "bigint:" Digitos
ValorBigFraction ::= "bigfrac:" Digitos "/" Digitos

Digitos ::= [0-9]+

ExpUnaria ::= "-" Expressao 
            | "not" Expressao 
            | "length" Expressao

ExpBinaria ::= Expressao "+" Expressao
             | Expressao "-" Expressao
             | Expressao "and" Expressao
             | Expressao "or" Expressao
             | Expressao "==" Expressao
             | Expressao "++" Expressao

ComandoDeclaracao ::= "{" Declaracao ";" Comando "}"

Declaracao ::= DeclaracaoVariavel
             | DeclaracaoProcedimento
             | DeclaracaoComposta

DeclaracaoVariavel ::= "var" Id "=" Expressao 

DeclaracaoComposta ::= Declaracao "," Declaracao

DeclaracaoProcedimento ::= "proc" Id "(" [ ListaDeclaracaoParametro ] ")" "{" Comando "}"

ListaDeclaracaoParametro ::= Tipo Id 
                           | Tipo Id "," ListaDeclaracaoParametro

Tipo ::= "string" 
       | "int" 
       | "boolean"
       | "BigInt"
       | "BigFraction"

While ::= "while" Expressao "do" Comando

IfThenElse ::= "if" Expressao "then" Comando "else" Comando

IO ::= "write" "(" Expressao ")" 
    | "read" "(" Id ")" 

ChamadaProcedimento ::= "call" Id "(" [ ListaExpressao ] ")" 

ListaExpressao ::= Expressao 
                 | Expressao "," ListaExpressao
```

## Como usar
```
make run
```