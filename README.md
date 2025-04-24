# Cryptographic-Cloud-Storage
CSU34031 Project 2
Secure Cloud Storage – Aiven Arones (21365715) 

My application uses AES encryption to encrypt files before storing them in a secure cloud folder. The AES key will then be encrypted using RSA for further security, the Asymmetric RSA algorithm is used to address the key distribution problem.1 Files are secured in such a way that only users of the Secure Cloud Storage Group can download the decrypted files. To anyone not part of the group, the encrypted file will be unintelligible. 
