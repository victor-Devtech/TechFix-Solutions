package techsupport.model;

// Herda de OrdemServico. Trata de sistemas, resets de senha, etc.
public class OSSoftware extends OrdemServico {
    private String sistemaAfetado;

    public OSSoftware(String id, String descricao, ComplexidadeOS complexidade, int gravidade, double tempoEstimado, String sistemaAfetado) {
        super(id, descricao, complexidade, gravidade, tempoEstimado);
        this.sistemaAfetado = sistemaAfetado;
    }

    @Override
    public String obterDetalhesEquipamento() {
        return "Sistema/Software Afetado: " + this.sistemaAfetado;
    }
}