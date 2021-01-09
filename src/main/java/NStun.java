import BytesMessage.ParseUdpMessageSTUNResponse;
import BytesMessage.UdpStunMessage;

import java.io.IOException;
import java.net.*;

public class NStun {
    public static void main(String[] args) throws IOException {
        byte[] message = new UdpStunMessage().createUdpMessage();
        byte[] buffer = new byte[100];
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("stun1.l.google.com"); //stun.sipnet.ru
//        InetAddress address = InetAddress.getByName("stun.sipnet.ru");
        DatagramPacket packet = new DatagramPacket(message, message.length, address, 19302);
//        DatagramPacket packet = new DatagramPacket(message, message.length, address, 3478);
        socket.send(packet);

        DatagramPacket packetRecv = new DatagramPacket(buffer, buffer.length);
        socket.receive(packetRecv);

        ParseUdpMessageSTUNResponse parseUdpMessageSTUNResponse = new ParseUdpMessageSTUNResponse(packetRecv.getData());
       int[] res = parseUdpMessageSTUNResponse.getIPAddress();
       int port = parseUdpMessageSTUNResponse.getPort();
//         byte[] res = packetRecv.getData();
        String addr = parseUdpMessageSTUNResponse.getIPAddressAsString();

//        for (int i : res) {
//            System.out.print(i+".");
//        }
//        System.out.print(port);
//        System.out.println();
        System.out.println(addr);
//        int startSTUNMessage = 41;
//        System.out.println("\n===================== Response ==========================");
//        for (int i = 0; i < res.length; i++) {
//            byte b = res[i];
////            startSTUNMessage ++;
//            System.out.printf("%5d%10s: %5d - %d%n",i, Integer.toBinaryString(Byte.toUnsignedInt(b)), Byte.toUnsignedInt(b), b );
//        }
    }
}
