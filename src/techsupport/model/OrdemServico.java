package techsupport.model;

// Transformamos em classe abstrata para aplicar Herança.
// Ela guarda o peso e o tempo estimado, essenciais para a Fila de Prioridade (PriorityQueue).
public abstract class OrdemServico {
    private static int contadorGlobal = 0;
    private String id;
    private String descricao;
    private ComplexidadeOS complexidade;
    private int gravidade; 
    private int tempoEspera; 
    private double tempoEstimado; 
    private StatusOS status;
    private int ordemChegada;

    public OrdemServico(String id, String descricao, ComplexidadeOS complexidade, int gravidade, double tempoEstimado) {
        this.id = id;
        this.descricao = descricao;
        this.complexidade = complexidade;
        this.gravidade = gravidade;
        this.tempoEstimado = tempoEstimado;
        this.tempoEspera = 0; 
        this.status = StatusOS.PENDENTE;
        this.ordemChegada = ++contadorGlobal;
    }

    public static String gerarProximoID(){
        return "OS-" + (contadorGlobal + 1);
    }

    // Método abstrato: Força as classes filhas a implementarem sua própria regra. (Polimorfismo!)
    public abstract String obterDetalhesEquipamento();

    // A fórmula do peso (Gravidade + Tempo de Espera) que o Thiago pediu no PDF
    public int calcularPesoPrioridade() {
        return this.gravidade + this.tempoEspera;
    }

    public void incrementarTempoEspera(int tempo) {
        this.tempoEspera += tempo;
    }

    public void setStatus(StatusOS status) {
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getDescricao() { return descricao; }
    public ComplexidadeOS getComplexidade() { return complexidade; }
    public int getGravidade() { return gravidade; }
    public int getTempoEspera() { return tempoEspera; }
    public double getTempoEstimado() { return tempoEstimado; }
    public StatusOS getStatus() { return status; }
    public int getOrdemChegada() { return ordemChegada; }
}