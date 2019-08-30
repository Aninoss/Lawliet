package ServerStuff.CommunicationServer;

import CommandSupporters.CommandContainer;
import Constants.Settings;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.UserStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class CommunicationServer {
    private int port;
    private final byte HEARTBEAT = 0x1;
    private final byte CAN_UPDATE = 0x2;
    private final byte CONNECTED = 0x4;
    private final byte EXIT = 0x2;

    private DiscordApi api;

    public CommunicationServer(int port) {
        this.port = port;
        new Thread(this::run).start();
    }

    private void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port, 0, InetAddress.getLoopbackAddress());;

            System.out.println("Communication Server is running!");

            while(true) {
                try {
                    Socket socket = serverSocket.accept();

                    OutputStream os = socket.getOutputStream();

                    int output = HEARTBEAT;
                    if (CommandContainer.getInstance().getActivitiesSize() == 0) output |= CAN_UPDATE;
                    if (api != null && api.getServerById(Settings.HOME_SERVER_ID).isPresent() && api.getServerById(Settings.HOME_SERVER_ID).get().getTextChannelById(521088289894039562L).isPresent()) {
                        try {
                            Message message = api.getServerById(Settings.HOME_SERVER_ID).get().getTextChannelById(521088289894039562L).get().sendMessage("test").get();
                            if (message.getContent().equals("test")) output |= CONNECTED;
                            message.delete();
                        } catch (InterruptedException | ExecutionException e) {
                            //Ignore
                        }
                    }

                    os.write(output);
                    os.flush();

                    int input = socket.getInputStream().read();
                    if ((input & EXIT) > 0) System.exit(4);

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }

    public void setApi(DiscordApi api) {
        this.api = api;
    }
}
