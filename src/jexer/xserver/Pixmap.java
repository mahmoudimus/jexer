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
 * This class implements an X pixmap.
 *
 * @author Matthew Kwan
 */
public class Pixmap extends Resource {
    private final Drawable _drawable;
    public final ScreenView _screen;

    /**
     * Constructor.
     *
     * @param id      The pixmap's ID.
     * @param xServer The X server.
     * @param client  The client issuing the request.
     * @param screen  The screen.
     * @param width   The pixmap width.
     * @param height  The pixmap height.
     * @param depth   The pixmap depth.
     */
    public Pixmap(int id, XServer xServer, Client client, ScreenView screen,
        int width, int height, int depth) {

        super(PIXMAP, id, xServer, client);

        _drawable = new Drawable(width, height, depth, null, 0xff000000);
        _screen = screen;
    }

    /**
     * Return the pixmap's screen.
     *
     * @return The pixmap's screen.
     */
    public ScreenView getScreen() {
        return _screen;
    }

    /**
     * Return the pixmap's drawable.
     *
     * @return The pixmap's drawable.
     */
    public Drawable getDrawable() {
        return _drawable;
    }

    /**
     * Return the pixmap's depth.
     *
     * @return The pixmap's depth.
     */
    public int getDepth() {
        return _drawable.getDepth();
    }

    /**
     * Process an X request relating to this pixmap.
     *
     * @param client         The remote client.
     * @param opcode         The request's opcode.
     * @param arg            Optional first argument.
     * @param bytesRemaining Bytes yet to be read in the request.
     * @throws IOException
     */
    @Override
    public void processRequest(Client client, byte opcode, byte arg, int bytesRemaining) throws IOException {
        InputOutput io = client.getInputOutput();

        // AZL

        /*
        switch (opcode) {
            case RequestCode.FreePixmap:
                if (bytesRemaining != 0) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.write(client, ErrorCode.Length, opcode, 0);
                } else {
                    _xServer.freeResource(_id);
                    if (_client != null) _client.freeResource(this);
                    _drawable.getBitmap().recycle();
                }
                break;
            case RequestCode.GetGeometry:
                if (bytesRemaining != 0) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.write(client, ErrorCode.Length, opcode, 0);
                } else {
                    writeGeometry(client);
                }
                break;
            case RequestCode.CopyArea:
            case RequestCode.CopyPlane:
            case RequestCode.PolyPoint:
            case RequestCode.PolyLine:
            case RequestCode.PolySegment:
            case RequestCode.PolyRectangle:
            case RequestCode.PolyArc:
            case RequestCode.FillPoly:
            case RequestCode.PolyFillRectangle:
            case RequestCode.PolyFillArc:
            case RequestCode.PutImage:
            case RequestCode.GetImage:
            case RequestCode.PolyText8:
            case RequestCode.PolyText16:
            case RequestCode.ImageText8:
            case RequestCode.ImageText16:
            case RequestCode.QueryBestSize:
                _drawable.processRequest(_xServer, client, _id, opcode, arg, bytesRemaining);
                return;
            default:
                io.readSkip(bytesRemaining);
                bytesRemaining = 0;
                ErrorCode.write(client, ErrorCode.Implementation, opcode, 0);
                break;
        }
         */
    }

    /**
     * Write details of the pixmap's geometry in response to a GetGeometry
     * request.
     *
     * @param client The remote client.
     * @throws IOException
     */
    private void writeGeometry(Client client) throws IOException {
        InputOutput io = client.getInputOutput();

        synchronized (io) {
            Util.writeReplyHeader(client, (byte) 32);
            io.writeInt(0);    // Reply length.
            io.writeInt(_screen.getRootWindow().getId());    // Root window.
            io.writeShort((short) 0);    // X.
            io.writeShort((short) 0);    // Y.
            io.writeShort((short) _drawable.getWidth());    // Width.
            io.writeShort((short) _drawable.getHeight());    // Height.
            io.writeShort((short) 0);    // Border width.
            io.writePadBytes(10);    // Unused.
        }
        io.flush();
    }

    /**
     * Process a CreatePixmap request.
     *
     * @param xServer  The X server.
     * @param client   The client issuing the request.
     * @param id       The ID of the pixmap to create.
     * @param depth    The depth of the pixmap.
     * @param drawable The drawable whose depth it must match.
     * @throws IOException
     */
    public static void processCreatePixmapRequest(XServer xServer,
        Client client, int id, int width, int height, int depth,
        Resource drawable) throws IOException {

        ScreenView screen;
        Pixmap p;

        if (drawable.getType() == Resource.PIXMAP) screen = ((Pixmap) drawable).getScreen();
        else screen = ((Window) drawable).getScreen();

        try {
            p = new Pixmap(id, xServer, client, screen, width, height, depth);
        } catch (OutOfMemoryError e) {
            ErrorCode.write(client, ErrorCode.Alloc, RequestCode.CreatePixmap, 0);
            return;
        }

        xServer.addResource(p);
        client.addResource(p);
    }
}
