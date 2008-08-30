package com.meidusa.amoeba.oracle.net.packet;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.meidusa.amoeba.net.packet.AbstractPacketBuffer;

/**
 * @author hexianmao
 * @version 2008-8-11 ����04:17:45
 */
public class AnoDataPacket extends DataPacket implements AnoServices {

    private static Logger logger         = Logger.getLogger(AnoDataPacket.class);

    public int            m;
    public long           version;
    public int            anoServiceSize = SERV_INORDER_CLASSNAME.length;
    public short          h;
    public AnoService[]   anoService;

    @Override
    protected void init(AbstractPacketBuffer absbuffer) {
        super.init(absbuffer);
        AnoPacketBuffer buffer = (AnoPacketBuffer) absbuffer;
        if (buffer.readUB4() != NA_MAGIC) {
            throw new RuntimeException("Wrong Magic number in na packet");
        }
        m = buffer.readUB2();
        version = buffer.readUB4();
        anoServiceSize = buffer.readUB2();
        h = buffer.readUB1();

        /*
         * anoService = new AnoService[anoServiceSize]; try { String pkgPrefix = "com.meidusa.amoeba.oracle.packet.";
         * for (int i = 0; i < SERV_INORDER_CLASSNAME.length; i++) { anoService[i] = (AnoService)
         * Class.forName(pkgPrefix + SERV_INORDER_CLASSNAME[i]).newInstance(); anoService[i].doRead(buffer); } } catch
         * (Exception e) { throw new RuntimeException(); }
         */

        if (logger.isDebugEnabled()) {
            logger.debug(this.toString());
        }
    }

    public AnoService[] getAnoService() {
        return anoService;
    }

    @Override
    protected void write2Buffer(AbstractPacketBuffer absbuffer) throws UnsupportedEncodingException {
        super.write2Buffer(absbuffer);
        AnoPacketBuffer buffer = (AnoPacketBuffer) absbuffer;
        buffer.writeUB4(NA_MAGIC);
        buffer.writeUB2(m);
        buffer.writeUB4(version);
        buffer.writeUB2(anoServiceSize);
        buffer.writeUB1(h);
        if (anoService == null && anoServiceSize > 0) {
            anoService = new AnoService[anoServiceSize];
            try {
                String pkgPrefix = "com.meidusa.amoeba.oracle.packet.";
                for (int i = 0; i < SERV_INORDER_CLASSNAME.length; i++) {
                    anoService[i] = (AnoService) Class.forName(pkgPrefix + SERV_INORDER_CLASSNAME[i]).newInstance();
                    anoService[i].doWrite(buffer);
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    }

    @Override
    protected Class<? extends AbstractPacketBuffer> getPacketBufferClass() {
        return AnoPacketBuffer.class;
    }

    public static boolean isAnoType(byte[] buffer) {
        if (buffer != null && buffer.length > 13) {
            long l = 0L;
            int i = 10;
            l |= ((buffer[i++] & 0xff) << 24);
            l |= ((buffer[i++] & 0xff) << 16);
            l |= ((buffer[i++] & 0xff) << 8);
            l |= (buffer[i++] & 0xff);
            l &= -1L;
            return (l == NA_MAGIC);
        } else {
            return false;
        }

    }

}
