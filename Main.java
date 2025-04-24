import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

//./add-member a
//./add-member b
//./add-member c
//./upload picture_of_a_dog.jpeg a b
//./download picture_of_a_dog.jpeg c
public class Main {

    // commands for the application
    private static void listOfCommands() {
        System.out.println("\nCommands:");
        System.out.println("./add-member <username>");
        System.out.println("./remove-member <username>");
        System.out.println("./delete <filename>");
        System.out.println("./upload <filepath> <user1> <user2>");
        System.out.println("./download <encrypted-file> <username>");
        System.out.println("./exit\n");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        listOfCommands(); // print instructions 

        // check if the cloud folder exists if it does not create it
        if (Files.exists(Paths.get("cloud/"))!=true) {
            try {
                Files.createDirectories(Paths.get("cloud/")); // create cloud folder (Simulating establishing connection with cloud storage)
            } catch (Exception e) {
                System.out.println("Error creating cloud folder: " + e.getMessage());
            }
        }

        // check if the keys folder exists if it does not create it
        if (Files.exists(Paths.get("keys/"))!=true) {
            try {
                Files.createDirectories(Paths.get("keys/")); // create keys folder
            } catch (Exception e) {
                System.out.println("Error creating keys folder: " + e.getMessage());
            }
        }

        while (true) {
            System.out.print("> "); // makes reading input history easier and makes inputting look like a command line interface

            String input = scanner.nextLine().trim(); // get input
  
            String[] splitInput = input.split("\\s+"); // split input

            // not enough arguments
            if (splitInput.length < 2) {
                System.out.println("Invalid command. Try 'help' to see usage.");
            }

            String command = splitInput[0]; // get command

            try {
                             
                // Remove User
                if (command.equals("./remove-member")) {

                    // check if the user is specified
                    if (splitInput.length < 2) {
                        System.out.println("specify user to remove");
                    } 
                    
                    //remove user
                    else {
                        String username = splitInput[1];
                        SecureCloudStorageGroup.removeUser(username);
                    }
                } 
                
                // Add User
                if (command.equals("./add-member")) {
                    // check if a username is specified
                    if (splitInput.length < 2) {
                        System.out.println("specify user to add");
                    } 
                    
                    // add user to group
                    else {
                        String username = splitInput[1];

                        // check if the user already exists
                        if (SecureCloudStorageGroup.doesUserExist(username)) {
                            System.out.println("user already exists");
                        } 

                        else {
                            
                            SecureCloudStorageGroup.addUser(username); // add the user to the group and generate keys
                        }
                    }
                } 

                // delete file from the cloud
                if (command.equals("./delete")) {

                    // filename is not specified
                    if (splitInput.length < 2) {
                        System.out.println("delete <filename>");
                    } 
                    
                    else {
                        String filename = splitInput[1];
                        ManageFiles.deleteCloudFile(filename); // delete file
                    }
                } 
                
                // upload to cloud
                if (command.equals("./upload")) {

                    // incorrect command format
                    if (splitInput.length < 3) {
                        System.out.println("./upload <filepath> <user1> <user2>");
                    } 
                    
                
                    else {
                        String filepath = splitInput[1];
                        String[] recipients = Arrays.copyOfRange(splitInput, 2, splitInput.length);
                        ManageFiles.uploadFile(filepath, Arrays.asList(recipients));  // upload file
                    }
                }
                
                // download from cloud
                if (command.equals("./download")) {

                    // incorrect command format
                    if (splitInput.length < 3) {
                        System.out.println("./download <encrypted-file> <username>");
                    } else {
                        String encFile = splitInput[1];
                        String username = splitInput[2];
                        ManageFiles.downloadFile(encFile, username); // download file
                    }
                }

                // command unknown
                if (command.equals("./exit")!=true&&command.equals("./remove-member")!=true && command.equals("./delete")!=true && command.equals("./upload")!=true && command.equals("./download")!=true && command.equals("./add-member")!=true && command.equals("./help")!=true) {
                    System.out.println("Command Unknown: " + command);
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }

            if(command.equals("./exit")) {
                //System.out.println("Exit");
                break; 
            }
 
        }
        scanner.close(); // close scanner
    }


}