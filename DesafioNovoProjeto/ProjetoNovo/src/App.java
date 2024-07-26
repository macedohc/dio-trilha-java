import java.util.Scanner;

abstract class Cofre {
    protected String tipo;
    protected String metodoAbertura;

    public Cofre(String tipo, String metodoAbertura) {
        this.tipo = tipo;
        this.metodoAbertura = metodoAbertura;
    }

    public void imprimirInformacoes() {
        System.out.println("Tipo: " + this.tipo);
        System.out.println("Metodo de abertura: " + this.metodoAbertura);
    }
}

class CofreDigital extends Cofre {
    private int senha;

    public CofreDigital(int senha) {
        super("Cofre Digital", "Senha");
        this.senha = senha;
    }

    public boolean validarSenha(int confirmacaoSenha) {
        return confirmacaoSenha == this.senha;
    }
}

class CofreFisico extends Cofre {
    public CofreFisico() {
        super("Cofre Fisico", "Chave");
    }
}

public class CofreSingleton {
    private static CofreSingleton instance;
    private Cofre cofre;

    private CofreSingleton() {}

    public static synchronized CofreSingleton getInstance() {
        if (instance == null) {
            instance = new CofreSingleton();
        }
        return instance;
    }

    public void setCofre(Cofre cofre) {
        this.cofre = cofre;
    }

    public Cofre getCofre() {
        return this.cofre;
    }
}

public class Desafio {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String tipoCofre = scanner.nextLine();

        CofreSingleton cofreSingleton = CofreSingleton.getInstance();

        if (tipoCofre.equalsIgnoreCase("digital")) {
            int senha1 = scanner.nextInt();
            int senha2 = scanner.nextInt();
            CofreDigital cofreDigital = new CofreDigital(senha1);
            cofreSingleton.setCofre(cofreDigital);
            cofreDigital.imprimirInformacoes();
            if (cofreDigital.validarSenha(senha2)) {
                System.out.println("Cofre aberto!");
            } else {
                System.out.println("Senha incorreta!");
            }
        } else if (tipoCofre.equalsIgnoreCase("fisico")) {
            CofreFisico cofreFisico = new CofreFisico();
            cofreSingleton.setCofre(cofreFisico);
            cofreFisico.imprimirInformacoes();
        }
    }
}
