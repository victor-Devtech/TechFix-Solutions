package techsupport.model;

// Herda de OrdemServico. Trata de problemas físicos (peças, servidores).
public class OSHardware extends OrdemServico {
    private String pecaNecessaria;

    public OSHardware(String id, String descricao, ComplexidadeOS complexidade, int gravidade, double tempoEstimado, String pecaNecessaria) {
        super(id, descricao, complexidade, gravidade, tempoEstimado);
        this.pecaNecessaria = pecaNecessaria;
    }

    @Override
    public String obterDetalhesEquipamento() {
        return "Troca de Peça/Hardware: " + this.pecaNecessaria;
    }
}