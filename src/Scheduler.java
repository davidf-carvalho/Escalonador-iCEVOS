public class Scheduler {

    private static final int limiteAltaPrioridade = 5;

    private ListaDeProcessos listaAltaPrioridade;
    private ListaDeProcessos listaMediaPrioridade;
    private ListaDeProcessos listaBaixaPrioridade;

    private int cicloAtual;
    private int processosFinalizados;
    private int contadorCiclosAltaPrioridade;

    public Scheduler() {
        this.listaAltaPrioridade = new ListaDeProcessos();
        this.listaMediaPrioridade = new ListaDeProcessos();
        this.listaBaixaPrioridade = new ListaDeProcessos();
        this.cicloAtual = 0;
        this.processosFinalizados = 0;
        this.contadorCiclosAltaPrioridade = 0;
    }

    public int getCicloAtual() {
        return cicloAtual;
    }

    public int getProcessosFinalizados() {
        return processosFinalizados;
    }

    public int getContadorAltaPrioridade() {
        return contadorCiclosAltaPrioridade;
    }

    public void adicionarProcesso(Processo processo) {
        ListaDeProcessos listaDestino = obterListaPorPrioridade(processo.getPrioridade());
        if (listaDestino != null) {
            listaDestino.adicionar(processo);
            System.out.println("Processo adicionado: " + processo);
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
        System.out.println("Contador anti inanicao: " + contadorCiclosAltaPrioridade + "/" + limiteAltaPrioridade);
    }

    private Processo escolherProximoProcesso() {
        if (contadorCiclosAltaPrioridade >= limiteAltaPrioridade) {
            System.out.println("--- ANTI INANICAO ATIVADA! Priorizando menor prioridade...");

            if (!listaMediaPrioridade.isEmpty()) {
                contadorCiclosAltaPrioridade = 0; // reset contador
                System.out.println("Escolhido media por anti inanicao");
                return listaMediaPrioridade.removerPrimeiro();
            }

            if (!listaBaixaPrioridade.isEmpty()) {
                contadorCiclosAltaPrioridade = 0;
                System.out.println("Escolhido baixa por anti inanicao");
                return listaBaixaPrioridade.removerPrimeiro();
            }

            System.out.println("Apenas alta prioridade disponivel");
        }

        if (!listaAltaPrioridade.isEmpty()) {
            System.out.println("Escolhido alta prioridade (normal)");
            return listaAltaPrioridade.removerPrimeiro();
        } else if (!listaMediaPrioridade.isEmpty()) {
            System.out.println("Escolhido media prioridade (normal)");
            return listaMediaPrioridade.removerPrimeiro();
        } else if (!listaBaixaPrioridade.isEmpty()) {
            System.out.println("Escolhido baixa prioridade (normal)");
            return listaBaixaPrioridade.removerPrimeiro();
        }

        return null;
    }

    private void executarProcesso(Processo processo) {
        System.out.println("--- EXECUTANDO: " + processo);

        if (processo.getPrioridade() == 1) {
            contadorCiclosAltaPrioridade++;
        }

        processo.executar();

        if (processo.terminou()) {
            System.out.println("--- Processo terminou: " + processo);
            processosFinalizados++;
        } else {
            ListaDeProcessos listaOrigem = obterListaPorPrioridade(processo.getPrioridade());
            if (listaOrigem != null) {
                listaOrigem.adicionar(processo);
                System.out.println("--- Recolocado na lista prioridade " + processo.getPrioridade());
            }
        }
    }

    public boolean executarCiclo() {
        cicloAtual++;
        System.out.println("\n" + "=".repeat(50));
        System.out.println("CICLO " + cicloAtual);
        System.out.println("=".repeat(50));

        exibirEstado();

        if (!temProcessosProntos()) {
            System.out.println("Nenhum processo pronto para execucao!");
            return false;
        }

        Processo processoEscolhido = escolherProximoProcesso();

        if (processoEscolhido != null) {
            executarProcesso(processoEscolhido);
        }

        return true;
    }

    public void executarCompleto(int maxCiclos) {
        System.out.println(" Iniciando Scheduler com Anti Inanicao ");
        System.out.println("Limite de alta prioridade: " + limiteAltaPrioridade);
        System.out.println("Maximo de ciclos: " + maxCiclos);

        while (temProcessosProntos() && cicloAtual < maxCiclos) {
            if (!executarCiclo()) {
                break;
            }

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("EXECUCAO FINALIZADA");
        System.out.println("=".repeat(50));
        System.out.println("Total de ciclos: " + cicloAtual);
        System.out.println("Processos finalizados: " + processosFinalizados);

        if (cicloAtual >= maxCiclos) {
            System.out.println("ATENCAO: Limite de ciclos atingido!");
        }
    }
}