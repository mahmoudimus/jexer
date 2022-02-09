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
 * This class stores details of a pixmap format.
 *
 * @author Matthew Kwan
 */
public class Format {
    private final byte _depth;
    private final byte _bitsPerPixel;
    private final byte _scanlinePad;

    /**
     * Constructor.
     *
     * @param depth        The depth in bits.
     * @param bitsPerPixel Number of bits per pixel.
     * @param scanlinePad  Number of bits to pad each scan line.
     */
    public Format(byte depth, byte bitsPerPixel, byte scanlinePad) {
        _depth = depth;
        _bitsPerPixel = bitsPerPixel;
        _scanlinePad = scanlinePad;
    }

    /**
     * Write details of the format.
     *
     * @param io The input/output stream.
     * @throws IOException
     */
    public void write(InputOutput io) throws IOException {
        io.writeByte(_depth);        // Depth.
        io.writeByte(_bitsPerPixel);    // Bits per pixel.
        io.writeByte(_scanlinePad);    // Scanline pad.
        io.writePadBytes(5);    // Unused.
    }
}
