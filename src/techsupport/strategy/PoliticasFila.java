package techsupport.strategy;

import techsupport.model.OrdemServico;
import java.util.Comparator;

// Aqui ficam as políticas de escalonamento.
// Usamos lambdas pra criar os Comparators em uma linha só (requisito do projeto).
public class PoliticasFila {

    // 1. FIFO (Ordem de chegada)
    public static EstrategiaEscalonamento fifo() {
        return () -> Comparator.comparingInt(OrdemServico::getOrdemChegada);
    }

    // 2. Maior Prioridade Primeiro (Fórmula: Gravidade + Tempo Espera)
    public static EstrategiaEscalonamento maiorPrioridade() {
        return () -> Comparator.comparingInt(OrdemServico::calcularPesoPrioridade).reversed();
    }

    // 3. Menor Tempo Estimado (SJF - Shortest Job First)
    public static EstrategiaEscalonamento sjf() {
        return () -> Comparator.comparingDouble(OrdemServico::getTempoEstimado);
    }
}