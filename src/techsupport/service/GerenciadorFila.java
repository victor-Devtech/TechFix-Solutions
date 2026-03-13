package techsupport.service;

import techsupport.model.OrdemServico;
import techsupport.strategy.EstrategiaEscalonamento;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

// essa é a classe que vai segurar as O.S.
// Quem diz a regra é a "EstrategiaEscalonamento" passada no construtor (Design Pattern: Strategy).
public class GerenciadorFila {
    
    // Uso obrigatório de Queue /
    private Queue<OrdemServico> filaOrdemServico;

    public GerenciadorFila(EstrategiaEscalonamento estrategia) {
        // Inicializa a PriorityQueue
        this.filaOrdemServico = new PriorityQueue<>(estrategia.obterComparator());
    }

    public void adicionarOS(OrdemServico os) {
        this.filaOrdemServico.add(os);
    }

    // O método poll() tira e retorna o primeiro elemento da fila respeitando a prioridade atual
    public OrdemServico proximaOS() {
        return this.filaOrdemServico.poll(); 
    }

    public boolean filaVazia() {
        return this.filaOrdemServico.isEmpty();
    }

    public int tamanhoFila() {
        return this.filaOrdemServico.size();
    }

    public void envelhecerFila(int tempoAcrescimo) {
        if (filaVazia()) return;

        // Tira todas as OSs da fila para uma lista temporária
        java.util.List<OrdemServico> temporaria = new java.util.ArrayList<>(this.filaOrdemServico);
        this.filaOrdemServico.clear();
        //  Incrementa o tempo e devolve

        // add() a PriorityQueue recalcula as prioridades automaticamente
        for (OrdemServico os : temporaria) {
            os.incrementarTempoEspera(tempoAcrescimo);
            this.filaOrdemServico.add(os);
        }
    }
}