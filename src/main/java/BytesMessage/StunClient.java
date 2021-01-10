package BytesMessage;

import java.io.IOException;
import java.net.*;

public class StunClient {
    private final UdpStunMessage udpStunMessageClient;
    private String serverAddress = "stun1.l.google.com";
    private int port = 19302;
    private int socketTimeOut = 500;

    public StunClient() {
        udpStunMessageClient = new UdpStunMessage();
    }

    public void setSoftwareName(String softwareName) {
        udpStunMessageClient.setAttributesSoftware(softwareName);
    }

    public void setServerAddress(String server) {
        serverAddress = server;
    }

    public void setServerPort(int port) {
        this.port = port;
    }

    public String discover() {
        ParseUdpMessageSTUNResponse parseUdpMessageSTUNResponse = null;
        byte[] buffer = new byte[100];
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(socketTimeOut);
            InetAddress address = InetAddress.getByName(serverAddress);
            byte[] message = udpStunMessageClient.createUdpMessage();
            DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
            socket.send(packet);
            packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException se) {
                  socketTimeOut += 100;
                  parseUdpMessageSTUNResponse = reconnect();
                  if (parseUdpMessageSTUNResponse.getIPAddressAsString() != null) {
                      return parseUdpMessageSTUNResponse.getIPAddressAsString();
                  }
                  return "";
            }
            parseUdpMessageSTUNResponse = new ParseUdpMessageSTUNResponse(packet.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (parseUdpMessageSTUNResponse.getIPAddressAsString() != null) {
            return parseUdpMessageSTUNResponse.getIPAddressAsString();
        }
        return "";
    }

    private ParseUdpMessageSTUNResponse reconnect() {
        DatagramPacket packet = null;
        byte[] buffer = new byte[100];
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(socketTimeOut);
            InetAddress address = InetAddress.getByName(serverAddress);
            byte[] message = udpStunMessageClient.createUdpMessage();
            packet = new DatagramPacket(message, message.length, address, port);
            socket.send(packet);
            packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException se) {
                if (socketTimeOut < 1500) {
                    socketTimeOut += 100;
                    return reconnect();
                } else {
                    throw new SocketTimeoutException();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ParseUdpMessageSTUNResponse(packet.getData());
    }
}



