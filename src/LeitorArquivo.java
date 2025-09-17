import java.io.*;
import java.util.Scanner;

public class LeitorArquivo {

    private static final String comentarioPrefix = "#";
    private static final String separador = ",";

    public static boolean carregarProcessos(String nomeArquivo, Scheduler scheduler) {
        File arquivo = new File(nomeArquivo);

        if (!arquivo.exists()) {
            System.out.println("⚠️ Arquivo nao encontrado: " + nomeArquivo);
            criarProcessosExemplo(scheduler);
            return false;
        }

        try (Scanner scanner = new Scanner(arquivo)) {
            System.out.println("📁 Carregando processos do arquivo: " + nomeArquivo);

            int linha = 0;
            int processosCarregados = 0;

            while (scanner.hasNextLine()) {
                linha++;
                String conteudoLinha = scanner.nextLine().trim();

                if (isLinhaValida(conteudoLinha)) {
                    if (processarLinha(conteudoLinha, scheduler, linha)) {
                        processosCarregados++;
                    }
                }
            }

            System.out.println("✅ Processos carregados com sucesso: " + processosCarregados);
            return processosCarregados > 0;

        } catch (FileNotFoundException e) {
            System.err.println("❌ Erro: arquivo nao encontrado - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ Erro de E/S ao ler arquivo - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado - " + e.getMessage());
        }

        criarProcessosExemplo(scheduler);
        return false;
    }

    private static boolean isLinhaValida(String linha) {
        return !linha.isEmpty() && !linha.startsWith(comentarioPrefix);
    }

    private static boolean processarLinha(String linha, Scheduler scheduler, int numeroLinha) {
        try {
            String[] campos = linha.split(separador);

            if (campos.length < 4) {
                System.err.println("⚠️ Linha " + numeroLinha + ": Poucos campos (minimo 4): " + linha);
                return false;
            }

            int id = parseInt(campos[0], "ID", numeroLinha);
            String nome = validarNome(campos[1], numeroLinha);
            int prioridade = validarPrioridade(parseInt(campos[2], "Prioridade", numeroLinha), numeroLinha);
            int ciclos = validarCiclos(parseInt(campos[3], "Ciclos", numeroLinha), numeroLinha);

            String recurso = null;
            if (campos.length > 4 && !campos[4].trim().isEmpty()) {
                recurso = campos[4].trim();
            }

            if (id > 0 && nome != null && prioridade > 0 && ciclos > 0) {
                Processo processo = new Processo(id, nome, prioridade, ciclos, recurso);
                scheduler.adicionarProcesso(processo);
                return true;
            }

        } catch (NumberFormatException e) {
            System.err.println("⚠️ Linha " + numeroLinha + ": Erro de formato numerico - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("⚠️ Linha " + numeroLinha + ": Erro ao processar - " + e.getMessage());
        }

        return false;
    }

    private static int parseInt(String valor, String nomeCampo, int numeroLinha) throws NumberFormatException {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            System.err.println("⚠️ Linha " + numeroLinha + ": " + nomeCampo + " deve ser um número: " + valor);
            throw e;
        }
    }

    private static String validarNome(String nome, int numeroLinha) {
        String nomeProcessado = nome.trim();
        if (nomeProcessado.isEmpty()) {
            System.err.println("⚠️ Linha " + numeroLinha + ": Nome na   o pode ser vazio");
            return null;
        }
        return nomeProcessado;
    }

    private static int validarPrioridade(int prioridade, int numeroLinha) {
        if (prioridade < 1 || prioridade > 3) {
            System.err.println("⚠️ Linha " + numeroLinha + ": Prioridade deve ser 1, 2 ou 3. Encontrado: " + prioridade);
            return -1;
        }
        return prioridade;
    }

    private static int validarCiclos(int ciclos, int numeroLinha) {
        if (ciclos <= 0) {
            System.err.println("⚠️ Linha " + numeroLinha + ": Ciclos deve ser maior que 0. Encontrado: " + ciclos);
            return -1;
        }
        return ciclos;
    }

    private static void criarProcessosExemplo(Scheduler scheduler) {
        System.out.println("🔧 Criando processos de exemplo...");

        scheduler.adicionarProcesso(new Processo(1, "Calculadora", 3, 2, null));
        scheduler.adicionarProcesso(new Processo(2, "EditorTexto", 2, 4, "DISCO"));
        scheduler.adicionarProcesso(new Processo(3, "NavegadorWeb", 1, 3, null));
        scheduler.adicionarProcesso(new Processo(4, "JogoTetris", 1, 5, null));
        scheduler.adicionarProcesso(new Processo(5, "MusicPlayer", 2, 3, null));
        scheduler.adicionarProcesso(new Processo(6, "Antivirus", 1, 6, "DISCO"));
        scheduler.adicionarProcesso(new Processo(7, "EmailClient", 3, 2, null));
        scheduler.adicionarProcesso(new Processo(8, "BackupSystem", 3, 4, "DISCO"));

        System.out.println("✅ Processos de exemplo criados!");
    }

    public static void exibirFormatoAjuda() {
        System.out.println("📖 Formato do arquivo:");
        System.out.println("   id,nome,prioridade,ciclos[,recurso]");
        System.out.println("");
        System.out.println("   - id: numero inteiro unico");
        System.out.println("   - nome: string sem virgulas");
        System.out.println("   - prioridade: 1 (Alta), 2 (Media), 3 (Baixa)");
        System.out.println("   - ciclos: numero inteiro > 0");
        System.out.println("   - recurso: opcional, \"DISCO\" ou vazio");
        System.out.println("");
        System.out.println("📝 Exemplo:");
        System.out.println("   1,Calculadora,3,2,");
        System.out.println("   2,Editor,2,4,DISCO");
        System.out.println("   # Comentarios começam com #");
    }
}