import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Random;
import java.util.zip.CRC32;


// byte[] ba = new ByteArrayBuilder()
public class STUN {
    @Test
    public void stunTest() throws IOException {
        System.out.println("Test");
        int startSTUNMessage = 41;
        byte[] messageHeader = new byte[100];
        messageHeader[0] = 0;
        messageHeader[1] = 0;

        byte[] message = new byte[]{(byte)0, (byte)1, (byte)0, (byte)24, (byte)33, (byte)18, (byte)164, (byte)66, (byte) 53, (byte)79, (byte)184, (byte)221, (byte) 131, (byte) 192, (byte) 236, (byte) 50, (byte) 80, (byte) 164, (byte) 161, (byte) 18, (byte) 128, (byte) 34, (byte)0, (byte) 12, (byte) 83, (byte) 116, (byte) 117, (byte) 110, (byte) 67, (byte) 108, (byte) 105, (byte) 101, (byte) 110, (byte) 116, (byte) 0, (byte) 0, (byte) 128, (byte) 40, (byte) 0, (byte) 4, (byte) 130, (byte) 52, (byte) 198, (byte) 203};

        System.out.println("Request");
        for (int i = 0; i < message.length; i++) {
            byte b = message[i];
            startSTUNMessage ++;
//            sb.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
            System.out.printf("%5d%10s: %5d - %d%n",startSTUNMessage, Integer.toBinaryString(Byte.toUnsignedInt(b)), Byte.toUnsignedInt(b), b );
//            char c = (char) Byte.toUnsignedInt(b);
//            System.out.print(c);
        }

        startSTUNMessage = 41;

        byte[] buffer = new byte[100];

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("stun.sipnet.ru");
        DatagramPacket packet = new DatagramPacket(message, message.length, address, 3478);
        socket.send(packet);

        DatagramPacket packetRecv = new DatagramPacket(buffer, buffer.length);
        socket.receive(packetRecv);

        byte[] res = packetRecv.getData();

        int[] intRes = new int[res.length];
        for(int i = 0; i<res.length; i++) {
            intRes[i] = res[i];
        }
//        StringBuilder sb = new StringBuilder();

//        for (byte b : res)
        System.out.println("\n===================== Response ==========================");
        for (int i = 0; i < res.length; i++) {
            byte b = res[i];
            startSTUNMessage ++;
//            sb.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
            System.out.printf("%5d%10s: %5d - %d%n",startSTUNMessage, Integer.toBinaryString(Byte.toUnsignedInt(b)), Byte.toUnsignedInt(b), b );
//            char c = (char) Byte.toUnsignedInt(b);
//            System.out.print(c);
        }
//        String tmp = sb.toString();
//        System.out.println("Bites: " + tmp);

//        System.out.println(convertByteArrayToInt2(res));
    }

    @Test
    public void someTest() {
        byte[] bytes = new byte[]{(byte) 32, (byte) 0, (byte) 8, (byte) 0, (byte) 1, (byte) 151, (byte) 183, (byte) 108, (byte) 250, (byte) 178, (byte) 86};
        for (byte b : bytes) {
            System.out.println((char) Byte.toUnsignedInt(b));
        }
    }

    @Test
     public void generateTransactionID() {
        byte[] transactionID = new byte[12];
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            transactionID[i] = (byte) random.nextInt(Byte.MAX_VALUE);
        }
       for (byte b : transactionID) {
           System.out.println(b);
       }
    }

   public byte[] concat(byte[] arrayA, byte[] arrayB) {
        byte[] res = new byte[arrayA.length + arrayB.length];
        for (int i = 0; i < arrayA.length; i ++) {
            res[i] = arrayA[i];
        }

        for (int j = 0; j < arrayB.length; j++) {
            res[j+arrayA.length] = arrayB[j];
        }

        return res;
    }

    @Test
    public void srsTest2 () {
        byte[] message = new byte[]{(byte)0, (byte)1, (byte)0, (byte)24, (byte)33, (byte)18, (byte)164, (byte)66, (byte) 53, (byte)79, (byte)184, (byte)221, (byte) 131, (byte) 192, (byte) 236, (byte) 50, (byte) 80, (byte) 164, (byte) 161, (byte) 18, (byte) 128, (byte) 34, (byte)0, (byte) 12, (byte) 83, (byte) 116, (byte) 117, (byte) 110, (byte) 67, (byte) 108, (byte) 105, (byte) 101, (byte) 110, (byte) 116, (byte) 0, (byte) 0}; //(byte) 128, (byte) 40}; //, (byte) 0, (byte) 4};
        CRC32 crc = new CRC32();
        crc.update(message);
        System.out.println(crc.getValue());
        System.out.println(Long.toBinaryString(crc.getValue()));
        System.out.println(xorCrc(Long.toHexString(crc.getValue())));

        byte[] f = xorCrc(Long.toHexString(crc.getValue()));

        for (byte b : f) {
            System.out.println(b);
        }

    }

