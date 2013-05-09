/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.onetwo.common.web.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author GKH
 */
public class HtmlFilter {

    public interface FilteredDelegate {

        public abstract void filtered(String s);
    }
    private FilteredDelegate filteredDelegate;

    public interface ImageFilteredDelegate {

        public abstract String filteredImage(String src);
    }
    private ImageFilteredDelegate imageFilteredDelegate;

    public interface CidFilteredDelegate {

        public abstract String filteredCid(String src);
    }
    private CidFilteredDelegate cidFilteredDelegate;
    private String washLink;
    private String washLinkExcluded[];
    private String washMailto;
    private String washMailtoExcluded[];
    private String safeUrls[];
    private boolean keepStyle;

    public HtmlFilter(FilteredDelegate d) {
        filteredDelegate = d;
    }

    public HtmlFilter(FilteredDelegate d, boolean keepStyle) {
        filteredDelegate = d;
        this.keepStyle = keepStyle;
    }

    public HtmlFilter(FilteredDelegate d1, ImageFilteredDelegate d2, boolean keepStyle) {
        filteredDelegate = d1;
        imageFilteredDelegate = d2;
        this.keepStyle = keepStyle;
    }

    public HtmlFilter(FilteredDelegate d1, ImageFilteredDelegate d2, CidFilteredDelegate d3, boolean keepStyle) {
        filteredDelegate = d1;
        imageFilteredDelegate = d2;
        cidFilteredDelegate = d3;
        this.keepStyle = keepStyle;
    }

    public void setKeepStyle(boolean keepStyle) {
        this.keepStyle = keepStyle;
    }

    public void setWashLink(String washLink) {
        this.washLink = washLink;
    }

    public void setWashLink(String washLink, String washLinkExcluded[]) {
        this.washLink = washLink;
        this.washLinkExcluded = washLinkExcluded;
    }

    public void setWashMailto(String washMailto) {
        this.washMailto = washMailto;
    }

    public void setWashMailto(String washMailto, String washMailtoExcluded[]) {
        this.washMailto = washMailto;
        this.washMailtoExcluded = washMailtoExcluded;
    }

    public void setSafeUrls(String safeUrls[]) {
        this.safeUrls = safeUrls;
    }

    private enum HTMLSTATE {

        INTEXT, /* Initial value. In plain text */
        SEENLT, /* Seen < */
        SEENLTBANG, /* Seen <! */
        SEENLTBANGDASH, /* Seen <!- */
        INTAG, /* Seen < but not <!, we're collecting the tag */
        INCOMMENT, /* <!--, in a comment, have not seen any dashes */
        INCOMMENTSEENDASH, /* In a comment, seen - */
        INCOMMENTSEENDASHDASH /* In a comment, seen -- */

    };

    private class TagAttrInfo {

        int tagNameStart;
        int tagNameLen;
        int tagValueStart;
        int tagValueLen;
        int atagStart;
        int atagLen;
    };
    private HTMLSTATE htmlState;
    private int inStyleTag;
    private int inScriptTag;
    private StringBuilder tagBuf = new StringBuilder();
    private int tagBufLen = 0;
    private TagAttrInfo tagAttrs[] = {};
    private int tagAttrsLen = 0;

    private void addTagBuf(char ch) {
        if (tagBufLen >= 1024) {
            return; /* DOS attack - get rid of the tag */
        }
        tagBuf.append(ch);
        tagBufLen++;
    }

