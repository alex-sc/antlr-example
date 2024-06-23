package example;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

public class BouncyCastleDemo {
    public static void main(String[] args) {
        RSAKeyPairGenerator rsaKeyPairGen = new RSAKeyPairGenerator();
        RSAKeyGenerationParameters param = new RSAKeyGenerationParameters(BigInteger.valueOf(65537), new SecureRandom(), 2048, 2);
        rsaKeyPairGen.init(param);


        AsymmetricCipherKeyPair kp = rsaKeyPairGen.generateKeyPair();
        System.err.println("Public: " + kp.getPublic());
        System.err.println("Private: " + kp.getPrivate());
    }
}
