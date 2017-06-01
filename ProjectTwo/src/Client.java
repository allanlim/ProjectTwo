import java.util.*;

/**
 * Created by christianbartram on 5/11/17.
 */
class Client {

    private static int command = 1; //Command corresponds to the number in the menu
    private static int id;
    private static long endTime;

    private static String hostname; //Hostname specified
    private static HashMap<String, Long> responseTimes = new HashMap<>(); //Clients id is the key and response time is the value
    private static ArrayList<Thread> threads = new ArrayList<>(); //List to hold the threads
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
            while(true)
            {
                printMenu();
                processInput(scanner);
                launchThreads(1);
            }

    }

    private static void launchThreads(int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            ClientThreader threader = new ClientThreader(id, command, hostname, responseTimes, endTime);

            threads.add(new Thread(threader));
            //threader.run(); this can execute a thread immediately whenever a client opens it connects
            id++;
        }

        //Execute all the threads at once
        threads.forEach(Thread::run);

        System.out.println("Mean Response Time: " + mean(responseTimes.values()));

        threads.clear();
        id = 1;
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
    private static void processInput(Scanner s)
    {
        try {
            String input;

            input = s.nextLine();

            if(Character.getNumericValue(input.charAt(0)) == 7) {
                System.out.println("Thank you for using this software!");
                System.exit(0);
            }

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
                        //If there is a thread argument parse hostname differently
                        hostname = input.substring(i + 1, arr.length);
                    }
                }
            }

            //s.close();

        } catch(Exception e) {
            System.err.println("That input is invalid please enter a number between 1 and 7");
        }
    }

}
