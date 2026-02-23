package techsupport.strategy;

import techsupport.model.OrdemServico;
import java.util.Comparator;

// Dupla, saca só: o professor exigiu o uso de Interface Funcional pra estratégia.
// A anotação @FunctionalInterface garante que essa interface tenha só UM método abstrato.
// O objetivo dela é devolver o Comparator que a PriorityQueue vai usar.
@FunctionalInterface
public interface EstrategiaEscalonamento {
    Comparator<OrdemServico> obterComparator();
}