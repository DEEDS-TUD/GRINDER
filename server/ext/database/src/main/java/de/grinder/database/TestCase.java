package de.grinder.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "testcases")
public class TestCase {

    public final static int MAX_KSERVICE_LENGTH = 32;
    public final static String SEPERATOR = " ";

    @Id
    @GeneratedValue
    private int id;

    @Column(length = 255)
    private String module;

    @Column(length = 255)
    private String kservice;

    @Column(columnDefinition = "SMALLINT")
    private short parameter;

    @Column(columnDefinition = "SMALLINT")
    private short bit;

    public TestCase() {
        super();
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public String getKservice() {
        return kservice;
    }

    public void setKservice(final String kservice) {
        this.kservice = kservice;
    }

    public short getParameter() {
        return parameter;
    }

    public void setParameter(final short parameter) {
        this.parameter = parameter;
    }

    public short getBit() {
        return bit;
    }

    public void setBit(final short bit) {
        this.bit = bit;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("TestCase [id=%d]", id);
    }

    public String getConfiguration() {
        return module + SEPERATOR + kservice + SEPERATOR + parameter + SEPERATOR + bit;
    }

    /**
     * Returns a {@value #MAX_KSERVICE_LENGTH} byte array, constructed as follows
     * (msB->lsB): {@value #MAX_KSERVICE_LENGTH} byte - kservice name; 2 byte - parameter
     * position; 2 byte - bit position
     *
     * @return
     * @throws KserviceNameToLongException
     */
    public byte[] getInjectionParameters() throws KserviceNameToLongException {
        final byte[] result = new byte[MAX_KSERVICE_LENGTH + 4];
        final byte[] kserviceBytes = kservice.intern().getBytes();

        // kservice needs to be smaller then MAX_KSERVICE_LENGTH - 1 (the
        // null-terminator need to be delivered as well)
        if (kserviceBytes.length > MAX_KSERVICE_LENGTH - 1) {
            throw new KserviceNameToLongException();
        }

        for (int i = 0; i < kserviceBytes.length; i++) {
            result[i] = kserviceBytes[i];
        }
        result[MAX_KSERVICE_LENGTH] = (byte) (parameter >>> 8);
        result[MAX_KSERVICE_LENGTH + 1] = (byte) parameter;
        result[MAX_KSERVICE_LENGTH + 2] = (byte) (bit >>> 8);
        result[MAX_KSERVICE_LENGTH + 3] = (byte) bit;
        return result;
    }

    public class KserviceNameToLongException extends Exception {

        private static final long serialVersionUID = 1869870117076394322L;

    }
}
