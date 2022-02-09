/*
 * Jexer - Java Text User Interface
 *
 * The MIT License (MIT)
 *
 * Copyright (C) 2022 Autumn Lamonte
 * Copyright (C) 2012 Matt Kwan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package jexer.xserver;

import java.io.IOException;

/**
 * All the X error codes.
 *
 * @author Matthew Kwan
 */
public class ErrorCode {
    public static final byte None = 0;
    public static final byte Request = 1;
    public static final byte Value = 2;
    public static final byte Window = 3;
    public static final byte Pixmap = 4;
    public static final byte Atom = 5;
    public static final byte Cursor = 6;
    public static final byte Font = 7;
    public static final byte Match = 8;
    public static final byte Drawable = 9;
    public static final byte Access = 10;
    public static final byte Alloc = 11;
    public static final byte Colormap = 12;
    public static final byte GContext = 13;
    public static final byte IDChoice = 14;
    public static final byte Name = 15;
    public static final byte Length = 16;
    public static final byte Implementation = 17;

    /**
     * Write an X error.
     *
     * @param client     The remote client.
     * @param error      The error code.
     * @param opcode     The opcode of the error request.
     * @param resourceId The (optional) resource ID that caused the error.
     * @throws IOException
     */
    public static void write(Client client, byte error, byte opcode,
        int resourceId) throws IOException {

        writeWithMinorOpcode(client, error, (short) 0, opcode, resourceId);
    }

    /**
     * Write an X error with a minor opcode specified.
     *
     * @param client      The remote client.
     * @param error       The error code.
     * @param minorOpcode The minor opcode of the error request.
     * @param opcode      The major opcode of the error request.
     * @param resourceId  The (optional) resource ID that caused the error.
     * @throws IOException
     */
    public static void writeWithMinorOpcode(Client client, byte error,
        short minorOpcode, byte opcode, int resourceId) throws IOException {

        InputOutput io = client.getInputOutput();
        short sn = (short) (client.getSequenceNumber() & 0xffff);

        synchronized (io) {
            io.writeByte((byte) 0);    // Indicates an error.
            io.writeByte(error);        // Error code.
            io.writeShort(sn);            // Sequence number.
            io.writeInt(resourceId);    // Bad resource ID.
            io.writeShort(minorOpcode);    // Minor opcode.
            io.writeByte(opcode);        // Major opcode.
            io.writePadBytes(21);        // Unused.
        }
        io.flush();
    }
}
