import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Servidor {
    private static BigInteger N;


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9600)) {
            System.out.println("Servidor aguardando conexão...");
            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                N = new BigInteger(in.readUTF());

                BigInteger E = new BigInteger(in.readUTF());

                // Gerar a chave RSA com p e q
                BigInteger p = new BigInteger("2000107");
                BigInteger q = new BigInteger("2000113");
                RSA rsa = new RSA(p, q);

                BigInteger mensagemCriptografada = new BigInteger(in.readUTF());
                System.out.println("-> Mensagem criptografada recebida: " + mensagemCriptografada);

                // Descriptografar a mensagem recebida
                BigInteger mensagemDescriptografada = rsa.decrypt(mensagemCriptografada);
                String mensagemOriginal = new String(mensagemDescriptografada.toByteArray(), StandardCharsets.UTF_8);
                System.out.println("-> Mensagem descriptografada: " + mensagemOriginal);

                // Permitir que o servidor envie uma resposta
                Scanner scanner = new Scanner(System.in);
                System.out.print("Digite a resposta que deseja enviar ao cliente: ");
                String resposta = scanner.nextLine();

                // Criptografar a resposta para enviar ao cliente
                BigInteger respostaBigInt = new BigInteger(1, resposta.getBytes(StandardCharsets.UTF_8));
                BigInteger respostaCriptografada = rsa.encrypt(respostaBigInt);
                out.writeUTF(respostaCriptografada.toString()); // Envia a resposta criptografada de volta
                System.out.println("-> Resposta criptografada enviada ao cliente: " + respostaCriptografada);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
