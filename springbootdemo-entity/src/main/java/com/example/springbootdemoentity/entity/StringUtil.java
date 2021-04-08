/*****************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one                *
 * or more contributor license agreements.  See the NOTICE file              *
 * distributed with this work for additional information                     *
 * regarding copyright ownership.  The ASF licenses this file                *
 * to you under the Apache License, Version 2.0 (the                         *
 * "License"); you may not use this file except in compliance                *
 * with the License.  You may obtain a copy of the License at                *
 *                                                                           *
 *     http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                           *
 * Unless required by applicable law or agreed to in writing,                *
 * software distributed under the License is distributed on an               *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                    *
 * KIND, either express or implied.  See the License for the                 *
 * specific language governing permissions and limitations                   *
 * under the License.                                                        *
 *                                                                           *
 *                                                                           *
 * This file is part of the BeanShell Java Scripting distribution.           *
 * Documentation and updates may be found at http://www.beanshell.org/       *
 * Patrick Niemeyer (pat@pat.net)                                            *
 * Author of Learning Java, O'Reilly & Associates                            *
 *                                                                           *
 *****************************************************************************/


package com.example.springbootdemoentity.entity;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtil {

    public static String[] split(String s, String delim) {
        Vector v = new Vector();
        StringTokenizer st = new StringTokenizer(s, delim);
        while (st.hasMoreTokens())
            v.addElement(st.nextToken());
        String[] sa = new String[v.size()];
        v.copyInto(sa);
        return sa;
    }

    public static String appendStrs(Object... strings) {
        StringBuffer sb = new StringBuffer();
        for (Object str : strings) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String[] bubbleSort(String[] in) {
        Vector v = new Vector();
        for (int i = 0; i < in.length; i++)
            v.addElement(in[i]);

        int n = v.size();
        boolean swap = true;
        while (swap) {
            swap = false;
            for (int i = 0; i < (n - 1); i++)
                if (((String) v.elementAt(i)).compareTo(
                        ((String) v.elementAt(i + 1))) > 0) {
                    String tmp = (String) v.elementAt(i + 1);
                    v.removeElementAt(i + 1);
                    v.insertElementAt(tmp, i);
                    swap = true;
                }
        }

        String[] out = new String[n];
        v.copyInto(out);
        return out;
    }


    public static String maxCommonPrefix(String one, String two) {
        int i = 0;
        while (one.regionMatches(0, two, 0, i))
            i++;
        return one.substring(0, i - 1);
    }

    public static String methodString(String name, Class[] types) {
        StringBuffer sb = new StringBuffer(name + "(");
        if (types.length > 0)
            sb.append(" ");
        for (int i = 0; i < types.length; i++) {
            Class c = types[i];
            sb.append(((c == null) ? "null" : c.getName())
                    + (i < (types.length - 1) ? ", " : " "));
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     Split a filename into dirName, baseName
     @return String [] { dirName, baseName }
     public String [] splitFileName( String fileName )
     {
     String dirName, baseName;
     int i = fileName.lastIndexOf( File.separator );
     if ( i != -1 ) {
     dirName = fileName.substring(0, i);
     baseName = fileName.substring(i+1);
     } else
     baseName = fileName;

     return new String[] { dirName, baseName };
     }

     */

    /**
     * Hack - The real method is in Reflect.java which is not public.
     */


    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String image) {
        return !isBlank(image);
    }


    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }


    public static boolean isNumber(String str) {
        if (str.length() == 0) {
            return false;
        }
        int sz = str.length();
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (str.charAt(0) == '-') ? 1 : 0;
        if (sz > start + 1) {
            if (str.charAt(start) == '0' && str.charAt(start + 1) == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < str.length(); i++) {
                    char ch = str.charAt(i);
                    if ((ch < '0' || ch > '9')
                            && (ch < 'a' || ch > 'f')
                            && (ch < 'A' || ch > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            char ch = str.charAt(i);
            if (ch >= '0' && ch <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (ch == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (ch == 'e' || ch == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (ch == '+' || ch == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < str.length()) {
            char ch = str.charAt(i);

            if (ch >= '0' && ch <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (ch == 'e' || ch == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (!allowSigns
                    && (ch == 'd'
                    || ch == 'D'
                    || ch == 'f'
                    || ch == 'F')) {
                return foundDigit;
            }
            if (ch == 'l'
                    || ch == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }


    public static String getString(Object obj) {
        StringBuilder sb = new StringBuilder();
        if (obj instanceof List) {
            sb.append("[");
            int i = 0;
            for (Object o : (List<Object>) obj) {
                appendObject(sb, o);
                if (i < ((List<Object>) obj).size() - 1) {
                    sb.append(" ,");
                }
                i++;
            }
            sb.append("]");
        } else if (obj instanceof Map) {
            sb.append("{");
            int i = 0;
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) obj).entrySet()) {
                if (i > 0) {
                    sb.append(",");
                }
                Object key = entry.getKey();
                Object value = entry.getValue();
                appendObject(sb, key);
                sb.append(": ");
                appendObject(sb, value);
                i++;
            }
            sb.append("}");
        } else {
            sb.append(obj);
        }
        return sb.toString();
    }

    public static void appendObject(StringBuilder sb, Object o) {
        if (o instanceof List || o instanceof Map) {
            sb.append(getString(o));
        } else {
            if (o instanceof String) {
                sb.append("'").append(o).append("'");
            } else {
                sb.append(o);
            }
        }
    }

}
