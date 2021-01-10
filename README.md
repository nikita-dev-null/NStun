# NStun

This is an implementation of a simple client that works with the STUN protocol according to RFC 5389

A simple usage example

```java
class Client {
    public static void main(String[] args) {
        StunClient stunClient = new StunClient();
        String address = stunClient.discover();
        System.out.println(address);
    }
}
```

To set the Software name there is a method 
```java
stunClient.setSoftwareName("SoftwareName");
// default - "StunClient"
```

To set the server there is a method
```java
stunClient.setServerAddress("stun.server.example");
//default - "stun1.l.google.com"
```
To set the server port there is a method
```java
stunClient.setServerPort(3478);
//default - 19392 for stun1.l.google.com 
```
The current jar version is here https://github.com/nikita-dev-null/NStun/releases/tag/v0.1-alpha