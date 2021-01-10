package BytesMessage;

import java.math.BigInteger;

class ParseUdpMessageSTUNResponse {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

//    The magic cookie field MUST contain the fixed value 0x2112A442 in network byte order
//    RFC 5389
    private static final String MAGIC_COOKIE = "2112A442";
    private static final String MAGIC_COOKIE_PORT = "2112";

    private final byte[] rawMessage;

    ParseUdpMessageSTUNResponse(byte[] rawMessage) {
        this.rawMessage = rawMessage;
    }

    int[] getIPAddress() {
        int[] res = new int[4];
        byte[] rawAddress = new byte[]{rawMessage[28],rawMessage[29],rawMessage[30], rawMessage[31]};
        String hexAddress = bytesToHex(rawAddress);
        byte[] mappedAddress = xorMappedAddress(hexAddress, MAGIC_COOKIE);
        for (int i = 0; i < mappedAddress.length; i++) {
                res[i] = Byte.toUnsignedInt(mappedAddress[i]);
        }
        return res;
    }

    String getIPAddressAsString() {
        String stringRes = "";
        byte[] rawAddress = new byte[]{rawMessage[28],rawMessage[29],rawMessage[30], rawMessage[31]};
        String hexAddress = bytesToHex(rawAddress);
        byte[] mappedAddress = xorMappedAddress(hexAddress, MAGIC_COOKIE);
        for (int i = 0; i < mappedAddress.length; i++) {
            stringRes = stringRes + Byte.toUnsignedInt(mappedAddress[i]);
            if (i == mappedAddress.length - 1) {
                stringRes = stringRes + ":";
            } else {
                stringRes = stringRes + ".";
            }
        }
        return stringRes+getPort();
    }

    int getPort() {
        byte[] rawPort = new byte[]{rawMessage[26], rawMessage[27]};
        String hexPort = bytesToHex(rawPort);
        byte[] mappedPort = xorMappedAddress(hexPort, MAGIC_COOKIE_PORT);
        return Integer.parseInt(bytesToHex(mappedPort), 16);
    }

    private byte[] xorMappedAddress(String crc, String magicCookie) {
        BigInteger i1 = new BigInteger(crc, 16);
        BigInteger i2 = new BigInteger(magicCookie, 16);
        BigInteger res = i1.xor(i2);
        return res.toByteArray();
    }
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
