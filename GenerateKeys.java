import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.SecretKey;

// https://www.baeldung.com/java-base64-encode-and-decode
// https://medium.com/@AlexanderObregon/javas-base64-getencoder-method-explained-d3c331139837
// https://docs.oracle.com/en/java/javase/23/docs/api/java.base/java/security/KeyFactory.html

// While Base64 encoding does not offer encryption, 
// it often works in tandem with encryption algorithms to provide a safe way to transmit encrypted data.

// The KeyPairGenerator class is used to generate pairs of public and private keys.

/**
    X.509 standard defines the public key certificate format and X509EncodedKeySpec is part of java.security.spec 
    X509EncodedKeySpec is used when an X.509-encoded public key is in a byte array, and you want to convert it into a PublicKey object. 
 */
public class GenerateKeys {

    // Generates a new RSA key pair with a size of 2048 bits
    // This key pair will be used for asymmetric encryption/decryption 

    // generate RSA key pair using KeyPairGenerator
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA"); // generate key pair using RSA
        generator.initialize(2048); // use RSA key size of 2048 bits
        
        KeyPair keyPairRSA= generator.generateKeyPair();

        //System.out.println("Public Key: "+keyPairRSA.getPublic());
        //System.out.println("Private Key: "+keyPairRSA.getPrivate());

        return keyPairRSA; 

    }

    // Generates a new AES key with a size of 256 bits
    // This key will be used for symmetric encryption/decryption, AES key will be used to encrypt the file data

    /// generate AES key
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("AES");
        // create a new AES key 
        generator.init(256); // use 256 bits for AES key size
        SecretKey generatedAesKey = generator.generateKey();
        return generatedAesKey;
    }


    //  Binary data (like keys or encrypted files) can't always be directly stored due to the fact that
    //  they may contain non-printable or special characters.
    //  Base64 converts binary data into an ASCII string of characters 

    // encode key to base64 string
    public static String encodeKeyToBase64(Key key) {

        Base64.Encoder base64Encoder = Base64.getEncoder(); // create an encoder
        String encodedKey =base64Encoder.encodeToString(key.getEncoded()); // encode into String using Base64 encoding scheme
        
        return encodedKey;
    }

    // decode RSA public key from a base64 string
    public static PublicKey decodeRSAPublicKeyFromBase64(String keyStr) throws Exception {
        Base64.Decoder base64Decoder = Base64.getDecoder(); // create a decoder
        byte[] keyBytes = base64Decoder.decode(keyStr); // decode keyStr

        // Convert the byte array to a PublicKey object

        //x509 encoded public key
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes); 
        KeyFactory factory = KeyFactory.getInstance("RSA");
  
        PublicKey publicKey = factory.generatePublic(keySpec); // generate public key from key specification
        return publicKey;
    }

    // decode RSA private key from a base64 string
    public static PrivateKey decodeRSAPrivateKeyFromBase64(String keyStr) throws Exception {
        Base64.Decoder base64Decoder = Base64.getDecoder(); // create a decoder
        byte[] keyBytes = base64Decoder.decode(keyStr); // decode keyStr

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        // generates public key object from key specification
        PrivateKey privateKey = factory.generatePrivate(keySpec); // generate private key from key specification

        return privateKey;
    }
}
