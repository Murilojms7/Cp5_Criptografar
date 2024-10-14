import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("localhost", 9600);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            System.out.print("Digite a mensagem que deseja enviar ao servidor: ");
            String mensagem = scanner.nextLine();

            // Gerar as chaves RSA
            BigInteger p = new BigInteger("2000107");
            BigInteger q = new BigInteger("2000113");
            RSA rsa = new RSA(p, q);

            // Converter a mensagem para BigInteger
            BigInteger mensagemBigInt = new BigInteger(1, mensagem.getBytes(StandardCharsets.UTF_8));

            // Enviar as chaves públicas (N, E) para o servidor
            out.writeUTF(rsa.getN().toString());
            out.writeUTF(rsa.getE().toString());

            // Criptografar a mensagem e enviá-la
            BigInteger mensagemCriptografada = rsa.encrypt(mensagemBigInt);
            System.out.println("-> Mensagem criptografada (como número): " + mensagemCriptografada);
            out.writeUTF(mensagemCriptografada.toString());

            // Receber a resposta do servidor
            System.out.println("-> Aguardando mensagem do servidor!");
            BigInteger respostaCriptografada = new BigInteger(in.readUTF());
            System.out.println("-> Resposta criptografada do servidor: " + respostaCriptografada);

            // Descriptografar a resposta recebida do servidor
            BigInteger respostaDescriptografada = rsa.decrypt(respostaCriptografada);
            String mensagemResposta = new String(respostaDescriptografada.toByteArray(), StandardCharsets.UTF_8);
            System.out.println("-> Mensagem recebida do servidor: " + mensagemResposta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
