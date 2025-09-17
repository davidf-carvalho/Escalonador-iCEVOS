public class Main {
    public static void main(String[] args) {
        System.out.println("=== Teste Scheduler Básico ===");

        Scheduler scheduler = new Scheduler();


        scheduler.adicionarProcesso(new Processo(1, "Alta1", 1, 2, null));
        scheduler.adicionarProcesso(new Processo(2, "Media1", 2, 3, null));
        scheduler.adicionarProcesso(new Processo(3, "Baixa1", 3, 1, null));
        scheduler.adicionarProcesso(new Processo(4, "Alta2", 1, 2, null));


        scheduler.executarCiclos(10);
    }
}