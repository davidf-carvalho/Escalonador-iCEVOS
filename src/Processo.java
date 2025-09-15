public class Processo {
    private int id;
    private String nome;
    private int prioridade;    //1-alta 2-media 3-baixa
    private int ciclosNecessarios;
    private String recursoNecessario;
    private boolean jaPediuRecurso;

    public Processo(int id, String nome, int prioridade, int ciclosNecessarios, String recursoNecessario) {
        this.id = id;
        this.nome = nome;
        this.prioridade = prioridade;
        this.ciclosNecessarios = ciclosNecessarios;
        this.recursoNecessario = recursoNecessario;
        this.jaPediuRecurso = false;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public int getCiclosNecessarios() {
        return ciclosNecessarios;
    }

    public String getRecursoNecessario() {
        return recursoNecessario;
    }

    public boolean isJaPediuRecurso() {
        return jaPediuRecurso;
    }

    public void executar() {
        if (ciclosNecessarios > 0) {
            ciclosNecessarios--;
        }
    }

    public boolean terminou() {
        return ciclosNecessarios == 0;
    }

    public boolean precisaDisco() {
        return recursoNecessario != null &&
                recursoNecessario.equals("DISCO") &&
                !jaPediuRecurso;
    }

    public void marcarRecursoPedido() {
        this.jaPediuRecurso = true;
    }

    @Override
    public String toString() {
        return "P" + id + "(" + nome + ",pri:" + prioridade + ",ciclos:" + ciclosNecessarios + ")";
    }
}