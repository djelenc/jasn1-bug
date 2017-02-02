package com.david;


import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerEnum;
import org.openmuc.jasn1.ber.types.string.BerPrintableString;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        final MyMessage orig = new MyMessage(new BerEnum(0),
                new BerPrintableString("a".getBytes()));

        final BerByteArrayOutputStream baos = new BerByteArrayOutputStream(100, true);
        try {
            orig.encode(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("Encoded %s as %s%n", orig, Arrays.toString(baos.getArray()));

        final MyMessage decoded = new MyMessage();

        try {
            decoded.decode(new ByteArrayInputStream(baos.getArray()));
        } catch (IOException e) {
            System.out.printf("Could not decode %s: %s%n", Arrays.toString(baos.getArray()), e.getMessage());
        }

        final byte[] correctEncoding = {106, 6, 10, 1, 0, 19, 1, 97};

        try {
            decoded.decode(new ByteArrayInputStream(correctEncoding));
            System.out.printf("Decoded %s as %s%n", Arrays.toString(correctEncoding), decoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
