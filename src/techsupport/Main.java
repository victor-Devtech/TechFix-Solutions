package techsupport;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import techsupport.model.*;
import techsupport.service.*;
import techsupport.strategy.*;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        // Equipe expandida: 2 técnicos de cada nível (melhoria apontada pelo professor)
        List<Tecnico> equipe = new ArrayList<>();
        equipe.add(new Tecnico("T01", "Thiago (Senior)",   NivelTecnico.SENIOR));
        equipe.add(new Tecnico("T02", "Maria (Senior)",    NivelTecnico.SENIOR));
        equipe.add(new Tecnico("T03", "Anderson (Pleno)",  NivelTecnico.PLENO));
        equipe.add(new Tecnico("T04", "Hellen (Pleno)",    NivelTecnico.PLENO));
        equipe.add(new Tecnico("T05", "Victor (Junior)",   NivelTecnico.JUNIOR));
        equipe.add(new Tecnico("T06", "Lis (Junior)",      NivelTecnico.JUNIOR));

        GerenciadorFila fila = new GerenciadorFila(PoliticasFila.maiorPrioridade());
        int opcao = 0;

        // Atualizado para 7 por causa da nova opção de listar equipe
        while (opcao != 7) {
            System.out.println("\n========================================");
            System.out.println("       SISTEMA TECHFIX SOLUTIONS        ");
            System.out.println("========================================");
            System.out.println("[1] - Cadastrar Nova OS");
            System.out.println("[2] - Ver Tamanho da Fila");
            System.out.println("[3] - Alocar Próxima OS para um Técnico");
            System.out.println("[4] - Concluir Atendimento (Liberar Técnico)");
            System.out.println("[5] - Gerar Dados de Teste");
            System.out.println("[6] - Ver Status da Equipe");
            System.out.println("[7] - Sair");
            System.out.println("========================================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\n⚠️ ERRO: Entrada inválida! Por favor, digite apenas números de 1 a 7.");
                continue;
            }

            if (opcao == 1) {
                System.out.println("\n--- CADASTRO DE NOVA ORDEM DE SERVIÇO ---");
                try {
                    System.out.print("Qual o tipo da OS? (1 - Hardware / 2 - Software): ");
                    int tipoOs = Integer.parseInt(scanner.nextLine());

                    System.out.print("Digite a descrição do problema: ");
                    String descricao = scanner.nextLine();

                    System.out.print("Nível de Complexidade (1-BAIXA, 2-MEDIA, 3-ALTA): ");
                    int opComplexidade = Integer.parseInt(scanner.nextLine());
                    ComplexidadeOS complexidade = ComplexidadeOS.BAIXA;
                    if (opComplexidade == 2) complexidade = ComplexidadeOS.MEDIA;
                    else if (opComplexidade == 3) complexidade = ComplexidadeOS.ALTA;

                    System.out.print("Nível de Gravidade (1 a 10): ");
                    int gravidade = Integer.parseInt(scanner.nextLine());

                    if (gravidade < 1 || gravidade > 10) {
                        System.out.println("\n⚠️ ERRO: Gravidade deve ser entre 1 e 10. Cadastro cancelado.");
                    } else {
                        System.out.print("Tempo estimado para resolução (em horas): ");
                        double tempoEstimado = Double.parseDouble(scanner.nextLine());

                        OrdemServico novaOs;
                        String id = OrdemServico.gerarProximoID();

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
                    }

                } catch (NumberFormatException e) {
                    System.out.println("\n⚠️ ERRO: Você digitou um valor inválido (esperado número). Cadastro cancelado.");
                }

            } else if (opcao == 2) {
                System.out.println("\n📊 O tamanho atual da fila é: " + fila.tamanhoFila() + " OS(s) aguardando.");

            } else if (opcao == 3) {
                if (fila.filaVazia()) {
                    System.out.println("\n✅ A fila está vazia. Nenhum problema pendente!");
                } else {
                    System.out.print("\nAnalisando fila e cruzando dados");
                    for (int i = 0; i < 4; i++) {
                        Thread.sleep(500);
                        System.out.print(".");
                    }
                    System.out.println();

                    List<OrdemServico> candidatas = new ArrayList<>();
                    while (!fila.filaVazia()) {
                        candidatas.add(fila.proximaOS());
                    }

                    OrdemServico osAlocada = null;
                    Tecnico tecnicoEscolhido = null;

                    for (OrdemServico candidata : candidatas) {
                        Optional<Tecnico> tecnicoIdeal = AlocadorOS.buscarTecnicoDisponivel(equipe, candidata);
                        if (tecnicoIdeal.isPresent()) {
                            osAlocada = candidata;
                            tecnicoEscolhido = tecnicoIdeal.get();
                            break;
                        }
                    }

                    if (osAlocada != null) {
                        candidatas.remove(osAlocada);
                        for (OrdemServico restante : candidatas) {
                            fila.adicionarOS(restante);
                        }

                        System.out.println("🔍 OS selecionada: " + osAlocada.getDescricao());
                        System.out.println("👷 Técnico ideal alocado: " + tecnicoEscolhido.getNome());
                        tecnicoEscolhido.setOsAtual(osAlocada);
                        tecnicoEscolhido.setOcupado(true);
                        osAlocada.setStatus(StatusOS.EM_ANDAMENTO);

                        fila.envelhecerFila(1);
                        System.out.println("⏳ O tempo de espera das OSs restantes foi atualizado!");
                    } else {
                        for (OrdemServico restante : candidatas) {
                            fila.adicionarOS(restante);
                        }
                        System.out.println("⚠️ Nenhum técnico disponível ou com nível suficiente para qualquer OS na fila.");
                    }
                }

            } else if (opcao == 4) {
                System.out.println("\n--- CONCLUIR ATENDIMENTO ---");
                List<Tecnico> ocupados = equipe.stream().filter(Tecnico::isOcupado).toList();

                if (ocupados.isEmpty()) {
                    System.out.println("✅ Todos os técnicos já estão livres no momento!");
                } else {
                    System.out.println("Técnicos em atendimento no momento:");
                    for (Tecnico t : ocupados) {
                        System.out.println(" - ID: " + t.getId() + " | Nome: " + t.getNome());
                    }
                    System.out.print("\nDigite o ID do técnico que concluiu a OS (Ex: T01): ");
                    String idTecnico = scanner.nextLine();

                    Optional<Tecnico> tecnicoPraLiberar = equipe.stream()
                            .filter(t -> t.getId().equalsIgnoreCase(idTecnico))
                            .findFirst();

                    if (tecnicoPraLiberar.isPresent() && tecnicoPraLiberar.get().isOcupado()) {
                        Tecnico tecnico = tecnicoPraLiberar.get();
                        if (tecnico.getOsAtual() != null) {
                            tecnico.getOsAtual().setStatus(StatusOS.CONCLUIDA);
                        }
                        tecnico.setOsAtual(null);
                        tecnico.setOcupado(false);
                        System.out.println("🎉 Atendimento concluído! O técnico " + tecnicoPraLiberar.get().getNome() + " está livre.");
                    } else {
                        System.out.println("⚠️ Técnico não encontrado ou já está livre.");
                    }
                }

            } else if (opcao == 5) {
                carregarDadosDemonstracao(fila);

            } else if (opcao == 6) {
                // Lista a equipe, solicitação do professor
                System.out.println("\n--- STATUS DA EQUIPE ---");
                System.out.println("----------------------------------------");
                equipe.stream()
                        .sorted(Comparator.comparing(Tecnico::getNivel))
                        .forEach(t -> System.out.println(
                                " " + t.getNivel()
                                + " | " + t.getId()
                                + " | " + t.getNome()
                                + " | " + (t.isOcupado() ? "OCUPADO" : "LIVRE")
                        ));
                System.out.println("----------------------------------------");

                // Contadores usando stream e filter
                long livres  = equipe.stream().filter(t -> !t.isOcupado()).count();
                long ocupados = equipe.stream().filter(Tecnico::isOcupado).count();
                System.out.println("Total: " + equipe.size()
                        + " técnicos | " + livres + " livres"
                        + " | " + ocupados + " ocupados");
            }
        }

        System.out.println("\nSistema encerrado. Bom trabalho, equipe!");
        scanner.close();
    }

    private static void carregarDadosDemonstracao(GerenciadorFila fila) {
        System.out.println("\n⏳ Carregando dados de demonstração...");

        fila.adicionarOS(new OSHardware(OrdemServico.gerarProximoID(),
                "Servidor principal não inicializa",
                ComplexidadeOS.ALTA, 10, 4.0, "Fonte ATX 750W"));

        fila.adicionarOS(new OSSoftware(OrdemServico.gerarProximoID(),
                "Usuário sem acesso ao sistema ERP",
                ComplexidadeOS.BAIXA, 3, 0.5, "Windows 11"));

        fila.adicionarOS(new OSHardware(OrdemServico.gerarProximoID(),
                "Notebook da diretoria com tela quebrada",
                ComplexidadeOS.MEDIA, 7, 2.0, "Display LED 15.6''"));

        fila.adicionarOS(new OSSoftware(OrdemServico.gerarProximoID(),
                "Ransomware detectado em estação de trabalho",
                ComplexidadeOS.ALTA, 10, 6.0, "Windows 10"));

        fila.adicionarOS(new OSSoftware(OrdemServico.gerarProximoID(),
                "Reset de senha do e-mail corporativo",
                ComplexidadeOS.BAIXA, 2, 0.3, "Outlook 365"));

        fila.adicionarOS(new OSHardware(OrdemServico.gerarProximoID(),
                "Switch de rede com falha intermitente",
                ComplexidadeOS.ALTA, 8, 3.5, "Switch Cisco 24P"));

        System.out.println("✅ OSs de demonstração carregadas com sucesso!");
        System.out.println("(2x ALTA, 2x MEDIA, 2x BAIXA... Totalizando 6 Os!)");
    }
}