public class Scheduler {

    private static final int limiteAltaPrioridade = 5;
    private static final String recursoDisco = "DISCO";

    private ListaDeProcessos listaAltaPrioridade;
    private ListaDeProcessos listaMediaPrioridade;
    private ListaDeProcessos listaBaixaPrioridade;
    private ListaDeProcessos listaBloqueados;

    private int cicloAtual;
    private int processosFinalizados;
    private int contadorCiclosAltaPrioridade;

    private int totalBloqueios;
    private int totalDesbloqueios;

    public Scheduler() {
        this.listaAltaPrioridade = new ListaDeProcessos();
        this.listaMediaPrioridade = new ListaDeProcessos();
        this.listaBaixaPrioridade = new ListaDeProcessos();
        this.listaBloqueados = new ListaDeProcessos();
        this.cicloAtual = 0;
        this.processosFinalizados = 0;
        this.contadorCiclosAltaPrioridade = 0;
        this.totalBloqueios = 0;
        this.totalDesbloqueios = 0;
    }

    public int getCicloAtual() {
        return cicloAtual;
    }

    public int getProcessosFinalizados() {
        return processosFinalizados;
    }

    public int getTotalBloqueios() {
        return totalBloqueios;
    }

    public int getTotalDesbloqueios() {
        return totalDesbloqueios;
    }

    public boolean sistemaVazio() {
        return !temProcessosProntos() && listaBloqueados.isEmpty();
    }

    public void adicionarProcesso(Processo processo) {
        ListaDeProcessos listaDestino = obterListaPorPrioridade(processo.getPrioridade());
        if (listaDestino != null) {
            listaDestino.adicionar(processo);
            System.out.println("✓ Processo adicionado: " + processo);
        } else {
            System.err.println("Prioridade invalida: " + processo.getPrioridade());
        }
    }

    private ListaDeProcessos obterListaPorPrioridade(int prioridade) {
        switch (prioridade) {
            case 1: return listaAltaPrioridade;
            case 2: return listaMediaPrioridade;
            case 3: return listaBaixaPrioridade;
            default: return null;
        }
    }

    public boolean temProcessosProntos() {
        return !listaAltaPrioridade.isEmpty() ||
                !listaMediaPrioridade.isEmpty() ||
                !listaBaixaPrioridade.isEmpty();
    }

    public void exibirEstado() {
        System.out.println(" Estado das Listas ");
        System.out.println("Alta: " + listaAltaPrioridade);
        System.out.println("Media: " + listaMediaPrioridade);
        System.out.println("Baixa: " + listaBaixaPrioridade);
        System.out.println("Bloqueados: " + listaBloqueados);
        System.out.println("Contador anti inanicao: " + contadorCiclosAltaPrioridade + "/" + limiteAltaPrioridade);
    }

    private void desbloquearProcesso() {
        if (!listaBloqueados.isEmpty()) {
            Processo processo = listaBloqueados.removerPrimeiro();
            totalDesbloqueios++;

            System.out.println("🔓 DESBLOQUEANDO: " + processo);

            ListaDeProcessos listaDestino = obterListaPorPrioridade(processo.getPrioridade());
            if (listaDestino != null) {
                listaDestino.adicionar(processo);
                System.out.println("   → Retornou a lista prioridade " + processo.getPrioridade());
            }
        }
    }

    private void bloquearProcesso(Processo processo) {
        totalBloqueios++;
        System.out.println("🔒 BLOQUEANDO: " + processo + " (precisa " + processo.getRecursoNecessario() + ")");

        processo.marcarRecursoPedido();
        listaBloqueados.adicionar(processo);

        System.out.println("   → Movido para lista de bloqueados");
    }

