# Protocolo de Decisoes - Seldon Fleet

## 1. Regra de Classificacao Adotada
A classificacao de saude da frota baseia-se primariamente no Dreno Relativo (%/h), normalizado a partir da capacidade da bateria e do tempo de tela ativa, cruzado com metricas de estabilidade (crashCount7d) e fluidez (jankFramesPct).
A precedencia avalia primeiro estados INDETERMINADO, seguidos de CRITICO e ATENCAO, recaindo em OK caso nenhuma condicao de alerta seja atingida.

- INDETERMINADO: Dispositivos com capacidade de bateria invalida (<= 0 ou ausente), tempo de tela nulo/negativo (<= 0), ou unidade de energia em mWh sem tensao nominal informada.
- CRITICO: Dreno relativo >= 12%/h, falhas recentes >= 3 ou jank de quadros >= 15%.
- ATENCAO: Dreno relativo >= 7%/h, falhas recentes >= 1 ou jank de quadros >= 8%.
- OK: Nenhuma das condicoes anteriores satisfeita.

## 2. Tratamento de Defeitos (Dados Reais)
- Defeito 1 (Unidade de Energia Inconsistente): Registros que informam o consumo em mWh sao convertidos obrigatoriamente para mAh atraves da divisao pelo campo nominalVoltage antes de qualquer calculo de dreno ou agregacao. Isso previne distorcoes de magnitude de aproximadamente 3.85x.
- Defeito 2 (Registros Incompletos / Divisor Nulo): Dispositivos com capacidade de bateria ou tempo de tela nulos recebem tratamento explicito no dominio, retornando o estado INDETERMINADO de forma controlada. Isso elimina completamente o risco de excecoes de divisao por zero, exibicao de valores NaN, Infinity ou encerramento abrupto do aplicativo.

## 3. Ameaca a Validade
O calculo do dreno relativo assume linearidade e considera que todo o consumo energetico registrado ocorreu estritamente durante o periodo com a tela ligada (screenOnMinutes). Em cenarios reais onde servicos pesados em primeiro plano ou em segundo plano executam com a tela desligada, a metrica pode superestimar o dreno por hora de tela ativa, gerando potenciais falsos positivos para o status CRITICO.