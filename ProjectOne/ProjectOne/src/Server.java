import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by christianbartram on 5/11/17.
 */
class Server {

    private static ServerSocket service = null;
    private static PrintStream out = null;
    private static DataInputStream in = null;


    public static void main(String[] args)
    {
         Socket connection = null;

        try {
            //creating a server socket
            service = new ServerSocket(43594);

            // Wait for connection
            System.out.println("[Server] Waiting for connection...");

            while(true) {

                connection = service.accept();

                ClientHandler handler = new ClientHandler(connection, service, out, in);

                Thread thread = new Thread(handler);
                thread.start();

            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}




/*

for(int i = 0 i < 10 i++ {

new Thread();

}

for(int i = 0; i < 10; i++)
{
 thread[i].start();
}

startTime;
thread.start.

 must have separate time measurements for each thread

thread.stop;
stopTime;

response time = startTime - stopTime
 */
//date
//uptime
//free
//netstat
//who
//ps -c
//ps -aux