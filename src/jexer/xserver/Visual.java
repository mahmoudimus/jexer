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
 * An X visual. This is always 32-bit TrueColor.
 *
 * @author Matthew Kwan
 */
public class Visual {
    public final static byte BackingStoreNever = 0;
    public final static byte BackingStoreWhenMapped = 1;
    public final static byte BackingStoreAlways = 2;

    public final static byte StaticGray = 0;
    public final static byte GrayScale = 1;
    public final static byte StaticColor = 2;
    public final static byte PseudoColor = 3;
    public final static byte TrueColor = 4;
    public final static byte DirectColor = 5;

    private final int _id;

    /**
     * Constructor.
     *
     * @param id The visual ID.
     */
    public Visual(int id) {
        _id = id;
    }

    /**
     * Return the visual's ID.
     *
     * @return The visual's ID.
     */
    public int getId() {
        return _id;
    }

    /**
     * Return whether the visual supports a backing store.
     *
     * @return Whether a backing store is supported.
     */
    public byte getBackingStoreInfo() {
        return BackingStoreAlways;
    }

    /**
     * Return whether the visual supports save-under.
     *
     * @return Whether save-under is supported.
     */
    public boolean getSaveUnder() {
        return false;
    }

    /**
     * Return the depth of the visual.
     * Under Android this is always 32.
     *
     * @return The depth of the visual, in bits.
     */
    public byte getDepth() {
        return 32;
    }

    /**
     * Write details of the visual.
     *
     * @param io The input/output stream.
     * @throws IOException
     */
    public void write(InputOutput io) throws IOException {
        io.writeInt(_id);        // Visual ID.
        io.writeByte(TrueColor);    // Class.
        io.writeByte((byte) 8);    // Bits per RGB value.
        io.writeShort((short) (1 << 8));    // Colormap entries.
        io.writeInt(0x00ff0000);    // Red mask.
        io.writeInt(0x0000ff00);    // Green mask.
        io.writeInt(0x000000ff);    // Blue mask.
        io.writePadBytes(4);    // Unused.
    }
}