    public void filter(StringBuilder html) {
        int pos = 0, start = 0;
        int len = html.length();

        htmlState = HTMLSTATE.INTEXT;
        inStyleTag = 0;
        inScriptTag = 0;

        for (; pos < len; pos++) {
            char ch = html.charAt(pos);
            switch (htmlState) {
                case INTEXT:
                    if (ch == '>') {
                        if (start < len && start < pos) {
                            filteredDelegate.filtered(html.substring(start, pos));
                        }
                        filteredDelegate.filtered("&gt;");
                        start = pos + 1;
                    }
                    if (ch != '<') {
                        continue;
                    }
                    if (inStyleTag == 0 && inScriptTag == 0 && start < len && start < pos) {
                        filteredDelegate.filtered(html.substring(start, pos));
                    }
                    htmlState = HTMLSTATE.SEENLT;
                    tagBuf.setLength(0);
                    tagBufLen = 0;
                    break;
                case SEENLT:
                    if (ch == '>') {
                        htmlState = HTMLSTATE.INTEXT;
                        start = pos + 1;
                        if (inStyleTag == 0 && inScriptTag == 0) {
                            filteredDelegate.filtered("<>");
                        }
                        continue;
                    }
                    if (ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ') {
                        break;
                    }
                    if (ch == '!') {
                        htmlState = HTMLSTATE.SEENLTBANG;
                    } else if (ch != '/' && !Character.isLetter(ch)) {
                        start = pos + 1;
                        htmlState = HTMLSTATE.INTEXT;
                        break;
                    } else {
                        htmlState = HTMLSTATE.INTAG;
                    }
                    addTagBuf(ch);
                    break;
                case INTAG:
                    htmlState = HTMLSTATE.INTAG;
                    if (ch == '>') {
                        start = pos + 1;
                        htmlState = HTMLSTATE.INTEXT;
                        filteredTag();
                        continue;
                    }
                    addTagBuf(ch);
                    continue;
                case SEENLTBANG:
                    if (ch != '-') {
                        htmlState = HTMLSTATE.INTAG;
                        if (ch == '>') {
                            start = pos + 1;
                            htmlState = HTMLSTATE.INTEXT;
                            filteredTag();
                            continue;
                        }
                        addTagBuf(ch);
                        continue;
                    }
                    addTagBuf(ch);
                    htmlState = HTMLSTATE.SEENLTBANGDASH;
                    continue;
                case SEENLTBANGDASH:
                    if (ch != '-') {
                        htmlState = HTMLSTATE.INTAG;
                        if (ch == '>') {
                            start = pos + 1;
                            htmlState = HTMLSTATE.INTEXT;
                            filteredTag();
                            continue;
                        }
                        addTagBuf(ch);
                        continue;
                    }
                    if (inStyleTag == 0 && inScriptTag == 0) {
                        filteredDelegate.filtered("<!--");
                    }
                    start = pos + 1;
                    htmlState = HTMLSTATE.INCOMMENT;
                    continue;
                case INCOMMENT:
                    if (ch == '-') {
                        htmlState = HTMLSTATE.INCOMMENTSEENDASH;
                    }
                    continue;
                case INCOMMENTSEENDASH:
                    htmlState = ch == '-' ? HTMLSTATE.INCOMMENTSEENDASHDASH : HTMLSTATE.INCOMMENT;
                    continue;
                case INCOMMENTSEENDASHDASH:
                    if (ch == '-') {
                        continue;
                    }
                    if (ch != '>') {
                        htmlState = HTMLSTATE.INCOMMENT;
                        continue;
                    }
                    if (inStyleTag == 0 && inScriptTag == 0 && start < len && pos + 1 > start) {
                        filteredDelegate.filtered(html.substring(start, pos + 1 > len ? len : pos + 1));
                    }
                    htmlState = HTMLSTATE.INTEXT;
                    start = pos + 1;
                    continue;
            }
        }

        switch (htmlState) {
            case INTEXT:
            case INCOMMENT:
            case INCOMMENTSEENDASH:
            case INCOMMENTSEENDASHDASH:
                if (inStyleTag == 0 && inScriptTag == 0 && start < len && pos > start) {
                    filteredDelegate.filtered(html.substring(start, pos));
                }
            default:
                break;
        }
    }

    private boolean isAttr(TagAttrInfo tai, String attr) {
        return (tai.tagNameLen == attr.length() && tai.tagNameStart < tagBufLen && tai.tagNameLen > 0 && tagBuf.substring(tai.tagNameStart, tai.tagNameStart + tai.tagNameLen > tagBufLen ? tagBufLen : tai.tagNameStart + tai.tagNameLen).compareToIgnoreCase(attr) == 0);
    }

