package com.bellsoft.updater.common.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @project : onlineExchange
 * <pre>
 * com.bellsoft.exchange.common.util 
 *    |_ InetUtils.java
 * 
 * </pre>
 * @date : 2019. 1. 10. 오전 11:21:49
 * @author :  [](psc)
 * @history : 
 * <pre>
 *	-----------------------------------------------------------------------
 *	변경일				작성자						변경내용  
 *	----------- ------------------- ---------------------------------------
 *	2019. 1. 10.		psc				최초 작성 
 *	-----------------------------------------------------------------------
 * </pre>
 * @description : 네트워크 관련 유틸
 * <pre>
 * 1. 개요 : 
 * 2. 처리내용 : 
 * </pre>
 */
public class InetUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(InetUtils.class);
    
    /**
     * Returns this host's non-loopback IPv4 addresses.
     * 
     * @return
     * @throws SocketException 
     */
    public static List<Inet4Address> getInet4Addresses() throws SocketException {
        List<Inet4Address> ret = new ArrayList<Inet4Address>();

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    ret.add((Inet4Address)inetAddress);
                }
            }
        }

        return ret;
    }

    /**
     * Returns this host's first non-loopback IPv4 address string in textual
     * representation.
     * 
     * @return
     * @throws SocketException
     */
    public static String getHost4Address() throws SocketException {
        List<Inet4Address> inet4 = getInet4Addresses();
        return !inet4.isEmpty()
                ? inet4.get(0).getHostAddress()
                : null;
    }
    
    /** 
     * 아이피 주소 가져오기
     * 
     * @param request 
     * @return 
     */
    public static String getIpAddress(HttpServletRequest request) {
       String ipAddress = request.getHeader("x-forwarded-for");
       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
          ipAddress = request.getHeader("Proxy-Client-IP");
       }
       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
          ipAddress = request.getHeader("WL-Proxy-Client-IP");
       }
       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
          ipAddress = request.getHeader("HTTP_CLIENT_IP");
       }
       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
          ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
       }
       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
          ipAddress = request.getRemoteAddr();
          if (ipAddress.equals("127.0.0.1")) {
              // 로컬 아이피의 경우, 기계에 의해 구성된 IP를 반환함.
              InetAddress inet = null;
              try {
                  inet = InetAddress.getLocalHost();
                  ipAddress = inet.getHostAddress();
              } catch (UnknownHostException e) {
                  e.printStackTrace();
              }
          }
       }
     
       // 다중 에이전트의 경우, 첫 번째 IP는 클라이언트의 실제 IP이고, 여러 개의 IP의 경우 ',' 로 구분된다.
       if (ipAddress != null && ipAddress.split(".").length > 3) { // "***.***.***.***"
           if (ipAddress.indexOf(",") > 0) {
               ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
           }
       }
       return ipAddress;
    }
    
    
    //로컬 IP Address
    public static String getLocalIpAddr(HttpServletRequest request) {
        return request.getLocalAddr() + ":" + request.getLocalPort();
    }
    
    /**
     * 맥주소 가져오기
     * @param ip
     * @return
     */
    public String getMACAddress(String ip) {  
        String str = "";  
        String macAddress = "";  
        try {  
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);  
            InputStreamReader ir = new InputStreamReader(p.getInputStream());  
            LineNumberReader input = new LineNumberReader(ir);  
            for (int i = 1; i < 100; i++) {  
                str = input.readLine();  
                if (str != null) {  
                    if (str.indexOf("MAC Address") > 1) {  
                        macAddress = str.substring(  
                                str.indexOf("MAC Address") + 14, str.length());  
                        break;  
                    }  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return macAddress;  
    } 
    
    /**
     * 브라우저 정보 가져오기
     * 
     *@param request
     *@return
     */
    public static String getOsAndBrowserInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String user = userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS Info=======================  
        if (userAgent.toLowerCase().indexOf("windows") >= 0) {
            os = "Windows";
        } else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
            os = "Mac";
        } else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
            os = "Unix";
        } else if (userAgent.toLowerCase().indexOf("android") >= 0) {
            os = "Android";
        } else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
            os = "IPhone";
        } else {
            os = "UnKnown, More-Info: " + userAgent;
        }
        //===============Browser===========================  
        if (user.contains("edge")) {
            browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie")) {
            String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version")) {
            browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
                    + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if (user.contains("opr") || user.contains("opera")) {
            if (user.contains("opera")) {
                browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
                        + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if (user.contains("opr")) {
                browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            }

        } else if (user.contains("chrome")) {
            browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf("mozilla/4.7") != -1)
                || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
            browser = "Netscape-?";

        } else if (user.contains("firefox")) {
            browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("rv")) {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser = "IE" + IEVersion.substring(0, IEVersion.length() - 1);
        } else {
            browser = "UnKnown, More-Info: " + userAgent;
        }

        return os + "(" + browser+ ")";
    }
}
