public class Main {
    public static void main(String[] args) {
        System.out.println("Teste Processo");

        Processo p1 = new Processo(1, "Calculadora", 3, 3, null);
        System.out.println("Inicial: " + p1);

        p1.executar();
        System.out.println("Após execução: " + p1);
        System.out.println("Terminou? " + p1.terminou());

        Processo p2 = new Processo(2, "Editor", 2, 2, "DISCO");
        System.out.println("\nProcesso com disco: " + p2);
        System.out.println("Precisa disco? " + p2.precisaDisco());

        p2.marcarRecursoPedido();
        System.out.println("Após marcar recurso - Precisa disco? " + p2.precisaDisco());
    }
}