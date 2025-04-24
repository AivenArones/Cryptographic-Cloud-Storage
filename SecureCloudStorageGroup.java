import java.nio.file.*;
import java.security.*;
import java.util.ArrayList;

public class SecureCloudStorageGroup {

    public static ArrayList<String> groupUsers = new ArrayList<>();
    
    // Adds a new user to the group and generates RSA keys
    public static void addUser(String username) throws Exception {

        
        // check if the keys folder exists
        if (Files.exists(Paths.get("keys/"))!=true) {
            try {
                Files.createDirectories(Paths.get("keys/")); 
            } catch (Exception e) {
                System.out.println("Error creating keys folder: " + e.getMessage());
            }
        }

        KeyPair keyPair = GenerateKeys.generateRSAKeyPair(); // generate RSA keys for new user

        // Check if user is in group
        if(doesUserExist(username)==true){
            System.out.println("User already in group");
        }

        if(doesUserExist(username)!=true){
        // Save public key
        String publicKeyBase64 = GenerateKeys.encodeKeyToBase64(keyPair.getPublic());
        Files.write(Paths.get("keys/" + username + "_pub.key"), publicKeyBase64.getBytes());

        
        // Save private key
        String privateKeyBase64 = GenerateKeys.encodeKeyToBase64(keyPair.getPrivate());
        Files.write(Paths.get("keys/" + username + "_priv.key"), privateKeyBase64.getBytes());

        System.out.println("User '" + username + "' added to the group.");
        groupUsers.add(username); // add user to the group
        }
    }

    // remove user from the group and delete their RSA keys
    public static void removeUser(String username) throws Exception {
        
        // check if the keys folder exists
        if (Files.exists(Paths.get("keys/"))!=true) {
            try {
                Files.createDirectories(Paths.get("keys/")); 
            } catch (Exception e) {
                System.out.println("Error creating keys folder: " + e.getMessage());
            }
        }
        
        // check if user is in group
        if(doesUserExist(username)!=true){
            System.out.println(username + "is not in group.");
        }

    
        if(doesUserExist(username)==true){
        Path publicKeyPath = Paths.get("keys/" + username + "_pub.key");
        Path privateKeyPath = Paths.get("keys/" + username + "_priv.key");

        // delete public and private keys
        if (Files.exists(publicKeyPath)) {
            Files.delete(publicKeyPath);
        }
        if (Files.exists(privateKeyPath)) {
            Files.delete(privateKeyPath);
        }

        groupUsers.remove(username);
        System.out.println("User '" + username + "' removed from the group.");
    }
    }

    // get a user's RSA public key from file
    public static PublicKey getPublicKey(String username) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get("keys/" + username + "_pub.key"));
        String keyStr = new String(keyBytes);
        return GenerateKeys.decodeRSAPublicKeyFromBase64(keyStr);
    }

    // get a user's RSA private key from file
    public static PrivateKey getPrivateKey(String username) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get("keys/" + username + "_priv.key"));
        String keyStr = new String(keyBytes);
        return GenerateKeys.decodeRSAPrivateKeyFromBase64(keyStr);
    }

    // Checks if user is already in group
    public static boolean doesUserExist(String username) {
               
        if(groupUsers.contains(username)||Files.exists(Paths.get("keys/" + username + "_pub.key"))==true) {
            return true;
        }else  {
            return false;
        }
    }
}
