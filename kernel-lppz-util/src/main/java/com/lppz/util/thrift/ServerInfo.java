package com.lppz.util.thrift;
public class ServerInfo {
 
    public String getHost() {
        return host;
    }
 
    public void setHost(String host) {
        this.host = host;
    }
 
    public int getPort() {
        return port;
    }
 
    public void setPort(int port) {
        this.port = port;
    }
 
    private String host;
    private int port;
 
    public ServerInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }
 
    public String toString() {
        return "host:" + host + ",port:" + port;
    }
}