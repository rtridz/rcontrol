/**
 * Created by vadim on 27.09.15.
 */
public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 8001;

    public static void main(String[] args) {
        Server server = new Server(HOST, PORT);
        System.out.println("Server has been started at " + HOST + ":" + PORT);
        try {
            server.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
