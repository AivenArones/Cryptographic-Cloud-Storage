import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

// https://medium.com/javarevisited/appsec-a-developers-handbook-to-mastering-rsa-and-aes-encryption-9c0e6465452f
// https://www.baeldung.com/java-aes-encryption-decryption
// https://www.baeldung.com/java-rsa-encryption-decryption
// https://docs.oracle.com/javase/8/docs/api/javax/crypto/Cipher.html
// https://docs.oracle.com/javase/8/docs/api/javax/crypto/spec/SecretKeySpec.html
// https://docs.oracle.com/javase/8/docs/api/javax/crypto/spec/IvParameterSpec.html
/* 
Mode: ECB (Electronic Codebook) - In the context of RSA, the mode is often set to Electronic Codebook, 
                                  even though RSA doesn't technically have different modes like block ciphers. 
                                  It's essentially ignored in the RSA context.

Padding Scheme: OAEPWithSHA-256AndMGF1Padding - This part specifies the padding scheme used with RSA. In this case, 
                                                it's OAEP (Optimal Asymmetric Encryption Padding) with SHA-256 
                                                as the hash function and MGF1 (Mask Generation Function 1) with the 
                                                same SHA-256 as the mask generation function.

REF: https://medium.com/javarevisited/appsec-a-developers-handbook-to-mastering-rsa-and-aes-encryption-9c0e6465452f
*/

//public SecretKeySpec: Constructs a secret key from the given byte array.
//public IvParameterSpec: Constructs an IvParameterSpec from the given byte array.
//public Cipher: This class provides the functionality of a cryptographic cipher for encryption and decryption.
//public Cipher.getInstance: This method creates a Cipher object that implements the specified transformation.

public class EncryptionAndDecryption {

    
    // Encrypt AES key using RSA public key
    // Algorithm: RSA, Mode: ECB, Padding: OAEPWithSHA-256AndMGF1Padding
    public static byte[] encryptAESKeyWithRSA(SecretKey aesKey, PublicKey publicKey) throws Exception {
        String transformation = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"; // use RSA Algorithm, ECB and OAEP padding scheme

        Cipher cipher = Cipher.getInstance(transformation); 
        cipher.init(Cipher.ENCRYPT_MODE, publicKey); // initilaize cipher for encryption using public key
        byte[] encryptedAESKey = cipher.doFinal(aesKey.getEncoded()); // encrypt the raw AES key bytes
        return encryptedAESKey;  // return encrypted AES key

    }

    // Decrypt AES key using RSA private key.
    public static SecretKey decryptAESKeyWithRSA(byte[] encryptedKey, PrivateKey privateKey) throws Exception {
        String transformation = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"; 
        Cipher cipher = Cipher.getInstance(transformation); // use RSA Algorithm, ECB and OAEP padding scheme

        // initialize cipher for decryption using private key
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  // decrypt AES key using private key
        byte[] decryptedKey = cipher.doFinal(encryptedKey);

        //javax.​crypto.​spec.​SecretKeySpec constructs a secret key from the given byte array
        SecretKey decryptedAESKey = new javax.crypto.spec.SecretKeySpec(decryptedKey, "AES"); // Restore AES key object
        return decryptedAESKey;
    }

    // Encrypt data using AES
    public static byte[] encryptDataWithAES(byte[] data, SecretKey aesKey, IvParameterSpec iv) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // return encrypted data using aesKey and Initializaition Vector
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
        byte[] encryptedData = cipher.doFinal(data); // encrypt the data
        return encryptedData;
    
    }
    
    // Decrypt data using AES
    public static byte[] decryptDataWithAES(byte[] encryptedData, SecretKey aesKey, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // return decrypted data using aesKey and Initializaition Vectr
        cipher.init(Cipher.DECRYPT_MODE, aesKey, iv); 
        byte[] decryptedData = cipher.doFinal(encryptedData); // decrypt the data
        return decryptedData;
    }
}
