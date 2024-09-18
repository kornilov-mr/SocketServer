import server.Server;

public class Runner {
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();


        server.stopServer();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.getAllStackTraces());

    }
}
