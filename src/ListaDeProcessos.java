public class ListaDeProcessos {
    private No primeiro;
    private No ultimo;
    private int tamanho;

    public ListaDeProcessos() {
        this.primeiro = null;
        this.ultimo = null;
        this.tamanho = 0;
    }

    public void adicionar(Processo processo) {
        No novoNo = new No(processo);

        if (primeiro == null) {
            primeiro = novoNo;
            ultimo = novoNo;
        } else {
            ultimo.setProximo(novoNo);
            ultimo = novoNo;
        }
        tamanho++;
    }

    public Processo removerPrimeiro() {
        if (primeiro == null) {
            return null;
        }

        Processo processo = primeiro.getProcesso();
        primeiro = primeiro.getProximo();

        if (primeiro == null) {
            ultimo = null;
        }

        tamanho--;
        return processo;
    }

    public boolean isEmpty() {
        return primeiro == null;
    }

    public int size() {
        return tamanho;
    }

    public Processo peek() {
        if (primeiro == null) {
            return null;
        }
        return primeiro.getProcesso();
    }
}