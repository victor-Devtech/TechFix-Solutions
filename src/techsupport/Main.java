package techsupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import techsupport.model.*;
import techsupport.service.*;
import techsupport.strategy.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        
        // Nossa equipe da TechFix Solutions
        List<Tecnico> equipe = new ArrayList<>();
        equipe.add(new Tecnico("T01", "Thiago (Senior)", NivelTecnico.SENIOR));
        equipe.add(new Tecnico("T02", "Anderson (Pleno)", NivelTecnico.PLENO));
        equipe.add(new Tecnico("T03", "Victor (Junior)", NivelTecnico.JUNIOR));

        GerenciadorFila fila = new GerenciadorFila(PoliticasFila.maiorPrioridade());
        int opcao = 0;
        
        while (opcao != 4) {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA TECHFIX SOLUTIONS  ");
            System.out.println("========================================");
            System.out.println("[1] - Cadastrar Nova OS");
            System.out.println("[2] - Ver Tamanho da Fila");
            System.out.println("[3] - Alocar Próxima OS para um Técnico");
            System.out.println("[4] - Sair");
            System.out.println("========================================");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do teclado

            if (opcao == 1) {
                System.out.println("\n--- CADASTRO DE NOVA ORDEM DE SERVIÇO ---");
                
                System.out.print("Qual o tipo da OS? (1 - Hardware / 2 - Software): ");
                int tipoOs = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer

                // 📝 AQUI O SISTEMA LÊ O QUE VOCÊ DIGITAR
                System.out.print("Digite a descrição do problema: ");
                String descricao = scanner.nextLine();

                System.out.print("Nível de Complexidade (1-BAIXA, 2-MEDIA, 3-ALTA): ");
                int opComplexidade = scanner.nextInt();
                ComplexidadeOS complexidade = ComplexidadeOS.BAIXA; // Padrão
                if (opComplexidade == 2) complexidade = ComplexidadeOS.MEDIA;
                else if (opComplexidade == 3) complexidade = ComplexidadeOS.ALTA;

                System.out.print("Nível de Gravidade (1 a 10): ");
                int gravidade = scanner.nextInt();

                System.out.print("Tempo estimado para resolução (em horas): ");
                double tempoEstimado = scanner.nextDouble();
                scanner.nextLine(); // Limpar o buffer

                OrdemServico novaOs;
                String id = "OS-" + (fila.tamanhoFila() + 1);
                
                // Perguntas específicas baseadas no tipo de OS (Herança)
                if (tipoOs == 1) {
                    System.out.print("Peça necessária para o conserto: ");
                    String peca = scanner.nextLine();
                    novaOs = new OSHardware(id, descricao, complexidade, gravidade, tempoEstimado, peca);
                } else {
                    System.out.print("Sistema Operacional do cliente (Ex: Windows 11): ");
                    String so = scanner.nextLine();
                    novaOs = new OSSoftware(id, descricao, complexidade, gravidade, tempoEstimado, so);
                }
                
                System.out.print("\nSalvando no banco de dados");
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(400); 
                    System.out.print(".");
                }
                
                fila.adicionarOS(novaOs);
                System.out.println("\n✅ OS " + id + " Cadastrada com Sucesso!");
                
            } else if (opcao == 2) {
                System.out.println("\n📊 O.S. aguardando na fila: " + fila.tamanhoFila());
                
            } else if (opcao == 3) {
                if (fila.filaVazia()) {
                    System.out.println("\n✅ A fila está vazia. Nenhum problema pendente!");
                } else {
                    OrdemServico osDaVez = fila.proximaOS(); 
                    System.out.println("\n🔍 Buscando técnico para: " + osDaVez.getDescricao());
                    
                    System.out.print("Analisando fila e cruzando dados");
                    for (int i = 0; i < 4; i++) {
                        Thread.sleep(500); 
                        System.out.print(".");
                    }
                    System.out.println();
                    
                    Optional<Tecnico> tecnicoIdeal = AlocadorOS.buscarTecnicoDisponivel(equipe, osDaVez);
                    
                    if (tecnicoIdeal.isPresent()) {
                        System.out.println("👷 Técnico ideal alocado: " + tecnicoIdeal.get().getNome());
                        tecnicoIdeal.get().setOcupado(true);
                        osDaVez.setStatus(StatusOS.EM_ANDAMENTO);
                    } else {
                        System.out.println("⚠️ Nenhum técnico disponível ou com nível suficiente para essa OS.");
                        fila.adicionarOS(osDaVez); 
                    }
                }
            }
        }
        
        System.out.println("\nSistema encerrado. Bom trabalho, equipe!");
        scanner.close();
    }
}