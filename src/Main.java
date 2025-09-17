public class Main {

    private static final String arquivoPadrao = "processos.txt";

    public static void main(String[] args) {
        exibirCabecalho();

        Scheduler scheduler = new Scheduler();
        String nomeArquivo = obterNomeArquivo(args);

        boolean sucesso = LeitorArquivo.carregarProcessos(nomeArquivo, scheduler);

        if (!sucesso) {
            System.out.println("💡 Para criar seu proprio arquivo:");
            LeitorArquivo.exibirFormatoAjuda();
        }

        iniciarExecucao(scheduler);
    }

    private static void exibirCabecalho() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              SISTEMA OPERACIONAL iCEVOS                 ║");
        System.out.println("║         ESCALONADOR DE PROCESSOS v1.0                   ║");
        System.out.println("║    com Anti Inanicao e Gerenciamento de Recursos        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    private static String obterNomeArquivo(String[] args) {
        if (args.length > 0) {
            System.out.println("📁 Arquivo especificado: " + args[0]);
            return args[0];
        } else {
            System.out.println("📁 Usando arquivo padrão: " + arquivoPadrao);
            System.out.println("💡 Use: java Main <arquivo> para especificar outro arquivo");
            return arquivoPadrao;
        }
    }

    private static void iniciarExecucao(Scheduler scheduler) {
        System.out.println("\n🚀 Iniciando execucao do scheduler...");
        System.out.println("💡 Pressione Ctrl+C para interromper a qualquer momento");
        System.out.println("");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scheduler.executarCompleto();

        System.out.println("\n🎯 Execucao do iCEVOS finalizada!");
    }
}