package techsupport.model;

// essa classe representa quem vai atender a fila.
// A regra diz que JUNIOR não pode pegar OS de complexidade ALTA. Lá nos requisitos do projeto
public class Tecnico {
    private String id;
    private String nome;
    private NivelTecnico nivel;
    private boolean ocupado; // Para checar disponibilidade (Regra do projeto)

    public Tecnico(String id, String nome, NivelTecnico nivel) {
        this.id = id;
        this.nome = nome;
        this.nivel = nivel;
        this.ocupado = false; 
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public NivelTecnico getNivel() { return nivel; }
    public boolean isOcupado() { return ocupado; }

    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
}