//             82  10000010:   130 - -126
//             83    110100:    52 - 52
//             84  11000110:   198 - -58
//             85  11001011:   203 - -53
    @Test
    public void crcTest() {
        byte[] message = new byte[]{(byte)0, (byte)1, (byte)0, (byte)24, (byte)33, (byte)18, (byte)164, (byte)66, (byte) 53, (byte)79, (byte)184, (byte)221, (byte) 131, (byte) 192, (byte) 236, (byte) 50, (byte) 80, (byte) 164, (byte) 161, (byte) 18, (byte) 128, (byte) 34, (byte)0, (byte) 12, (byte) 83, (byte) 116, (byte) 117, (byte) 110, (byte) 67, (byte) 108, (byte) 105, (byte) 101, (byte) 110, (byte) 116, (byte) 0, (byte) 0, (byte) 128, (byte) 40, (byte) 0, (byte) 4};
        long res = createCrc32(message);
        System.out.println(res);
        String resstr = Long.toHexString(res);
//        System.out.println(resstr);
//        System.out.println;
//        System.out.println(res);
        byte[] test = new byte[8];
        byte[] crc = longToByteArray(res);
        for (byte b : crc) {
            System.out.print(Integer.toBinaryString(Byte.toUnsignedInt(b)) + " ");
        }
        System.out.println();
//        01001100 11010101 01110011 00100011
//            76       213     115       35
//        01010011 01010100 01010101 01001110
//        00011111 10000001 00100110 01101101
//

//        10000010 00110100 11000110 11001011
//        01010011 01010100 01010101 01001110
//        11010001 01100000 10010011 10000101
//          209       96        147     133
        byte[] crc2 = xorCrc(resstr);
        for (byte b : crc2) {
            System.out.print(b+ " ");
        }
    }

    public byte[] xorCrc(String crc) {
        String str = "5354554e";
        BigInteger i1 = new BigInteger(crc, 16);
        BigInteger i2 = new BigInteger(str, 16);
        BigInteger res = i1.xor(i2);
        String s3 = res.toString(16);
        System.out.println(s3);
        return res.toByteArray();
    }

    public long createCrc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(bytes);
        System.out.printf("%X\n", crc.getValue());
        return crc.getValue();
    }

    public byte[] longToByteArray (long value ) {
        byte[] bytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(value).array();
        return bytes;
    }
      private final byte[] Message_Type_Binding_Request = new byte[]{(byte) 0, (byte) 1}; // 42 - 43 bytes

//    The magic cookie field MUST contain the fixed value 0x2112A442 in network byte order.
//    RFC 5389
    private final byte[] Message_Magic_Cookie = new byte[]{(byte) 33, (byte) 18, (byte) 164, (byte) 66};
    @Test
    public void testConcat() {
       byte[] res = concat(Message_Type_Binding_Request, Message_Magic_Cookie);
       for (byte b : res) {
           System.out.print(b);
       }
    }

    @Test
    public void setAttributes_Software_Software() {
        String software = "StunClient";
        byte[] bytes = software.getBytes();
        System.out.println(bytes.length);
        for (byte b : bytes) {
            System.out.print(b);
        }
    }

    @Test
    public void puddingTest() {
        int len = 14;
        int i = len % 4;
        int resLen;
        System.out.println(i);
    }
}

//          Format of STUN Message Header
//
//       0                   1                   2                   3
//       0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
//      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//      |0 0|     STUN Message Type     |         Message Length        |
//      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//      |                         Magic Cookie                          |  The magic cookie field MUST contain the fixed value 0x2112A442 in network byte order
//      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//      |                                                               |
//      |                     Transaction ID (96 bits)                  |  the transaction ID is chosen by the STUN client for the request
//      |                                                               |
//      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+


//                 Format of STUN Message Type Field
//                        0                 1
//                        2  3  4 5 6 7 8 9 0 1 2 3 4 5
//                       +--+--+-+-+-+-+-+-+-+-+-+-+-+-+
//                       |M |M |M|M|M|C|M|M|M|C|M|M|M|M|
//                       |11|10|9|8|7|1|6|5|4|0|3|2|1|0|
//                       +--+--+-+-+-+-+-+-+-+-+-+-+-+-+
//
//for IPv4, the actual STUN message would need
//   to be less than 548 bytes (576 minus 20-byte IP header, minus 8-byte
//   UDP header, assuming no IP options are used).


// 100000:    32 - 32
//         0:     0 - 0
//      1000:     8 - 8
//         0:     0 - 0
//         1:     1 - 1
//  10010111:   151 - -105
//  10110111:   183 - -73
//   1101100:   108 - 108
//  11111010:   250 - -6
//  10110010:   178 - -78
//   1010110:    86 - 86

//1101100 11111010 10110010 1010110 - addr (70 - 73 bytes)

//11000100 11000110 - port (68 - 69 bytes)

//cookie 2112a442
//00100001 00010010 10100100 01000010 - cookie (46 - 49 bytes)
//11000100 11000110 (port)
//11100101 11010100 (xor)
//11100101 11010100 - 58836

//01101100 11111010 10110010 01010110 (addr)
//00100001 00010010 10100100 01000010 (cookie)
//01001101 11101000 00010110 00010100  (xor)
//   77       232       22       20