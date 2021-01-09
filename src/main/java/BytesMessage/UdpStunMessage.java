package BytesMessage;

import java.math.BigInteger;
import java.util.Random;
import java.util.zip.CRC32;

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

public class UdpStunMessage {

//    Message Type 0x0001 RFC 5389
    private final byte[] Message_Type_Binding_Request = new byte[]{(byte) 0, (byte) 1}; // 42 - 43 bytes

//    The magic cookie field MUST contain the fixed value 0x2112A442 in network byte order.
//    RFC 5389
    private final byte[] Message_Magic_Cookie = new byte[]{(byte) 33, (byte) 18, (byte) 164, (byte) 66};


//    The transaction ID is a 96-bit identifier
//    RFC 5389
    private byte[] Message_Transaction_ID;
    private byte[] CRC_32;

    //   the 32-bit value 0x5354554e RFC 5389
    private final String XOR_FINGERPRINT_RFC_VALUE = "5354554e";
    private final byte[] Message_Length = new byte[]{(byte) 0, (byte)24};
    private final byte[] Attributes_Software_Type_Assignment = new byte[]{(byte) 128, (byte) 34};
    private final byte[] Attributes_Software_Attribute_Length = new byte[]{(byte) 0, (byte) 12};

//     It is a string "StunClient" in bytes array with length 12
    private final byte[] Attributes_Software_Software = new byte[]{(byte) 83, (byte) 116, (byte) 117, (byte) 110, (byte) 67, (byte) 108, (byte) 105, (byte) 101, (byte) 110, (byte) 116, (byte) 0, (byte) 0};
    private final byte[] Attributes_Fingerprint_Type_Assignment = new byte[]{(byte) 128, (byte) 40};
    private final byte[] Attributes_Fingerprint_Attribute_Length = new byte[]{(byte) 0, (byte) 4};

    public byte[] createUdpMessage() {
        generateTransactionID();
        byte[] res;
        res = concat(Message_Type_Binding_Request, Message_Length);
        res = concat(res, Message_Magic_Cookie);
        res = concat(res, Message_Transaction_ID);
        res = concat(res, Attributes_Software_Type_Assignment);
        res = concat(res, Attributes_Software_Attribute_Length);
        res = concat(res, Attributes_Software_Software);
        CRC_32 = xorCrc(Long.toHexString(createCrc32(res)));
        res = concat(res, Attributes_Fingerprint_Type_Assignment);
        res = concat(res, Attributes_Fingerprint_Attribute_Length);
        res = concat(res, CRC_32);
        return res;
    }

    private void generateTransactionID() {
        byte[] transactionID = new byte[12];
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            transactionID[i] = (byte) random.nextInt(Byte.MAX_VALUE);
        }
        Message_Transaction_ID = transactionID;
    }

    private byte[] concat(byte[] arrayA, byte[] arrayB) {
        byte[] res = new byte[arrayA.length + arrayB.length];
        System.arraycopy(arrayA, 0, res, 0, arrayA.length);
        System.arraycopy(arrayB, 0, res, arrayA.length, arrayB.length);
        return res;
    }

//   the CRC-32 of the STUN message
//   up to (but excluding) the FINGERPRINT attribute itself, XOR'ed with
//   the 32-bit value 0x5354554e 1010011 1010100 1010101 1001110
    private long createCrc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    private byte[] xorCrc(String crc) {
        BigInteger i1 = new BigInteger(crc, 16);
        BigInteger i2 = new BigInteger(XOR_FINGERPRINT_RFC_VALUE, 16);
        BigInteger res = i1.xor(i2);
        return res.toByteArray();
    }
}
