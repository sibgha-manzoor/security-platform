package com.security.securityplatform.model;

public class IOC {
    private String type;   // "IP" or "Domain"
    private String value;  // actual IP or domain
    private String source; // "AbuseIPDB" or "AlienVault"

    public IOC() {}

    public IOC(String type, String value, String source) {
        this.type = type;
        this.value = value;
        this.source = source;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    @Override
    public String toString() {
        return "IOC{type='" + type + "', value='" + value + "', source='" + source + "'}";
    }
}