    private boolean isHtmlTag(String tag) {
        return (tagAttrsLen > 0 ? isAttr(tagAttrs[0], tag) : false);
    }

    private boolean isValueStart(StringBuilder value, String prefix) {
        int i = 0, vlen = value.length(), plen = prefix.length();
        char ch;

        if (vlen <= 0 || plen <= 0) {
            return false;
        }
        for (; i < vlen; i++) {
            ch = value.charAt(i);
            if (!(ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ')) {
                break;
            }
        }
        if (i >= vlen) {
            return false;
        }
        return (value.substring(i, i + prefix.length() > vlen ? vlen : i + prefix.length()).compareToIgnoreCase(prefix) == 0);
    }

    private boolean isImageInput() {
        if (isHtmlTag("INPUT")) {
            int t = findAttr("type");
            if (t > 0) {
                StringBuilder s = new StringBuilder();
                if (tagAttrs[t].tagValueStart < tagBufLen && tagAttrs[t].tagValueLen > 0) {
                    s.append(tagBuf.substring(tagAttrs[t].tagValueStart, tagAttrs[t].tagValueStart + tagAttrs[t].tagValueLen > tagBufLen ? tagBufLen : tagAttrs[t].tagValueStart + tagAttrs[t].tagValueLen));
                }
                return (s.toString().trim().compareToIgnoreCase("image") == 0);
            }
        }
        return false;
    }

    private int findAttr(String attr) {
        for (int index = 1; index < tagAttrsLen; index++) {
            if (isAttr(tagAttrs[index], attr)) {
                return index;
            }
        }
        return 0;
    }

    private int parseAttr(TagAttrInfo[] tais) {
        TagAttrInfo tai = null;

        int count = 0;

        for (int i = 0; i < tagBufLen;) {
            char ch = tagBuf.charAt(i);
            for (; i < tagBufLen; i++) {
                ch = tagBuf.charAt(i);
                if (!(ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ')) {
                    break;
                }
            }
            if (i >= tagBufLen) {
                break;
            }
            if (tais != null) {
                tai = tais[count++];
            } else {
                ++count;
            }
            if (tais != null) {
                tai.tagNameStart = i;
                tai.tagNameLen = 0;
                tai.atagStart = i;
            }
            for (; i < tagBufLen; i++) {
                ch = tagBuf.charAt(i);
                if (ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ' || ch == '=') {
                    break;
                }
                if (tais != null) {
                    ++tai.tagNameLen;
                }
            }
            if (ch != '=') /* No attribute value */ {
                if (tais != null) {
                    tai.tagValueStart = i;
                    tai.tagValueLen = 0;
                }
            } else {
                char ch1;
                if (++i < tagBufLen) {
                    ch = tagBuf.charAt(i);
                }
                if (i < tagBufLen && ((ch1 = ch) == '"' || ch1 == '\'')) /* Attr value in quotes */ {
                    ++i;
                    if (tais != null) {
                        tai.tagValueStart = i;
                        tai.tagValueLen = 0;
                    }
                    for (; i < tagBufLen; i++) {
                        ch = tagBuf.charAt(i);
                        if (ch == ch1) {
                            break;
                        }
                        if (tais != null) {
                            ++tai.tagValueLen;
                        }
                    }
                    if (i < tagBufLen) {
                        i++;
                    }
                } else {
                    if (tais != null) {
                        tai.tagValueStart = i;
                        tai.tagValueLen = 0;
                    }
                    for (; i < tagBufLen; i++) {
                        ch = tagBuf.charAt(i);
                        if (ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ') {
                            break;
                        }
                        if (tais != null) {
                            ++tai.tagValueLen;
                        }
                    }
                }
            }
            if (tais != null) {
                tai.atagLen = i - tai.atagStart;
            }
        }
        return (count);
    }

    private void parseTagBuf() {
        int pos = 0;

        while ((pos = tagBuf.indexOf("<", pos)) >= 0) {
            tagBuf.replace(pos, pos + 1, " ");
        }

        tagAttrsLen = parseAttr(null);
        if (tagAttrsLen > tagAttrs.length) {
            TagAttrInfo newtais[] = new TagAttrInfo[tagAttrsLen + 16];
            for (int i = 0; i < tagAttrsLen + 16; i++) {
                newtais[i] = new TagAttrInfo();
            }
            tagAttrs = newtais;
        }
        parseAttr(tagAttrs);
    }

    private void filteredTag() {

        boolean openWindow = false;

        parseTagBuf();

        if (isHtmlTag("SCRIPT")) {
            ++inScriptTag;
            return;
        }

        if (isHtmlTag("/SCRIPT")) {
            if (inScriptTag > 0) {
                --inScriptTag;
            }
            return;
        }

        if (isHtmlTag("TITLE") || isHtmlTag("/TITLE")
                || isHtmlTag("FRAME") || isHtmlTag("/FRAME")
                || isHtmlTag("IFRAME") || isHtmlTag("/IFRAME")
                || isHtmlTag("APP") || isHtmlTag("/APP")
                || isHtmlTag("APPLET") || isHtmlTag("/APPLET")
                || isHtmlTag("SERVER") || isHtmlTag("/SERVER")
                || isHtmlTag("OBJECT") || isHtmlTag("/OBJECT")
                || isHtmlTag("HTML") || isHtmlTag("/HTML")
                || isHtmlTag("HEAD") || isHtmlTag("/HEAD")
                || isHtmlTag("BODY") || isHtmlTag("/BODY")
                || isHtmlTag("LINK") || isHtmlTag("META")) {
            return;
        }

        if (isHtmlTag("STYLE")) {
            ++inStyleTag;
            return;
        }

        if (isHtmlTag("/STYLE")) {
            if (inStyleTag > 0) {
                --inStyleTag;
            }
            return;
        }

        if (inStyleTag > 0 || inScriptTag > 0) {
            return;
        }

        for (int i = 1; i < tagAttrsLen; i++) {
            if (tagAttrs[i].tagNameLen > 2) {
                int start = tagAttrs[i].tagNameStart;
                if (start < tagBufLen && tagBuf.substring(start, start + 2 > tagBufLen ? tagBufLen : start + 2).compareToIgnoreCase("ON") == 0) {
                    fillSpace(tagBuf, tagAttrs[i].atagStart, tagAttrs[i].atagLen);
                }
            }
        }

        for (int i = 1; i < tagAttrsLen; i++) {
            if (isAttr(tagAttrs[i], "target")
                    || isAttr(tagAttrs[i], "code")
                    || isAttr(tagAttrs[i], "language")
                    || isAttr(tagAttrs[i], "action")
                    || isAttr(tagAttrs[i], "background")
                    || !keepStyle && isAttr(tagAttrs[i], "style")
                    || (isAttr(tagAttrs[i], "type")
                    && !isHtmlTag("BLOCKQUOTE"))
                    || isAttr(tagAttrs[i], "codetype")) {
                fillSpace(tagBuf, tagAttrs[i].atagStart, tagAttrs[i].atagLen);
            }

            if (keepStyle && isAttr(tagAttrs[i], "style")) {
                StringBuilder style = new StringBuilder();
                if (tagAttrs[i].tagValueStart < tagBufLen && tagAttrs[i].tagValueLen > 0) {
                    style.append(tagBuf.substring(tagAttrs[i].tagValueStart, tagAttrs[i].tagValueStart + tagAttrs[i].tagValueLen > tagBufLen ? tagBufLen : tagAttrs[i].tagValueStart + tagAttrs[i].tagValueLen));
                }
                replaceLink(i, filterStyle(style.toString()));
            } else if (isAttr(tagAttrs[i], "href")
                    || isAttr(tagAttrs[i], "src")
                    || isAttr(tagAttrs[i], "lowsrc")) {

                boolean goodHref = false;
                StringBuilder href = new StringBuilder();

                if (tagAttrs[i].tagValueStart < tagBufLen && tagAttrs[i].tagValueLen > 0) {
                    href.append(tagBuf.substring(tagAttrs[i].tagValueStart, tagAttrs[i].tagValueStart + tagAttrs[i].tagValueLen > tagBufLen ? tagBufLen : tagAttrs[i].tagValueStart + tagAttrs[i].tagValueLen));
                }

                for (int hi = 0, hn = href.length(); hi < hn; hi++) {
                    char ch = href.charAt(hi);
                    if (ch == '"' || ch == '<' || ch == '>') {
                        href.setLength(hi);
                        break;
                    }
                }

                if (isValueStart(href, "cid:")) {
                    if (cidFilteredDelegate != null && isAttr(tagAttrs[i], "src") && isHtmlTag("IMG")) {
                        goodHref = true;
                        doWashCid(i, href.toString());
                    }
                } else if (isValueStart(href, "http:")
                        || isValueStart(href, "https:")) {
                    if (isAttr(tagAttrs[i], "src") && isHtmlTag("IMG")) {
                        goodHref = true;
                        if (imageFilteredDelegate != null) {
                            replaceLink(i, imageFilteredDelegate.filteredImage(href.toString()));
                        } else {
                            replaceLink(i, href.toString());
                        }
                    } else if (isAttr(tagAttrs[i], "href")) {
                        goodHref = true;
                        if (washLink != null) {
                            doWashLink(i, href.toString());
                        } else {
                            replaceLink(i, href.toString());
                        }
                        if (isHtmlTag("A")) {
                            openWindow = true;
                        }
                    }
                } else if (isValueStart(href, "mailto:")) {
                    goodHref = true;
                    if (washMailto != null) {
                        doWashMailto(i, href.substring(href.indexOf(":") + 1));
                    } else {
                        replaceLink(i, href.toString());
                    }
                } else if (isValueStart(href, "ftp:")
                        || isValueStart(href, "gopher:")
                        || isValueStart(href, "wais:")
                        || isValueStart(href, "telnet:")) {
                    goodHref = true;
                    if (isHtmlTag("A")) {
                        openWindow = true;
                    }
                } else if (isAttr(tagAttrs[i], "src") && isHtmlTag("IMG")) {
                        goodHref = true;
                        if (imageFilteredDelegate != null) {
                            replaceLink(i, imageFilteredDelegate.filteredImage(href.toString()));
                        } else {
                            replaceLink(i, href.toString());
                        }
                    }
                else {
                    if (safeUrls != null) {
                        for (int si = 0, sn = safeUrls.length; si < sn; si++) {
                            String sl = safeUrls[si];
                            if (sl != null && isValueStart(href, sl)) {
                                goodHref = true;
                                if (isHtmlTag("A")) {
                                    openWindow = true;
                                }
                                break;
                            }
                        }
                    }
                }
                if (!goodHref) {
                    fillSpace(tagBuf, tagAttrs[i].atagStart, tagAttrs[i].atagLen);
                }
            }
        }
        filteredDelegate.filtered("<");
        filteredDelegate.filtered(tagBuf.toString());
        if (openWindow) {
            filteredDelegate.filtered(" target=\"_blank\"");
        }
        filteredDelegate.filtered(">");
    }

    public static StringBuilder decodeHtml(String p) {
        StringBuilder q = new StringBuilder();
        int i;

        for (; p.length() > 0;) {

            if (p.charAt(0) != '&') {
                q.append(p.charAt(0));
                p = p.substring(1);
                continue;
            }

            if (p.length() > 1 && p.charAt(1) == '#') {
                long c = 0;
                p = p.substring(2);
                for (; p.length() > 0 && Character.isDigit(p.charAt(0)); p = p.substring(1)) {
                    c = c * 10 + (p.charAt(0) - '0');
                }
                c = (char) c;
                if (c != 0) {
                    q.append((char) c);
                }
                if (p.length() > 0 && p.charAt(0) == ';') {
                    p = p.substring(1);
                }
                continue;
            }

            for (i = 1; i < p.length(); i++) {
                if (!Character.isLetter(p.charAt(i))) {
                    break;
                }
            }
            if (i >= p.length() || p.charAt(i) != ';') {
                q.append(p.charAt(0));
                p = p.substring(1);
                continue;
            }
            String s = p.substring(0, ++i);
            if (s.compareToIgnoreCase("&lt;") == 0) {
                q.append('<');
            } else if (s.compareToIgnoreCase("&gt;") == 0) {
                q.append('>');
            } else if (s.compareToIgnoreCase("&amp;") == 0) {
                q.append('&');
            } else if (s.compareToIgnoreCase("&quot;") == 0) {
                q.append('"');
            }
            p = p.substring(i);
        }
        return q;
    }

    private String redirectEncode(String p) {
        try {
            return URLEncoder.encode(decodeHtml(p).toString(), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    private void replaceLink(int index, String link) {
        TagAttrInfo tai = tagAttrs[index];
        StringBuilder newbuf = new StringBuilder();

        newbuf.append(tagBuf.substring(0, tai.tagValueStart > tagBufLen ? tagBufLen : tai.tagValueStart));
        newbuf.append(link);
        if (tai.tagValueStart + tai.tagValueLen < tagBufLen) {
            newbuf.append(tagBuf.substring(tai.tagValueStart + tai.tagValueLen));
        }

        tagBuf.setLength(0);
        tagBuf.append(newbuf);
        tagBufLen = tagBuf.length();
        parseTagBuf();
    }

    private void doWashLink(int index, String link) {
        String ll = link.toLowerCase();
        if (washLinkExcluded != null && washLinkExcluded.length > 0) {
            for (int i = 0; i < washLinkExcluded.length; i++) {
                String s = washLinkExcluded[i];
                if (s == null) {
                    continue;
                }
                if (ll.startsWith(s.toLowerCase())) {
                    replaceLink(index, link);
                    return;
                }
            }
        }
        if (ll.startsWith(washLink.toLowerCase())) {
            replaceLink(index, link);
        } else {
            replaceLink(index, washLink + redirectEncode(link));
        }
    }

    private void doWashMailto(int index, String mailto) {
        String ll = mailto.toLowerCase();
        if (washMailtoExcluded != null && washMailtoExcluded.length > 0) {
            for (int i = 0; i < washMailtoExcluded.length; i++) {
                String s = washMailtoExcluded[i];
                if (s == null) {
                    continue;
                }
                if (ll.startsWith(s.toLowerCase())) {
                    replaceLink(index, "mailto:" + mailto);
                    return;
                }
            }
        }
        if (ll.startsWith(washMailto.toLowerCase())) {
            replaceLink(index, "mailto:" + mailto);
        } else {
            replaceLink(index, washMailto + mailto.replace('?', '&'));
        }
    }

    private void doWashCid(int index, String cid) {
        if (cidFilteredDelegate == null) {
            cid = "";
        } else {
            if (cid.length() > 4) {
                cid = cidFilteredDelegate.filteredCid(cid.substring(4));
            }
        }
        replaceLink(index, cid);
    }

    private void fillSpace(StringBuilder sb, int start, int len) {
        if (len > 0) {
            byte[] b = new byte[len];
            Arrays.fill(b, (byte) ' ');
            String s = new String(b);
            sb.replace(start, start + len, s);
        }
    }

    private String filterStyleAttrValue(StringBuilder attrName, StringBuilder attrValue) {
        if (attrValue.length() > 11 && attrValue.substring(0, 11).compareToIgnoreCase("expression(") == 0) {
            return (" ");
        } else if (attrValue.length() > 4 && attrValue.substring(0, 4).compareToIgnoreCase("url(") == 0) {
            String s = attrValue.substring(4);
            {
                int i;
                if ((i = s.indexOf(")")) > 0) {
                    s = s.substring(0, i);
                }
            }
            s = s.trim();
            int len = s.length();
            if (len > 0 && s.charAt(0) == '\"' || len >= 6 && s.substring(0, 6).compareToIgnoreCase("&quot;") == 0) {
                int i = s.indexOf("\"", 1);
                int i1 = s.indexOf("&quot;", 6);
                if (i < 6 && i1 < 1) {
                    i = len;
                } else if (i < 6 || i1 < 1) {
                    if (i < 6) {
                        i = i1;
                    }
                } else {
                    if (i > i1) {
                        i = i1;
                    }
                }
                if (s.charAt(0) == '\"') {
                    s = s.substring(1, i);
                } else {
                    s = s.substring(6, i);
                }
            } else if (len > 0 && s.charAt(0) == '\'') {
                int i = 0;
                char ch = s.charAt(0);
                if (ch == '\"' || ch == '\'') {
                    char ch1 = ch;
                    for (i++; i < len; i++) {
                        ch = s.charAt(i);
                        if (ch == ch1) {
                            break;
                        }
                    }
                    if (i < len) {
                        if (i > 1) {
                            s = s.substring(1, i);
                        } else {
                            s = "";
                        }
                    }
                }
            }
            s = s.trim();
            len = s.length();
            String sl = s.toLowerCase();

            if (sl.startsWith("http:") || sl.startsWith("https:")) {
                if (imageFilteredDelegate != null) {
                    String name = attrName.toString().trim().toLowerCase();
                    if (name.compareToIgnoreCase("background-image") == 0 || name.compareToIgnoreCase("background") == 0) {
                        sl = imageFilteredDelegate.filteredImage(s.toString());
                        if(sl == null || sl.trim().isEmpty())
                            return (" ");
                        return ("url(" + sl + ")");
                    }
                }
                return attrValue.toString();
            }
            if (safeUrls != null) {
                for (int si = 0, sn = safeUrls.length; si < sn; si++) {
                    sl = safeUrls[si];
                    if (sl != null && s.startsWith(sl)) {
                        return attrValue.toString();
                    }
                }
            }
            return (" ");
        }
        return attrValue.toString();
    }

    private String filterStyle(String style) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, len = style.length(); i < len; i++) {
            char ch = style.charAt(i);
            if (ch == '/' && i + 1 < len) {
                ch = style.charAt(i + 1);
                if (ch == '*') {
                    i += 2;
                    for (; i < len; i++) {
                        ch = style.charAt(i);
                        if (ch == '*' && i + 1 < len) {
                            ch = style.charAt(i + 1);
                            if (ch == '/') {
                                i++;
                                break;
                            }
                        }
                    }
                    if (i >= len) {
                        break;
                    }
                } else {
                    sb.append('/');
                }
            } else {
                sb.append(ch);
            }
        }
        style = sb.toString();
        sb.setLength(0);

        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        ArrayList<String> values = new ArrayList<String>();

        for (int i = 0, len = style.length(); i < len; i++) {
            char ch = style.charAt(i);
            if (ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ') {
                for (i++; i < len; i++) {
                    ch = style.charAt(i);
                    if (!(ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ')) {
                        break;
                    }
                }
                if (i >= len) {
                    break;
                }
            }
            for (; i < len; i++) {
                ch = style.charAt(i);
                if (ch == '\'' || ch == '\"') {
                    char ch1 = ch;
                    name.append(ch);
                    i++;
                    for (; i < len; i++) {
                        ch = style.charAt(i);
                        name.append(ch);
                        if (ch == ch1) {
                            break;
                        }
                    }
                    if (i < len) {
                        continue;
                    } else {
                        name.append(ch1);
                        break;
                    }
                } else if (ch == ':') {
//================================================
                    i++;
                    for (; i < len; i++) {
                        ch = style.charAt(i);
                        if (ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ') {
                            for (i++; i < len; i++) {
                                ch = style.charAt(i);
                                if (!(ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ')) {
                                    break;
                                }
                            }
                            if (i >= len) {
                                break;
                            }
                        }
                        int level = 0;
                        for (; i < len; i++) {
                            ch = style.charAt(i);
                            if (ch == '\'' || ch == '\"') {
                                char ch1 = ch;
                                value.append(ch);
                                i++;
                                for (; i < len; i++) {
                                    ch = style.charAt(i);
                                    value.append(ch);
                                    if (ch == ch1) {
                                        break;
                                    }
                                }
                                if (i < len) {
                                    continue;
                                } else {
                                    value.append(ch);
                                    break;
                                }
                            } else if (ch == '(') {
                                value.append(ch);
                                level++;
                            } else if (ch == ')') {
                                value.append(ch);
                                if (level > 0) {
                                    level--;
                                }
                            } else if (level == 0 && (ch == ';' || ch == ',' || ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ')) {
                                if (ch == '\t' || ch == '\r' || ch == '\n' || ch == ' ') {
                                    value.append(' ');
                                } else if (ch == ',') {
                                    value.append(',');
                                }
                                break;
                            } else {
                                value.append(ch);
                            }
                        }
                        if (value.length() > 0) {
                            if (level == 0) {
                                values.add(filterStyleAttrValue(name, value));
                            }
                            value.setLength(0);
                        }
                        level = 0;
                        if (i >= len || ch == ';') {
                            break;
                        }
                    }
                    if ((name.length() < 8 || name.substring(0, 8).compareToIgnoreCase("behavior") != 0)
                            && (name.length() < 12 || name.substring(0, 12).compareToIgnoreCase("-moz-binding") != 0)) {
                        if (name.length() > 0) {
                            sb.append(name);
                            name.setLength(0);
                        }
                        sb.append(":");
                        for (String v : values) {
                            if (v.length() > 0) {
                                sb.append(v);
                            }
                        }
                        sb.append(";");
                    } else {
                        name.setLength(0);
                    }
                    values.clear();
                    break;
//==================================================                    
                } else if (ch == ';') {
                    break;
                } else {
                    name.append(ch);
                }
            }
            if (name.length() > 0) {
                if ((name.length() < 8 || name.substring(0, 8).compareToIgnoreCase("behavior") != 0)
                        && (name.length() < 12 || name.substring(0, 12).compareToIgnoreCase("-moz-binding") != 0)) {
                    sb.append(name);
                    sb.append(';');
                }
                name.setLength(0);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        final StringBuilder sb = new StringBuilder();

        HtmlFilter.FilteredDelegate d = new HtmlFilter.FilteredDelegate() {

            public void filtered(String s) {
                sb.append(s);
            }
        };

        HtmlFilter.ImageFilteredDelegate imgFilter = new HtmlFilter.ImageFilteredDelegate() {

            public String filteredImage(String src) {
                // �����ⲿͼƬ
                return src + "?ooo";
            }
        };

        HtmlFilter filter = new HtmlFilter(d, imgFilter, true);
        filter.washLink = "http://xxx/goto?url=";
        String safeUrls[] = {"/", "./", "../"};
        //filter.setSafeUrls(safeUrls);
//        filter.washMailto = "webmaster@ciipp.com?orginto=";

        StringBuilder html = new StringBuilder();
        html.append("<p><img style=\"display： block;margin-left： auto;margin-right： auto;\" src=\"../../XSiteData/admin/PrepaidCard/slshlm/sh/cyms/a/e54412_634702028301241208.jpg\" alt=\"\" /></p>");
//        StringBuilder html = new StringBuilder("<html><body><input type=image src=&quot;abc.do&quot;><script>hello me</script><div onerror='alert(&quot;error&quot;)' style=\"behavior:url(&quot;behave_typing.htc&quot;);background:url(&quot;http://behave_typing.htc&quot;);color:red\" onclick='alert();'>mmmm</div><div style=\'font: \"����\" 10px underline; color: expression(alert(\"okkk\")); background: url(\"someur\"); z-order:999\'>hello</div><img src=/rs/aaa.gif onclick=ccccc alt=ccc><a href=\"http://goto/?d=sss\">aaa</a><img src=http://aaa.gif onclick=asdad alt=aaa>hello<a href=http://aaa.do?cmd=�й�&abc=&#29664;&#28023;>linkaaa</a><img src=http://aaa.gif alt=aaa></body></html>");
        filter.filter(html);


        System.out.print(sb);
    }
}
