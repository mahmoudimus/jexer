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
import jexer.xserver.Cursor;
import jexer.xserver.ErrorCode;
import jexer.xserver.InputOutput;
import jexer.xserver.Resource;
import jexer.xserver.ScreenView;
import jexer.xserver.Util;
import jexer.xserver.Window;
import jexer.xserver.XServer;

/**
 * Handles requests related to the XTEST extension.
 *
 * @author mkwan
 */
public class XTest {
    private static final byte XTestGetVersion = 0;
    private static final byte XTestCompareCursor = 1;
    private static final byte XTestFakeInput = 2;
    private static final byte XTestGrabControl = 3;

    private static final byte KeyPress = 2;
    private static final byte KeyRelease = 3;
    private static final byte ButtonPress = 4;
    private static final byte ButtonRelease = 5;
    private static final byte MotionNotify = 6;

    /**
     * Process a request relating to the X XTEST extension.
     *
     * @param xServer        The X server.
     * @param client         The remote client.
     * @param opcode         The request's opcode.
     * @param arg            Optional first argument.
     * @param bytesRemaining Bytes yet to be read in the request.
     * @throws IOException
     */
    public static void processRequest(XServer xServer, Client client,
        byte opcode, byte arg, int bytesRemaining) throws java.io.IOException {

        // AZL
        /*
        InputOutput io = client.getInputOutput();

        switch (arg) {
            case XTestGetVersion:
                if (bytesRemaining != 4) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.writeWithMinorOpcode(client, ErrorCode.Length, arg, opcode, 0);
                } else {
                    io.readSkip(4);    // Skip client major/minor version.

                    final byte serverMajorVersion = 2;
                    final int serverMinorVersion = 1;

                    synchronized (io) {
                        Util.writeReplyHeader(client, serverMajorVersion);
                        io.writeInt(0);    // Reply length.
                        io.writeShort((short) serverMinorVersion);
                        io.writePadBytes(22);
                    }
                    io.flush();
                }
                break;
            case XTestCompareCursor:
                if (bytesRemaining != 8) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.writeWithMinorOpcode(client, ErrorCode.Length, arg, opcode, 0);
                } else {
                    int wid = io.readInt();
                    int cid = io.readInt();
                    Resource r = xServer.getResource(wid);

                    if (r == null || r.getType() != Resource.WINDOW) {
                        ErrorCode.writeWithMinorOpcode(client, ErrorCode.Window, arg, opcode, wid);
                        return;
                    }

                    Window w = (Window) r;
                    Cursor c = w.getCursor();
                    boolean same;

                    if (cid == 0)    // No cursor.
                        same = (c == null);
                    else if (cid == 1)    // CurrentCursor.
                        same = (c == xServer.getScreen().getCurrentCursor());
                    else same = (cid == c.getId());

                    synchronized (io) {
                        Util.writeReplyHeader(client, (byte) (same ? 1 : 0));
                        io.writeInt(0);    // Reply length.
                        io.writePadBytes(24);
                    }
                    io.flush();
                }
                break;
            case XTestFakeInput:
                if (bytesRemaining != 32) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.writeWithMinorOpcode(client, ErrorCode.Length, arg, opcode, 0);
                } else {
                    byte type = (byte) io.readByte();
                    int detail = io.readByte();

                    io.readSkip(2);

                    int delay = io.readInt();
                    int wid = io.readInt();

                    io.readSkip(8);

                    int x = io.readShort();
                    int y = io.readShort();

                    io.readSkip(8);

                    Window root;

                    if (wid == 0) {
                        root = xServer.getScreen().getRootWindow();
                    } else {
                        Resource r = (Window) xServer.getResource(wid);

                        if (r == null || r.getType() != Resource.WINDOW) {
                            ErrorCode.writeWithMinorOpcode(client, ErrorCode.Window, arg, opcode, wid);
                            return;
                        } else {
                            root = (Window) r;
                        }
                    }
                    testFakeInput(xServer, client, opcode, arg, type, detail, delay, root, x, y);
                }
                break;
            case XTestGrabControl:
                if (bytesRemaining != 4) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.writeWithMinorOpcode(client, ErrorCode.Length, arg, opcode, 0);
                } else {
                    boolean impervious = (io.readByte() != 0);

                    io.readSkip(3);
                    client.setImperviousToServerGrabs(impervious);
                }
                break;
            default:
                io.readSkip(bytesRemaining);
                ErrorCode.write(client, ErrorCode.Implementation, opcode, 0);
                break;
        }
         */
    }

    /**
     * Inject a fake event.
     *
     * @param xServer     The X server.
     * @param client      The remote client.
     * @param opcode      The request opcode, used for error reporting.
     * @param minorOpcode The minor opcode, used for error reporting.
     * @param type        The event type.
     * @param detail      Meaning depends on event type.
     * @param delay       Millisecond delay.
     * @param root        The root window in which motion takes place.
     * @param x           The X coordinate of a motion event.
     * @param y           The Y coordinate of a motion event.
     * @throws java.io.IOException
     */
    private static void testFakeInput(XServer xServer, Client client,
        byte opcode, byte minorOpcode, byte type, int detail, int delay,
        Window root, int x, int y) throws java.io.IOException {

        // AZL
        /*
        if (delay != 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }

        ScreenView sv = xServer.getScreen();

        switch (type) {
            case KeyPress:
                sv.notifyKeyPressedReleased(detail, true);
                break;
            case KeyRelease:
                sv.notifyKeyPressedReleased(detail, false);
                break;
            case ButtonPress:
                sv.updatePointerButtons(detail, true);
                break;
            case ButtonRelease:
                sv.updatePointerButtons(detail, false);
                break;
            case MotionNotify:
                if (root != null) sv = root.getScreen();

                if (detail != 0) {    // Relative position.
                    x += sv.getPointerX();
                    y += sv.getPointerY();
                }

                if (x < 0) x = 0;
                else if (x >= sv.getWidth()) x = sv.getWidth() - 1;

                if (y < 0) y = 0;
                else if (y >= sv.getHeight()) y = sv.getHeight() - 1;

                sv.updatePointerPosition(x, y, 0);
                break;
            default:
                ErrorCode.writeWithMinorOpcode(client, ErrorCode.Value, minorOpcode, opcode, type);
                break;
        }
         */
    }
}
