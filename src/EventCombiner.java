package com.gsta.neva2.business.service.event.database;

import com.gsta.neva2.business.service.appcode.Context;
import com.gsta.neva2.business.utils.Logger;
import com.gsta.neva2.business.utils.StringFormatter;
import com.gsta.neva2.dal.Session;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: wangsongwei
 * Date: Sep 15, 2011
 * Time: 3:51:25 PM
 * name: PACKAGE_NAME
 */
public class EventCombiner {

    private static final int VERSION = 1; // version
    private static final int REQUEST = 0; // Request type: request, response, heartbeat

    private Set semantics = new LinkedHashSet();

    public void addSemantic(Semantic semantic) {
        this.semantics.add(semantic);
    }

    public int setSize() {
        return semantics.size();
    }

    public void saveMessages(Connection conn, String account, long spec)
            throws SQLException, CombinerException, IOException {
        String sql = "INSERT INTO t_obm_message VALUES (?, seq_obm_message_id.nextval, ?, sysdate, ?, ?)";

        conn.setAutoCommit(false);
        PreparedStatement pstmt = conn.prepareCall(sql);

        pstmt.setInt(1, 99);
        pstmt.setBytes(2, pack(99, eventGroup(account)).toByteArray());
        pstmt.setString(3, account);
        pstmt.setLong(4, spec);
        pstmt.executeUpdate();

        conn.commit();
        conn.close();

        Context.clearSemantics();
    }

    public void saveMessages(Session session, String account, long spec)
            throws SQLException, IOException, CombinerException {
        String sql = "INSERT INTO t_obm_message VALUES (?, seq_obm_message_id.nextval, ?, sysdate, ?, ?)";

        Connection conn = session.getConnection();
        PreparedStatement pstmt = conn.prepareCall(sql);

        pstmt.setInt(1, 99);
        pstmt.setBytes(2, pack(99, eventGroup(account)).toByteArray());
        pstmt.setString(3, account);
        pstmt.setLong(4, spec);
        pstmt.executeUpdate();
        pstmt.close();

        Context.clearSemantics();
    }

    public ByteArrayOutputStream pack(int eventType, int eventGroup) throws IOException, CombinerException {

        ByteArrayOutputStream semantic_stream = new ByteArrayOutputStream();
        DataOutputStream semantic_part = new DataOutputStream(semantic_stream);

        ByteArrayOutputStream attribute_stream = new ByteArrayOutputStream();
        DataOutputStream attribute_part = new DataOutputStream(attribute_stream);

        ByteArrayOutputStream package_stream = new ByteArrayOutputStream();
        DataOutputStream event_package = new DataOutputStream(package_stream);

        int offset = 26;

        Iterator sit = semantics.iterator();
        while (sit.hasNext()) {
            Semantic semantic = (Semantic) sit.next();
            offset += semantic.attrValues.size() * 5 + 3;
        }

        int attrLength = 0;
        sit = semantics.iterator();
//        for (Semantic semantic : semantics) {
        while (sit.hasNext()) {
            Semantic semantic = (Semantic) sit.next();

            semantic_part.writeShort(semantic.semantic_id); // Semantic id, 2bytes
            semantic_part.writeByte(semantic.attrValues.size()); // attribute count, 1bytes

            Iterator ait = semantic.attrValues.entrySet().iterator();

            Logger.getInstance().debug(
                    new StringFormatter(25, StringFormatter.LEFT).format("    SEMANTIC").append(" ->   ")
                            .append(new StringFormatter(3, StringFormatter.LEFT).
                                    format(String.valueOf(semantic.semantic_id))), "EventCombiner");
            while (ait.hasNext()) {
                Map.Entry entry = (Map.Entry) ait.next();
                AttributeValue attrValue = (AttributeValue) entry.getValue();
                Logger.getInstance().debug(
                        new StringFormatter(25, StringFormatter.LEFT).format("    " + String.valueOf(entry.getKey())).append(" ::   ")
                                .append(new StringFormatter(35, StringFormatter.LEFT).
                                        format(String.valueOf(attrValue.value))), "EventCombiner");
                Attribute attribute = Dictionary.getAttribute((String) entry.getKey());
                semantic_part.writeShort(attribute.code); // Attribute code, 2bytes
                offset += attrLength;
                semantic_part.writeShort(offset); // offset, 2bytes

                if (attribute.type == AttributeType.Integer32) {
                    attribute_part.writeInt(attrValue.getIntValue());
                    attrLength = 4;
                } else if (attribute.type == AttributeType.Integer64) {
                    attribute_part.writeLong(attrValue.getLongValue());
                    attrLength = 8;
                } else if (attribute.type == AttributeType.STRING) {
                    attrLength = attrValue.getStringValue().length(); // this string must not be chinese character
                    for (int i = 0; i < attrLength; i++) attribute_part.writeByte(attrValue.getStringValue().charAt(i));
                } else
                    throw new CombinerException("attribute type not correct.");

                semantic_part.writeByte(attrLength); // length, 1bytes
            }
        }
        int packetLength = semantic_stream.toByteArray().length + attribute_stream.toByteArray().length + 26;


        EventHeader eventHeader = new EventHeader(VERSION, REQUEST, eventType, packetLength, eventGroup, semantics.size(), "");
        event_package.write(eventHeader.toByteArray());
        event_package.write(semantic_stream.toByteArray());
        event_package.write(attribute_stream.toByteArray());

        return package_stream;
    }

    public void clear() {
        semantics.clear();
    }

    public static int eventGroup(String account) {

        return (int) eventGroup(account, 100);
    }

    public static long eventGroup(String account, int mod) {
        long number = 0;
//        for (char c : account.toCharArray()) number = 5 * number + (int) c;
//        return number % mod;
        char[] chars = account.toCharArray();
        for (int i = 0; i < chars.length; i++) number = 5 * number + (int) chars[i];
        return number % mod;
    }

    public class EventHeader {
        int version;
        int request_type;
        int event_type;
        int packet_length;
        int event_group;
        int semantic_count;
        String verify;

        public EventHeader(int version, int request_type, int event_type,
                           int packet_length, int event_group, int semantic_count, String verify) {
            this.version = version;
            this.request_type = request_type;
            this.event_type = event_type;
            this.packet_length = packet_length;
            this.event_group = event_group;
            this.semantic_count = semantic_count;
            this.verify = verify;
        }

        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(stream);

            dos.writeByte(this.version);
            dos.writeByte(this.request_type);
            dos.writeShort(this.event_type);
            dos.writeShort(this.event_group);
            dos.writeShort(this.packet_length);
            dos.writeShort(this.semantic_count);
            dos.write(new byte[16]); // MD5 field, keep
            return stream.toByteArray();
        }
    }
}
