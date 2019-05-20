import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread extends Thread {
    private Socket socket = null;
    private final Server server;
    private BasicController controller = new BasicController();
    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        boolean running = true;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //client -> server stream
            PrintWriter out = new PrintWriter(socket.getOutputStream()); //server -> client stream
            while (running) {
                String request = in.readLine();
                String response = null;
                if(request != null)
                    response = execute(request);
                out.println(response);
                out.flush();

            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private String execute(String request) {
        String[] parts = request.split(" ");
        if(parts[0].equals("find"))
        {
            try {
                return controller.findById(Integer.parseInt(parts[1]));
            }catch (SQLException e){
                System.out.println(e);
            }
        }
        return "Request primit " + request;
    }
}
