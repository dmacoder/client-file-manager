package com.bellsoft.updater.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InetUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(InetUtil.class);
    
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
}
