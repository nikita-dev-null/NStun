import BytesMessage.StunClient;

public class Main {
    public static void main(String[] args) {
        StunClient stunClient = new StunClient();
//        stunClient.setSoftwareName("TestStunClient");
//        stunClient.setServerAddress("stun.sipnet.ru");
//        stunClient.setServerPort(3478);
        String address = stunClient.discover();
        System.out.println(address);
    }
}
