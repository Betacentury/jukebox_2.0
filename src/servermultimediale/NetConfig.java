/*
 * Copyright (C) 2015 betacentury
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package servermultimediale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author betacentury
 */
public class NetConfig {
    private final String appname, version;
    private final Integer port;
    
    public NetConfig (byte [] _in) {
        String temp = new String(_in);
        Pattern p = Pattern.compile("^.*" +
            abbracci( abbracci("(.*)","<appname>"),".*") +
            abbracci( abbracci("(.*)","<version>"),".*") +
            abbracci( abbracci("([0-9]*)","<port>"),".*") +
            "$");
        Matcher m = p.matcher(temp);
        System.out.println(temp);
        if (m.find()) {
            appname = m.group(1);
            version = m.group(2);
            port = Integer.parseInt(m.group(3));
        } else {
            appname = version = null;
            port = null;
        }
    }
    public NetConfig (String _application, String _version, Integer _port) {
        appname = _application;
        version = _version;
        port = _port;
    }
    public String getApplication() { return appname; }
    public String getVersion() { return version; }
    public Integer getPort() { return port; }
    @Override
    public String toString() {
        return abbracci(appname,"<appname>") + abbracci(version, "<version>") + abbracci(port.toString(), "<port>");
    }
    private static String abbracci(String _value, String _brack) {
        StringBuilder out = new StringBuilder(_brack);
        out.append(_value).append(_brack);
        return out.toString();
    }
}
