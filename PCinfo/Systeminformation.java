package PCinfo;

import LogClasses.ITransform;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Objects;

public class Systeminformation implements ITransform {
    public int corecount;
    public InetAddress ipaddress;
    public String username;
    public OperatingSystem os;
    public String mac;

    public Systeminformation(){
    }


    public int getCorecount() {
        return corecount;
    }
    public void setCorecount(int corecount) {
        this.corecount = corecount;
    }

    public InetAddress getIpaddress() {
        return ipaddress;
    }
    public void setIpaddress(InetAddress ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setOs(OperatingSystem OS_in_use){
        this.os =OS_in_use;
    }
    public void setOs(String OS_name,String version){
        this.os =new OperatingSystem(OS_name,version);
    }
    public OperatingSystem getOs() {
        return os;
    }

    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }

    public static Systeminformation autofill(){
        Systeminformation mySys = new Systeminformation();
        mySys.setCorecount(Runtime.getRuntime().availableProcessors());
        InetAddress myIPlocal;
        try {
            myIPlocal = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        mySys.setIpaddress(myIPlocal);
        mySys.setUsername(System.getProperty("user.name"));
        try{
            NetworkInterface myNetInf = NetworkInterface.getByInetAddress(mySys.getIpaddress());
            mySys.setMac(Hex.encodeHexString(myNetInf.getHardwareAddress()));
        }catch(Exception e){
            e.printStackTrace();
        }
        mySys.setOs(OperatingSystem.autofill());
        return mySys;
    }

    public String toString(){
        String temp = "User:"+this.username+"\n";
        temp = temp+"OS: "+this.os.toString()+"\n";
        temp = temp+"MAC: "+this.mac +"\n";
        temp = temp+"IP: "+this.ipaddress.toString()+"\n";
        temp = temp+"corecount:"+ corecount +"\n";
        return temp;
    }
    public  String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        String jsonString=null;
        try {
            jsonString=mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Systeminformation that = (Systeminformation) o;
        return corecount == that.corecount &&
                ipaddress.equals(that.ipaddress) &&
                username.equals(that.username) &&
                os.equals(that.os) &&
                mac.equals(that.mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(corecount, ipaddress, username, os.getOs(),os.getVersion(), mac);
    }

    @Override
    public String retrieveClassName() {
        return "Systeminformation";
    }

    @Override
    public Systeminformation fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        ITransform temp = null;
        try {
            temp = mapper.readValue(json, Systeminformation.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (Systeminformation) temp;
    }



}