    private Processo escolherProximoProcesso() {
        if (contadorCiclosAltaPrioridade >= limiteAltaPrioridade) {
            System.out.println("⚠️ ANTI INANICAO ATIVADA! Priorizando menor prioridade...");

            if (!listaMediaPrioridade.isEmpty()) {
                contadorCiclosAltaPrioridade = 0;
                System.out.println("   → Escolhido media por anti inanicao");
                return listaMediaPrioridade.removerPrimeiro();
            }

            if (!listaBaixaPrioridade.isEmpty()) {
                contadorCiclosAltaPrioridade = 0;
                System.out.println("   → Escolhido baixa por anti inanicao");
                return listaBaixaPrioridade.removerPrimeiro();
            }

            System.out.println("   → Apenas alta prioridade disponível");
        }

        if (!listaAltaPrioridade.isEmpty()) {
            System.out.println("📋 Escolhido alta prioridade");
            return listaAltaPrioridade.removerPrimeiro();
        } else if (!listaMediaPrioridade.isEmpty()) {
            System.out.println("📋 Escolhido media prioridade");
            return listaMediaPrioridade.removerPrimeiro();
        } else if (!listaBaixaPrioridade.isEmpty()) {
            System.out.println("📋 Escolhido baixa prioridade");
            return listaBaixaPrioridade.removerPrimeiro();
        }

        return null;
    }

    private void executarProcesso(Processo processo) {
        System.out.println("▶️ EXECUTANDO: " + processo);

        if (processo.getPrioridade() == 1) {
            contadorCiclosAltaPrioridade++;
        }

        processo.executar();

        if (processo.terminou()) {
            System.out.println("✅ Processo FINALIZADO: " + processo);
            processosFinalizados++;
        } else {
            ListaDeProcessos listaOrigem = obterListaPorPrioridade(processo.getPrioridade());
            if (listaOrigem != null) {
                listaOrigem.adicionar(processo);
                System.out.println("   → Recolocado na lista prioridade " + processo.getPrioridade());
            }
        }
    }

    public boolean executarCiclo() {
        cicloAtual++;
        System.out.println("\n" + "═".repeat(60));
        System.out.println("CICLO " + cicloAtual);
        System.out.println("═".repeat(60));

        desbloquearProcesso();

        exibirEstado();

        if (!temProcessosProntos()) {
            System.out.println("⏸️ Nenhum processo pronto para execucao!");
            if (listaBloqueados.isEmpty()) {
                System.out.println("🏁 Sistema completamente vazio - finalizando...");
                return false;
            }
            return true;
        }

        Processo processoEscolhido = escolherProximoProcesso();

        if (processoEscolhido == null) {
            System.out.println("❌ Erro: nenhum processo selecionado!");
            return false;
        }

        if (processoEscolhido.precisaDisco()) {
            bloquearProcesso(processoEscolhido);
            return true;
        }

        executarProcesso(processoEscolhido);

        return true;
    }

    public void executarCompleto() {
        final int maxCiclos = 100;

        System.out.println("═══════════════════════════════════════");
        System.out.println("    SISTEMA iCEVOS - INICIANDO");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Limite anti inanicao: " + limiteAltaPrioridade);
        System.out.println("Maximo de ciclos: " + maxCiclos);

        while (!sistemaVazio() && cicloAtual < maxCiclos) {
            if (!executarCiclo()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        exibirRelatorioFinal(maxCiclos);
    }

    private void exibirRelatorioFinal(int maxCiclos) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("EXECUCAO FINALIZADA - RELATORIO FINAL");
        System.out.println("═".repeat(60));
        System.out.println("🔢 Total de ciclos: " + cicloAtual);
        System.out.println("✅ Processos finalizados: " + processosFinalizados);
        System.out.println("🔒 Total de bloqueios: " + totalBloqueios);
        System.out.println("🔓 Total de desbloqueios: " + totalDesbloqueios);

        if (cicloAtual >= maxCiclos) {
            System.out.println("🚨 ATENCAO: Execução limitada por maximo de ciclos!");
        }

        if (!sistemaVazio()) {
            System.out.println("⚠️ ATENCAO: Sistema ainda tem processos!");
            exibirEstado();
        } else {
            System.out.println("🎯 Sistema finalizado com sucesso!");
        }

        System.out.println("═".repeat(60));
    }
}