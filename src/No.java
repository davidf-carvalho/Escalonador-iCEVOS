class No {
    private Processo processo;
    private No proximo;

    public No(Processo processo) {
        this.processo = processo;
        this.proximo = null;
    }

    public Processo getProcesso() {
        return processo;
    }

    public No getProximo() {
        return proximo;
    }

    public void setProximo(No proximo) {
        this.proximo = proximo;
    }
}