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
package jexer.xserver.Xext;

import java.io.IOException;

import jexer.xserver.Client;
import jexer.xserver.ErrorCode;
import jexer.xserver.InputOutput;
import jexer.xserver.Util;
import jexer.xserver.XServer;

/**
 * This class handles requests relating to extensions.
 *
 * @author Matthew Kwan
 */
public class Extensions {
    public static final byte XGE = -128;
    public static final byte XTEST = -124;
    public static final byte Sync = -127;
    public static final byte BigRequests = -126;
    public static final byte Shape = -125;

    static public void Initialize(){
        XSync.Initialize();
    }

    /**
     * Process a request relating to an X extension.
     *
     * @param xServer        The X server.
     * @param client         The remote client.
     * @param opcode         The request's opcode.
     * @param arg            Optional first argument.
     * @param bytesRemaining Bytes yet to be read in the request.
     * @throws IOException
     */
    public static void processRequest(XServer xServer, Client client,
        byte opcode, byte arg, int bytesRemaining) throws IOException {

        InputOutput io = client.getInputOutput();

        switch (opcode) {
            case XGE:
                if (bytesRemaining != 4) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.write(client, ErrorCode.Length, opcode, 0);
                } else {    // Assume arg == 0 (GEQueryVersion).
                    short xgeMajor = (short) io.readShort();
                    short xgeMinor = (short) io.readShort();

                    synchronized (io) {
                        Util.writeReplyHeader(client, arg);
                        io.writeInt(0);    // Reply length.
                        io.writeShort(xgeMajor);
                        io.writeShort(xgeMinor);
                        io.writePadBytes(20);
                    }
                    io.flush();
                }
                break;
            case BigRequests:
                if (bytesRemaining != 0) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.write(client, ErrorCode.Length, opcode, 0);
                } else {    // Assume arg == 0 (BigReqEnable).
                    synchronized (io) {
                        Util.writeReplyHeader(client, arg);
                        io.writeInt(0);
                        io.writeInt(Integer.MAX_VALUE);
                        io.writePadBytes(20);
                    }
                    io.flush();
                }
                break;
            case Shape:
                XShape.processRequest(xServer, client, opcode, arg, bytesRemaining);
                break;
            case XTEST:
                XTest.processRequest(xServer, client, opcode, arg, bytesRemaining);
                break;
            case Sync:
            //    XSync.processRequest(xServer, client, opcode, arg, bytesRemaining);
            //    break;
            default:
                io.readSkip(bytesRemaining);    // Not implemented.
                ErrorCode.write(client, ErrorCode.Implementation, opcode, 0);
                break;
        }
    }
}
