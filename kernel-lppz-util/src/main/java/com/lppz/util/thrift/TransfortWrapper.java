package com.lppz.util.thrift;
import org.apache.thrift.transport.TTransport;
 
import java.text.SimpleDateFormat;
import java.util.Date;
 
public class TransfortWrapper {
 
    private TTransport transport;
 
    /**
     * 是否正忙
     */
    private boolean isBusy = false;
 
    /**
     * 是否已经挂
     */
    private boolean isDead = false;
 
    /**
     * 最后使用时间
     */
    private Date lastUseTime;
 
    /**
     * 服务端Server主机名或IP
     */
    private String host;
 
    /**
     * 服务端Port
     */
    private int port;
 
    public TransfortWrapper(TTransport transport, String host, int port, boolean isOpen) {
        this.lastUseTime = new Date();
        this.transport = transport;
        this.host = host;
        this.port = port;
        if (isOpen) {
            try {
                transport.open();
            } catch (Exception e) {
                //e.printStackTrace();
                System.err.println(host + ":" + port + " " + e.getMessage());
                isDead = true;
            }
        }
    }
 
    public TransfortWrapper(TTransport transport, String host, int port) {
        this(transport, host, port, false);
    }
 
 
    public boolean isBusy() {
        return isBusy;
    }
 
    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }
 
    public boolean isDead() {
        return isDead;
    }
 
    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }
 
    public TTransport getTransport() {
        return transport;
    }
 
    public void setTransport(TTransport transport) {
        this.transport = transport;
    }
 
    /**
     * 当前transport是否可用
     *
     * @return
     */
    public boolean isAvailable() {
        return !isBusy && !isDead && transport.isOpen();
    }
 
    public Date getLastUseTime() {
        return lastUseTime;
    }
 
    public void setLastUseTime(Date lastUseTime) {
        this.lastUseTime = lastUseTime;
    }
 
    public String getHost() {
        return host;
    }
 
    public int getPort() {
        return port;
    }
 
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "hashCode:" + hashCode() + "," +
                host + ":" + port + ",isBusy:" + isBusy + ",isDead:" + isDead + ",isOpen:" +
                transport.isOpen() + ",isAvailable:" + isAvailable() + ",lastUseTime:" + format.format(lastUseTime);
    }
}