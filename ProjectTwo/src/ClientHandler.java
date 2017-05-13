import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by christianbartram on 5/12/17.
 */
public class ClientHandler implements Runnable {

    private ServerSocket service = null;
    private PrintStream out = null;
    private DataInputStream in = null;
    private Socket connection = null;
    private boolean shutdown = false;


    /**
     * Constructor instantiates required objects
     * @param connection
     * @param service
     * @param out
     * @param in
     */
    ClientHandler(Socket connection, ServerSocket service, PrintStream out, DataInputStream in) {
        this.connection = connection;
        this.service = service;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        processConnection(connection, service, out, in);
    }


    /**
     * Process's the client connection and responds to the request for data
     * @param connection
     * @param service
     * @param out
     * @param in
     */
    private void processConnection(Socket connection, ServerSocket service, PrintStream out, DataInputStream in)
    {
        try {

            System.out.println("[Server] Connection received from " + connection.getInetAddress().getHostName());

            //Get streams from client
            out = new PrintStream(connection.getOutputStream());
            in = new DataInputStream(connection.getInputStream());

            try {

                //Here we read the buffer from the client
                switch(in.read()) {
                    case 1:
                        System.out.println("[Server] Finding Current Date and Time from " + service.getInetAddress().getHostName());

                        //Run a command read its output and respond to the clients request
                        clientResponse(out, "Hosts current date and time is " + readCommandOutput(runCommand("date")), true);

                        break;
                    case 2:
                        System.out.println("[Server] Received Request for Finding Host Uptime from " + service.getInetAddress().getHostName());

                        clientResponse(out, "Hosts current uptime is " + readCommandOutput(runCommand("uptime")), true);

                        break;
                    case 3:
                        System.out.println("[Server] Received Request for Host Memory Use " + service.getInetAddress().getHostName());

                        clientResponse(out, "Hosts current memory use is " + readCommandOutput(runCommand("free")), true);

                        break;
                    case 4:
                        System.out.println("[Server] Received Request for Host Network Statistics from " + service.getInetAddress().getHostName());

                        clientResponse(out, "Host NetStat is " + readCommandOutput(runCommand("netstat -s")), true);

                        break;
                    case 5:
                        System.out.println("[Server] Received Request for Current Users from " + service.getInetAddress().getHostName());

                        clientResponse(out, "Hosts current user " + readCommandOutput(runCommand("who")), true);

                        break;
                    case 6:
                        System.out.println("[Server] Received Request for Currently Running Processes from " + service.getInetAddress().getHostName());

                        clientResponse(out, "Host currently running processes are " + readCommandOutput(runCommand("ps -c")), true);

                        break;
                    case 7:
                        System.out.println("[Server] Received Request to shutdown...");

                        shutdown = true;

                        //todo add a default? in case unexpected data comes from the client
                }

            } catch(Exception e){
                System.err.println("Data received in unknown format");
                e.printStackTrace();
            }

        } catch(IOException ioException) {
            ioException.printStackTrace();

        } finally {
            try {
                if (shutdown) {
                    in.close();
                    out.close();
                    service.close();
                    connection.close();

                    System.exit(0);
                }
            } catch(IOException e) {
                System.err.println("Error shutting down server");
                e.printStackTrace();
            }
        }

    }

    /**
     * Sends a response back to the client through a data output stream
     * @param outputStream PrintStream output stream which we write too
     * @param successfulResponse Boolean true if the program should output a successful response code to stop the client
     * from reading further false otherwise
     */
    private void clientResponse(PrintStream outputStream, String responseText, boolean successfulResponse)
    {
        try {

            outputStream.println("[Server] Response: " + responseText);

            if(successfulResponse) {
                //Give a response code so the client knows when to stop reading
                outputStream.println("[Server] [500] OK");
            }

            outputStream.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a commands output and returns it as a string
     * @param process Process object
     * @return String command output
     */
    private String readCommandOutput(Process process)
    {
        String s;
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * Wrapper for Runtime Exec command
     * which executes a command on a linux or windows terminal
     *
     * @return Process returns a process object
     */
    private Process runCommand(String command)
    {
        try {
            return Runtime.getRuntime().exec(command);
        } catch(IOException e) {
            e.printStackTrace();
        }
        //Something went wrong
        return null;
    }

}
