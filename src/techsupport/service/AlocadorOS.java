package techsupport.service;

import techsupport.model.OrdemServico;
import techsupport.model.Tecnico;
import techsupport.model.NivelTecnico;
import techsupport.model.ComplexidadeOS;

import java.util.List;
import java.util.Optional;

public class AlocadorOS {

    
    // Usamos .stream() para filtrar os técnicos sem precisar de laços 'for'.
    public static Optional<Tecnico> buscarTecnicoDisponivel(List<Tecnico> tecnicos, OrdemServico os) {
        return tecnicos.stream()
                .filter(t -> !t.isOcupado()) // 1º Filtro: O técnico precisa estar livre
                .filter(t -> podeAtender(t, os.getComplexidade())) // 2º Filtro: Aplica a regra de nível
                .findFirst(); // Retorna o primeiro que passar nos filtros
    }

    // Regra de negócio exigida
    private static boolean podeAtender(Tecnico tecnico, ComplexidadeOS complexidade) {
        // Júnior não dá conta de OS de nvel alto
        if (tecnico.getNivel() == NivelTecnico.JUNIOR && complexidade == ComplexidadeOS.ALTA) {
            return false;
        }
        return true;
    }
}