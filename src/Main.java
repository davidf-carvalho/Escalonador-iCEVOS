public class Main {
    public static void main(String[] args) {
        System.out.println("=== Teste Lista Ligada ===");

        ListaDeProcessos lista = new ListaDeProcessos();

        System.out.println("Lista vazia: " + lista);
        System.out.println("Tamanho: " + lista.size());
        System.out.println("Está vazia? " + lista.isEmpty());

        Processo p1 = new Processo(1, "Proc1", 1, 3, null);
        Processo p2 = new Processo(2, "Proc2", 2, 2, "DISCO");
        Processo p3 = new Processo(3, "Proc3", 3, 1, null);

        lista.adicionar(p1);
        lista.adicionar(p2);
        lista.adicionar(p3);

        System.out.println("Lista após adições: " + lista);
        System.out.println("Tamanho: " + lista.size());

        System.out.println("Primeiro (peek): " + lista.peek());

        Processo removido = lista.removerPrimeiro();
        System.out.println("Removido: " + removido);
        System.out.println("Lista após remoção: " + lista);
        System.out.println("Tamanho: " + lista.size());
    }
}