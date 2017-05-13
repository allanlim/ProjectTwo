import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by christianbartram on 5/11/17.
 */
class Client {

    private static int command = 0; //Command corresponds to the number in the menu
    private static String hostname; //Hostname specified
    private static long endTime;
    private static int id = 1;
    private static HashMap<String, Long> responseTimes = new HashMap<>(); //Clients are the key and response time is the value


    public static void main(String[] args)
    {
        printMenu();
        processInput();

        for(int i  = 0; i < 70; i++) {
            connect();
        }

        System.out.println("Mean Response Time: " + mean(responseTimes.values()));

    }

    private static void connect()
    {
        Socket requestSocket = null;
        DataOutputStream out = null;
        DataInputStream in = null;

        try {
            System.out.println("[Client " + id + "] Making connection request to hostname: " + hostname);

            //1. creating a socket to connect to the server
            requestSocket = new Socket(hostname, 43594);

            System.out.println("[Client " + id + "] Connected to localhost on port 43594");

            //Get Input and Output streams
            out = new DataOutputStream(requestSocket.getOutputStream());
            in = new DataInputStream(requestSocket.getInputStream());

            System.out.println("[Client " + id + "] Attempting to retrieve inputted data from server...");

            if(requestSocket != null && out != null && in != null) {

                //Communicating with the server
                try {

                    //Measure the response time
                    long startTime = System.currentTimeMillis();

                    //In the output buffer write the command entered and send it to the server
                    out.writeByte(command);
                    System.out.println("[Client " + id + "] Data Sent Successfully!");

                    String responseLine;

                    //Read the response from the server
                    while ((responseLine = in.readLine()) != null) {

                        System.out.println(responseLine);

                        if (responseLine.contains("[500] OK")) {
                            endTime = System.currentTimeMillis();
                            break;
                        }
                    }

                    responseTimes.put("[Client " + id + "]", (endTime - startTime));


                } catch (Exception e) {

                    System.err.println("Data received in unknown format");
                    e.printStackTrace();
                }
            }


        } catch(UnknownHostException unknownHost) {

            System.err.println("You are trying to connect to an unknown host!");


        } catch(IOException ioException) {

            ioException.printStackTrace();

        } finally {

            //Closing connection
            try{

                System.out.println("[Client " + id + "] Closing Connection to host");

                in.close();
                out.close();
                requestSocket.close();

                id++;

            } catch(IOException ioException) {

                ioException.printStackTrace();
            }
        }
    }


    /**
     * Prints a menu on the client side for a user to make a selection from
     */
    private static void printMenu()
    {
        LinkedList<String> commands = new LinkedList<>();

        //Add commands to the linked list
        commands.add("1 - Current Date & Time");
        commands.add("2 - Uptime");
        commands.add("3 - Memory Use");
        commands.add("4 - Network Statistics");
        commands.add("5 - Current Users");
        commands.add("6 - Hosts Running Processes");
        commands.add("7 - Quit");


        System.out.println("------------------------------------------------------------");
        System.out.println("Please type the numbers 1-7 to request data from the server.");
        System.out.println("Use CLI arguments -hostname to specify a desired hostname!");
        System.out.println("For example: 1 -hostname 127.0.0.1");
        System.out.println();



        //Iterate through the commands
        commands.forEach(System.out::println);

        System.out.println("------------------------------------------------------------");

    }

    private static double mean(Collection<Long> m) {
        long sum = 0;
        for (long aM : m) {
            sum += aM;
        }
        return sum / m.size();
    }


    /**
     * Validates that the input matches the selected criteria
     * @param input String input from scanner
     * @return true if the input is valid false otherwise
     */
    private static boolean isValidInput(String input)
    {
        char[] valid = {'1', '2', '3', '4', '5', '6', '7'};

        for (char c : valid) {
            if (c == input.charAt(0)) {
                //The number is valid
                if(input.contains("-hostname")) {
                    //Contains the hostname argument
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Validates Input given from the scanner
     * sets hostname and command variables
     */
    private static void processInput()
    {
        Scanner s = new Scanner(System.in);
        String input;

        try {

            input = s.nextLine();

            //Ensure number is between 1 and 7
            while(!isValidInput(input)) {
                System.err.println("Your input is invalid please enter a number between 1 and 7 and ensure that the -hostname flag is set");
                input = s.nextLine();
            }

            //We have our validated command mutate it
            command = Character.getNumericValue(input.charAt(0));

            char[] arr = input.toCharArray();
            int spaceCounter = 0;

            for(int i = 0; i < arr.length; i++) {
                if(arr[i] == ' ') {
                    spaceCounter++;

                    //The second space is the hostname value
                    if(spaceCounter == 2) {
                        hostname = input.substring(i + 1, arr.length);
                    }
                }
            }

        } catch(Exception e) {
            System.err.println("That input is invalid please enter a number between 1 and 7");
            e.printStackTrace();
        }
        s.close();
    }




}
