# Cryptographic-Cloud-Storage

CSU34031 Project 2
Secure Cloud Storage – Aiven Arones 

My application uses AES encryption to encrypt files before storing them in a secure cloud folder. The AES key will then be encrypted using RSA for further security, the Asymmetric RSA algorithm is used to address the key distribution problem.1 Files are secured in such a way that only users of the Secure Cloud Storage Group can download the decrypted files. To anyone not part of the group, the encrypted file will be unintelligible. 

o simulate transfer to the cloud, a folder called the cloud is used. Encrypted files are stored in this cloud folder. These encrypted files can then be downloaded after decryption, provided you have the correct keys. The private and public keys of group members are stored as a file ending in .key within a keys folder. This allows us to keep track of who is in the group because to be in the group would mean having an RSA KeyPair in the folder. 

When uploading a file to the cloud, an AES key and an Initialization Vector are generated as inputs for the encryptDataWithAES method. The output file is then stored in the cloud and appended with the .enc file extension.  

For each file, the public key of each recipient is used to encrypt the file’s AES key. The encrypted AES keys for each recipient are then stored in a .txt file to ensure that only the intended recipients can decrypt the file. Since the corresponding Initialization Vector is required, the Initialization Vector can also be found within the encrypted files .txt file. 

Encryption using AES is done in CBC mode and an IV (Initialization Vector). AES is a Symmetric Key algorithm. The file data, secret key and IV are needed for encryption. The IV ensures that plaintext encrypted with the same key produces different ciphertexts.2 Since it is in CBC mode, padding is required, my application uses PKCS5Padding. Files uploaded to the cloud are encrypted using AES encryption. 

 

RSA allows for the safe exchange of this symmetric key, allowing a user to securely send a secret key to another user. The AES key is encrypted with a user’s RSA public key and decrypted using the corresponding private key. So, in the case an attacker intercepts the AES key, they will need a user’s private key to decrypt it.1  

 

The RSA padding scheme I used for encryption was OAEP (Optimal Asymmetric Encryption Padding) and the mode was set to ECB. The padding introduces a level of randomness making it less susceptible to certain attacks.1 The padding scheme I use also involves MGF1 mask generation function and a SHA-256 hash function. 

 

My application also uses the X.509EncodedKeySpec class. The X.509 standard defines the format of a public key certificate.1 Within my GenerateKeys class lies a method that converts a byte array into a public key object.  

 

In tandem, my application also makes use of the Base64 library. In Java, it is common to store PublicKey objects as a Base64 String.1 This is because string formats are easier to store. The ManageFiles class writes the encrypted aes keys to the .txt file as a Base64 String. 

 

The KeyPairGenerator class from the Java.security library is used to generate RSA key pairs. The RSA keys of all group members have a key size of 2048 bits. My application uses AES keys with 256 bits. My GenerateKeys decodes RSA keys from Base64 Strings using an X509EncodedKeySpec which is passed into a KeyFactory to generate the public and private key. 

My ManageFiles class handles downloading from the cloud and uploading to the cloud. It also calls the EncryptionAndDecryption class to ensure that uploaded files are encrypted and downloaded files can be decrypted by the intended recipients. The ManageFiles class uses the java.io.* and java.nio.fil.* libraries to handle file management. A BufferedWriter is used when writing to files. The ManageFiles class also ensure that .key files are stored in the key folder and any .enc files are stored in the cloud folder. 

 The ManageFiles calls the relevant methods in the EncryptionAndDecryption class to facilitate uploading and downloading from the Cloud. It also writes the encrypted AES to relevant .txt file in the cloud folder. 

My ManageFiles, GenerateKeys and EncryptionAndDecryption classes all work together to implement a suitable key management system for my application. Just as specified in the problem, my program makes use of public-key certificates through X509EncodedKeySpec. The SecureCloudStorageGroup class implements the addition of adding and removing members from the group.  

My application mimics the use of a command line interface. The Main class will interpret user commands. My application will interpret inputs as possible commands. Within the Main file lies 6 application commands. The commands ./add-member and the ./remove-member commands are used to add and remove members from the Secure Cloud Storage Group. Inputs such as ./delete and ./upload are used to mimic uploading and deleting files to the cloud. As mentioned before, uploading a file will encrypt it before storing it into the cloud folder. To download a file from the cloud folder is done using ./download, a downloaded file will decrypt and encrypted files since all files in the cloud folder are encrypted. To exit the application, you can use the ./exit command as input. 

The Main file also creates a cloud folder to store encrypted files and a key folder to store group member public and private keys if those folders do not already exist. 

This application simulates cloud storage but can easily be integrated to something like Google Drive using Google Drive API. Since the cloud folder is stored locally, any user can access the file in the cloud folder but will be unable to read the contents or encrypt it without having the needed private key. Only members of the secure cloud group can upload and download decrypted files to the cloud folder since a _pub.key and _priv.key file are needed. And such keys are only created from my application by being added to the Secure Cloud Storage group. The creation of a cloud folder can also be thought of as creating a connection with the cloud storage service. 

Sources: 

https://medium.com/javarevisited/appsec-a-developers-handbook-to-mastering-rsa-and-aes-encryption-9c0e6465452f 1  

https://www.baeldung.com/java-aes-encryption-decryption2  

https://www.baeldung.com/java-cipher-input-output-stream 3   

https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html4  

https://www.baeldung.com/java-rsa5  

https://www.baeldung.com/cs/rsa-public-key-format6  



 

         
