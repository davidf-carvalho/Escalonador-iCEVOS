public class Scheduler {

    private ListaDeProcessos listaAltaPrioridade;
    private ListaDeProcessos listaMediaPrioridade;
    private ListaDeProcessos listaBaixaPrioridade;

    private int cicloAtual;
    private int processosFinalizados;

    public Scheduler() {
        this.listaAltaPrioridade = new ListaDeProcessos();
        this.listaMediaPrioridade = new ListaDeProcessos();
        this.listaBaixaPrioridade = new ListaDeProcessos();
        this.cicloAtual = 0;
        this.processosFinalizados = 0;
    }

    public int getCicloAtual() {
        return cicloAtual;
    }

    public int getProcessosFinalizados() {
        return processosFinalizados;
    }

    public void adicionarProcesso(Processo processo) {
        switch (processo.getPrioridade()) {
            case 1:
                listaAltaPrioridade.adicionar(processo);
                break;
            case 2:
                listaMediaPrioridade.adicionar(processo);
                break;
            case 3:
                listaBaixaPrioridade.adicionar(processo);
                break;
            default:
                System.err.println("Prioridade invalida: " + processo.getPrioridade());
                return;
        }
        System.out.println("Processo adicionado: " + processo);
    }

    public boolean temProcessosProntos() {
        return !listaAltaPrioridade.isEmpty() ||
                !listaMediaPrioridade.isEmpty() ||
                !listaBaixaPrioridade.isEmpty();
    }

    private ListaDeProcessos obterListaPorPrioridade(int prioridade) {
        switch (prioridade) {
            case 1: return listaAltaPrioridade;
            case 2: return listaMediaPrioridade;
            case 3: return listaBaixaPrioridade;
            default: return null;
        }
    }

    public void exibirEstado() {
        System.out.println(" Estado das Listas ");
        System.out.println("Alta Prioridade: " + listaAltaPrioridade);
        System.out.println("Media Prioridade: " + listaMediaPrioridade);
        System.out.println("Baixa Prioridade: " + listaBaixaPrioridade);
        System.out.println("Ciclo atual: " + cicloAtual);
    }

    public boolean executarCicloSimples() {
        cicloAtual++;
        System.out.println("\nCICLO " + cicloAtual + " ");

        exibirEstado();

        if (!temProcessosProntos()) {
            System.out.println("Nenhum processo para executar!");
            return false;
        }

        Processo processoEscolhido = null;
        if (!listaAltaPrioridade.isEmpty()) {
            processoEscolhido = listaAltaPrioridade.removerPrimeiro();
            System.out.println("Executando alta prioridade: " + processoEscolhido);
        } else if (!listaMediaPrioridade.isEmpty()) {
            processoEscolhido = listaMediaPrioridade.removerPrimeiro();
            System.out.println("Executando media prioridade: " + processoEscolhido);
        } else if (!listaBaixaPrioridade.isEmpty()) {
            processoEscolhido = listaBaixaPrioridade.removerPrimeiro();
            System.out.println("Executando baixa prioridade: " + processoEscolhido);
        }

        if (processoEscolhido != null) {
            processoEscolhido.executar();

            if (processoEscolhido.terminou()) {
                System.out.println("Processo terminou: " + processoEscolhido);
                processosFinalizados++;
            } else {
                ListaDeProcessos listaOrigem = obterListaPorPrioridade(processoEscolhido.getPrioridade());
                if (listaOrigem != null) {
                    listaOrigem.adicionar(processoEscolhido);
                    System.out.println("Processo recolocado na lista prioridade " + processoEscolhido.getPrioridade());
                }
            }
        }

        return true;
    }

    public void executarCiclos(int maxCiclos) {
        System.out.println("Iniciando scheduler com maximo de " + maxCiclos + " ciclos");

        while (temProcessosProntos() && cicloAtual < maxCiclos) {
            if (!executarCicloSimples()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("\n Execucao Finalizada ");
        System.out.println("Total de ciclos: " + cicloAtual);
        System.out.println("Processos finalizados: " + processosFinalizados);
    }
}