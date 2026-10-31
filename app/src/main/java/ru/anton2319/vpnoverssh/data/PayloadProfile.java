package ru.anton2319.vpnoverssh.data;

import java.util.UUID;

public class PayloadProfile {
    public String uuid = UUID.randomUUID().toString();
    public String name = "";
    public String proxyHost = "";
    public int proxyPort = 8080;
    public String payload = "";

    public PayloadProfile() { }
}
