public class Main {
    public static void main(String[] args) {
        System.out.println(" Teste Anti Inanicao ");

        Scheduler scheduler = new Scheduler();

        for (int i = 1; i <= 7; i++) {
            scheduler.adicionarProcesso(new Processo(i, "Alta" + i, 1, 2, null));
        }

        scheduler.adicionarProcesso(new Processo(10, "Media1", 2, 3, null));
        scheduler.adicionarProcesso(new Processo(11, "Baixa1", 3, 2, null));
        scheduler.adicionarProcesso(new Processo(12, "Media2", 2, 1, null));

        scheduler.executarCompleto(20);
    }
}