package org.ajaxanywhere;

import org.ajaxanywhere.utils.Hex;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class AutoZoneUtils {

    public static final String AZ_START_PREFIX = "<!--@aa:autoZone ";
    public static final String AZ_END_PREFIX = "<!--@/aa:autoZone ";
    public static final String AZ_SUFFIX = "-->";
    private static final String STRING_ENCODING = "UTF-8";
    private static final String HASH_ALGO = "MD5";

    // as we are not using MD for security purposes,
    // 4 bytes instead of MD5's 16 is enought to detect zone content update
    private static final int DIGEST_LENGTH = 4;
    private static final String AUTO_REFRESH_DIGESTS_HEADER_KEY = "autozonedigests";


    public static String getId(String content, int offset) {
        int pos1 = content.indexOf("id=\"", offset);
        if (pos1 != -1) {
            int pos2 = content.indexOf("\"", pos1+4);
            if (pos2 != -1) {
                return content.substring(pos1 + 4, pos2);
            }
        }
        return null;
    }

    public static String getStartMarker(String id) {
        return AZ_START_PREFIX + "id=\"" + id + "\"" + AZ_SUFFIX;
    }

    public static String getEndMarker(String id) {
        return AZ_END_PREFIX + "id=\"" + id + "\"" + AZ_SUFFIX;
    }

    /**
     * @param content
     * @param offset
     * @param data
     * @return zone end offset
     */
    private static int calcDigests(String content, int offset, Map data) {

        if (!content.substring(offset,offset+AZ_START_PREFIX.length()).equals(AZ_START_PREFIX))
            throw new RuntimeException("Developper error : ! content.startsWith(AZ_START_PREFIX)");

        try {
            MessageDigest md = getMessageDigest();
            String id = getId(content, offset);
            offset = content.indexOf(AZ_SUFFIX, offset) + AZ_SUFFIX.length();

            int zoneEnd = content.indexOf(getEndMarker(id), offset);

            // find subzones;
            while (true) {
                int subZonePos = content.indexOf(AZ_START_PREFIX, offset);
                if (subZonePos == -1 || subZonePos > zoneEnd) {
                    md.update(content.substring(offset, zoneEnd).getBytes(STRING_ENCODING));
                    break;
                } else {
                    int newOffset = calcDigests(content, subZonePos, data);
                    md.update(content.substring(offset, newOffset).getBytes(STRING_ENCODING));
                    offset = newOffset;
                }
            }

            if (data.containsKey(id)) {
                throw new IllegalStateException("duplicate autozone id:" + id);
            }

            String contentDigest = Hex.hex(md.digest(), DIGEST_LENGTH);
            data.put(id, contentDigest);

            zoneEnd = content.indexOf(AZ_SUFFIX, zoneEnd);
            return zoneEnd;

        } catch (UnsupportedEncodingException e) {
            //This should never happen
            throw new IllegalStateException("String.getBytes(\"UTF-8\") is not supported on this system.");
        }

    }

    public static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(HASH_ALGO);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 java.security.MessageDigest is not supported on this system");
        }
        return md;
    }

    public static Map getAutoZonesData(String content) {
        Map res = new HashMap();
        int zonePos =0;
        while(true){
            zonePos = content.indexOf(AZ_START_PREFIX,zonePos);
            if (zonePos == -1)
                break;
                
            zonePos = calcDigests(content, zonePos, res);
        }
        return res;
    }

    public static void sendNewDigests(BufferResponseWrapper brs, Map autoZonesData) throws IOException {
        StringBuffer newDigests = new StringBuffer();
        MessageDigest md = getMessageDigest();
        for (Iterator iterator = autoZonesData.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String id = (String) entry.getKey();
            String hash = (String) entry.getValue();
            String idHash = Hex.hex(md.digest(id.getBytes()),AutoZoneUtils.DIGEST_LENGTH);
            md.reset();
            newDigests.append(".").append(idHash).append(hash);
        }
        if (newDigests.length()>0) {
            String script = "<script>" +
                    "if (window.AjaxAnywhere) AjaxAnywhere.updateDigests(\"" + newDigests + "\")" +
                    "</script>";

            brs.output(script);
        }
    }

    static String getZoneContent(String zone, BufferResponseWrapper bufferResponseWrapper) {
        String res = bufferResponseWrapper.findSubstring(getStartMarker(zone), getEndMarker(zone));
        return res;
    }

    public static List getAutoRefreshZonesFromDigest(HttpServletRequest request, Map autoZonesData) {
        List res = new ArrayList();
        Map idHashToId = new HashMap(autoZonesData.size());
        MessageDigest md = getMessageDigest();
        for (Iterator iterator = autoZonesData.keySet().iterator(); iterator.hasNext();) {
            String id = (String) iterator.next();
            String idHash = Hex.hex(md.digest(id.getBytes()),AutoZoneUtils.DIGEST_LENGTH);
            md.reset();
            idHashToId.put(idHash,id);
        }
        String digests = request.getHeader(AUTO_REFRESH_DIGESTS_HEADER_KEY);
        if (digests!=null) {
            StringTokenizer st = new StringTokenizer(digests,".",false);
            while (st.hasMoreTokens()){
                String pair = st.nextToken();
                int splitPos = pair.length() / 2;
                String idHash = pair.substring(0,splitPos);
                String contentHash = pair.substring(splitPos);
                String id = (String) idHashToId.get(idHash);
                if (id!=null){
                    if (! autoZonesData.get(id).equals(contentHash)){
                       res.add(id);
                    }
                }

            }
        }
        return res;
    }

}
