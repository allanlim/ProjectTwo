import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by christianbartram on 5/13/17.
 */
public class ClientThreader implements Runnable {

    private int command = 0;
    private int id;
    private long endTime;

    private String hostname;
    private HashMap<String, Long> responseTimes = new HashMap<>();


    /**
     * ClientThreader constructor
     * @param id
     * @param command
     * @param hostname
     * @param responseTimes
     * @param endTime
     */
    public ClientThreader(int id, int command, String hostname, HashMap<String, Long> responseTimes, long endTime)
    {
        this.id = id;
        this.command = command;
        this.hostname = hostname;
        this.responseTimes = responseTimes;
        this.endTime = endTime;
    }

    @Override
    public void run()
    {
        connect(id, command, hostname, responseTimes, endTime);
    }


    /**
     * Handles opening a connection to the server and
     * sending the data inputted by the user
     * @param id client id
     * @param command command input by the user
     * @param hostname hostname to connect to
     * @param responseTimes HashMap to hold the response times
     * @param endTime end time of the
     */
    private static void connect(int id, int command, String hostname, HashMap<String, Long> responseTimes, long endTime)
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
                    System.out.println("[Client " + id + "] Response Time: " + (endTime - startTime));


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

            } catch(IOException ioException) {

                ioException.printStackTrace();
            }
        }
    }
}
