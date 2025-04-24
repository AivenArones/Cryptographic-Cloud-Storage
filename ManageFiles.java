import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

// https://www.baeldung.com/java-cipher-input-output-stream
public class ManageFiles {


    public static void createCloud() {
        // create cloud folder if it doesn't exist
        if (Files.exists(Paths.get("cloud/"))!=true) {
            try {
                Files.createDirectories(Paths.get("cloud/")); 
            } catch (Exception e) {
                System.out.println("Error creating cloud folder: " + e.getMessage());
            }
        }
    }

    // Upload a file: encrypt content with AES, encrypt AES key with public keys
    public static void uploadFile(String filepath, List<String> groupUsernames) throws Exception {

        // check if there is access to the cloud if not establish connection
        if (Files.exists(Paths.get("cloud/"))!=true) {
              createCloud(); // create cloud folder
        }

        

        File file = new File(filepath);
        
        // case 1: file does not exist
        if (file.exists()!=true) {
            System.out.println("File not found: " + filepath);
            return;
        }

        // case 2: no user specified
        if (groupUsernames.isEmpty()) {
            System.out.println("Specify users in group");
            return;
        }

        // Read file data
        byte[] fileData = Files.readAllBytes(file.toPath());

        // Generate AES key and IV as inputs for AES
        SecretKey aesKey = GenerateKeys.generateAESKey();
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes); // create initialization vector

        // Encrypt file data
        byte[] encryptedData = EncryptionAndDecryption.encryptDataWithAES(fileData, aesKey, iv);
       
        // Prepare file path
        String filename = file.getName();
        Path cloudPath1 = Paths.get("cloud/" + filename + ".enc"); // specify path for encrypted file
 
        Files.write(cloudPath1, encryptedData); // Save encrypted content

        // Save IV and encrypted AES key for each user in a .txt file
        Path cloudPath2 = Paths.get("cloud/" + filename + ".txt"); // specify path for .txt file
        BufferedWriter writer = Files.newBufferedWriter(cloudPath2);
        writer.write("iv:" + Base64.getEncoder().encodeToString(ivBytes));
        writer.newLine();

        // Encrypt AES key with each user's public key and save them in .txt file
        int numberOfUsernames=groupUsernames.size();
        for(int i=0;i<numberOfUsernames;i++){

            String username= groupUsernames.get(i);
            PublicKey publicKey = SecureCloudStorageGroup.getPublicKey(username); // get public key of group member
            byte[] encryptedAESKey = EncryptionAndDecryption.encryptAESKeyWithRSA(aesKey, publicKey); // encrypt aes key with user RSA public key
     
            writer.write(username + ":" + Base64.getEncoder().encodeToString(encryptedAESKey)); // write encrypted aes key to file
            writer.newLine();
        }

        writer.close();
        System.out.println("File: '" + filename + "' encrypted and uploaded to cloud.");
    }

    // Download and decrypt a file for the specified user using private keys

    public static void downloadFile(String encryptedFileName, String username) throws Exception {
               
        // check if there is access to the cloud
        if (Files.exists(Paths.get("cloud/"))!=true) {
              createCloud();
        }
        System.out.println(encryptedFileName);

        String encryptedFileName2=encryptedFileName;
        // add .enc
        if(encryptedFileName.endsWith(".enc")!=true) {
           encryptedFileName = encryptedFileName + ".enc";
        }

        // get file paths of .enc and .txt files
        Path cloudPath1 = Paths.get("cloud/" + encryptedFileName);
        Path cloudPath2 = Paths.get("cloud/" + encryptedFileName2+".txt");

        if (Files.exists(cloudPath1) !=true) {
            System.out.println("Encrypted file not found");
            return;
        }

        // Read encrypted file data
        byte[] encryptedData = Files.readAllBytes(cloudPath1);

        // Read stored IV and encrypted AES key from file
        if (Files.exists(cloudPath2) !=true) {
            System.out.println("IV file not found");
            return;
        }

        List<String> lines = Files.readAllLines(cloudPath2);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] ivBytes = decoder.decode(lines.get(0).split(":")[1]); // get IV from the first line using Base64

        String encryptedAESKeyBase64 = null;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith(username + ":")) {
            encryptedAESKeyBase64 = line.split(":")[1];
            break;
        }
    }

        if (encryptedAESKeyBase64 == null) {
            System.out.println("No encrypted key found for user: " + username);
            return;
        }

        // Decrypt AES key with user's private key
        byte[] encryptedAESKey = decoder.decode(encryptedAESKeyBase64);
        PrivateKey privateKey = SecureCloudStorageGroup.getPrivateKey(username);
        SecretKey aesKey = EncryptionAndDecryption.decryptAESKeyWithRSA(encryptedAESKey, privateKey);

        // Decrypt file data
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        byte[] decryptedData = EncryptionAndDecryption.decryptDataWithAES(encryptedData, aesKey, iv);
 
        
        // Save decrypted file locally
        String originalName = encryptedFileName.replace(".enc", ""); // get rid of .enc
        Path outputPath = Paths.get(originalName);
        Files.write(outputPath, decryptedData);

        System.out.println("File decrypted and saved as: " + outputPath.getFileName());
    }

    public static void deleteCloudFile(String encryptedFileName) throws Exception {

        String encryptedFileName2=encryptedFileName;

        // add ".enc" 
        if(encryptedFileName.endsWith(".enc")!=true) {
            encryptedFileName = encryptedFileName + ".enc";
        }

        
        // get paths to files
        Path cloudPath1 = Paths.get("cloud/" + encryptedFileName);
        Path cloudPath2 = Paths.get("cloud/" + encryptedFileName2+".txt");
    
  
            // Check if the file exists
            if (Files.exists(cloudPath1)==true) {

                // Delete the file
                Files.delete(cloudPath1);
                Files.delete(cloudPath2);
                System.out.println("Deleted encrypted file: " + cloudPath1.getFileName());
            
            }else{
                System.out.println("file not found: " + cloudPath1.getFileName());

            }

        } 
    }
    

