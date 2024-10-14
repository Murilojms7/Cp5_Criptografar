import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSA {
    private BigInteger N;
    private BigInteger E;
    private BigInteger D;

    public RSA(BigInteger p, BigInteger q) {
        // Calcular N = p * q
        N = p.multiply(q);

        // Calcular phi(N)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Definir chave pública E e calcular D
        E = BigInteger.valueOf(65537); // Valor comum para E em implementações modernas
        D = E.modInverse(phi); // Inverso multiplicativo de E
    }

    public BigInteger getN() {
        return N;
    }

    public BigInteger getE() {
        return E;
    }

    public BigInteger encrypt(BigInteger mensagem) {
        return mensagem.modPow(E, N);
    }

    public BigInteger decrypt(BigInteger mensagemCriptografada) {
        return mensagemCriptografada.modPow(D, N);
    }
}
