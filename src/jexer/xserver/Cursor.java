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

import java.awt.image.BufferedImage;

import java.io.IOException;

/**
 * This class implements an X Windows cursor.
 *
 * @author Matthew Kwan
 */
public class Cursor extends Resource {
    private final int _hotspotX;
    private final int _hotspotY;
    private BufferedImage _bitmap;
    private int _foregroundColor;
    private int _backgroundColor;

    // AZL TODO: make the hotpoints a file read from the directory with the
    // images.
    private static final String _glyphs[] = {
        "xc_x_cursor 7 7",
        "xc_arrow 14 1",
        "xc_based_arrow_down 4 10",
        "xc_based_arrow_up 4 10",
        "xc_boat 14 4",
        "xc_bogosity 7 7",
        "xc_bottom_left_corner 1 14",
        "xc_bottom_right_corner 14 14",
        "xc_bottom_side 7 14",
        "xc_bottom_tee 8 10",
        "xc_box_spiral 8 8",
        "xc_center_ptr 5 1",
        "xc_circle 8 8",
        "xc_clock 6 3",
        "xc_coffee_mug 7 9",
        "xc_cross 7 7",
        "xc_cross_reverse 7 7",
        "xc_crosshair 7 7",
        "xc_diamond_cross 7 7",
        "xc_dot 6 6",
        "xc_dotbox 7 6",
        "xc_double_arrow 6 8",
        "xc_draft_large 14 0",
        "xc_draft_small 14 0",
        "xc_draped_box 7 6",
        "xc_exchange 7 7",
        "xc_fleur 8 8",
        "xc_gobbler 14 3",
        "xc_gumby 2 0",
        "xc_hand1 12 0",
        "xc_hand2 0 1",
        "xc_heart 6 8",
        "xc_icon 8 8",
        "xc_iron_cross 8 7",
        "xc_left_ptr 1 1",
        "xc_left_side 1 7",
        "xc_left_tee 1 8",
        "xc_leftbutton 8 8",
        "xc_ll_angle 1 10",
        "xc_lr_angle 10 10",
        "xc_man 14 5",
        "xc_middlebutton 8 8",
        "xc_mouse 4 1",
        "xc_pencil 11 15",
        "xc_pirate 7 12",
        "xc_plus 5 6",
        "xc_question_arrow 5 8",
        "xc_right_ptr 8 1",
        "xc_right_side 14 7",
        "xc_right_tee 10 8",
        "xc_rightbutton 8 8",
        "xc_rtl_logo 7 7",
        "xc_sailboat 8 0",
        "xc_sb_down_arrow 4 15",
        "xc_sb_h_double_arrow 7 4",
        "xc_sb_left_arrow 0 4",
        "xc_sb_right_arrow 15 4",
        "xc_sb_up_arrow 4 0",
        "xc_sb_v_double_arrow 4 7",
        "xc_shuttle 11 0",
        "xc_sizing 8 8",
        "xc_spider 6 7",
        "xc_spraycan 10 2",
        "xc_star 7 7",
        "xc_target 7 7",
        "xc_tcross 7 7",
        "xc_top_left_arrow 1 1",
        "xc_top_left_corner 1 1",
        "xc_top_right_corner 14 1",
        "xc_top_side 7 1",
        "xc_top_tee 8 1",
        "xc_trek 4 0",
        "xc_ul_angle 1 1",
        "xc_umbrella 8 2",
        "xc_ur_angle 10 1",
        "xc_watch 15 9",
        "xc_xterm 4 8"
    };

    /**
     * Constructor for a pixmap cursor.
     *
     * @param id              The server cursor ID.
     * @param xServer         The X server.
     * @param client          The client issuing the request.
     * @param p               Cursor pixmap.
     * @param mp              Mask pixmap. May be null.
     * @param x               Hotspot X coordinate.
     * @param y               Hotspot Y coordinate.
     * @param foregroundColor Foreground color of the cursor.
     * @param backgroundColor Foreground color of the cursor.
     */
    public Cursor(int id, XServer xServer, Client client, Pixmap p, Pixmap mp,
        int x, int y, int foregroundColor, int backgroundColor) {

        super(CURSOR, id, xServer, client);

        _hotspotX = x;
        _hotspotY = y;
        _foregroundColor = foregroundColor;
        _backgroundColor = backgroundColor;

        BufferedImage bm = p.getDrawable().getBitmap();
        int width = bm.getWidth();
        int height = bm.getHeight();
        int[] pixels = new int[width * height];

        bm.getRGB(0, 0, width, height, pixels, 0, width);
        if (mp == null) {
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] == 0xffffffff) pixels[i] = foregroundColor;
                else pixels[i] = backgroundColor;
            }
        } else {
            BufferedImage mbm = mp.getDrawable().getBitmap();
            int[] mask = new int[width * height];

            mbm.getRGB(0, 0, width, height, mask, 0, width);
            for (int i = 0; i < pixels.length; i++) {
                if (mask[i] != 0xffffffff) pixels[i] = 0;
                else if (pixels[i] == 0xffffffff) pixels[i] = foregroundColor;
                else pixels[i] = backgroundColor;
            }
        }

        _bitmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        _bitmap.setRGB(0, 0, width, height, pixels, 0, width);
    }

    /**
     * Constructor for a glyph cursor.
     * This functions just assumes the caller wants one of the 77 predefined
     * cursors from the "cursor" font, so the sourceFont, maskFont, and
     * maskChar are all ignored.
     *
     * @param id              The server cursor ID.
     * @param xServer         The X server.
     * @param client          The client issuing the request.
     * @param sourceFont      Font to use for the cursor character.
     * @param maskFont        Font for the mask character. May be null.
     * @param sourceChar      Character to use as the cursor.
     * @param maskChar        Character to use as the mask.
     * @param foregroundColor Foreground color of the cursor.
     * @param backgroundColor Foreground color of the cursor.
     */
    public Cursor(int id, XServer xServer, Client client, Font sourceFont,
        Font maskFont, int sourceChar, int maskChar, int foregroundColor,
        int backgroundColor) {

        super(CURSOR, id, xServer, client);

        sourceChar /= 2;
        if (sourceChar < 0 || sourceChar >= _glyphs.length) sourceChar = 0;

        if (maskChar == 32) {
            _bitmap = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        } else {
            String glyphTokens = _glyphs[sourceChar];
            String [] tokens = glyphTokens.split(" ");
            // AZL TODO:
            // tokens[0] is a filename to a PNG
            // tokens[1] and tokens[2] are the hotspot coordinates.
            //
            // _bitmap = BitmapFactory.decodeResource(xServer.getContext().getResources(), _glyphs[sourceChar][0]);
        }

        _foregroundColor = 0xff000000;
        _backgroundColor = 0xffffffff;
        setColor(foregroundColor, backgroundColor);

        // AZL - use hotspot from _glyphs
        // _hotspotX = _glyphs[sourceChar][1];
        // _hotspotY = _glyphs[sourceChar][2];
    }

    /**
     * Return the cursor's bitmap.
     *
     * @return The cursor's bitmap.
     */
    public BufferedImage getBitmap() {
        return _bitmap;
    }

    /**
     * Return the X coordinate of the cursor's hotspot.
     *
     * @return The X coordinate of the cursor's hotspot.
     */
    public int getHotspotX() {
        return _hotspotX;
    }

    /**
     * Return the Y coordinate of the cursor's hotspot.
     *
     * @return The Y coordinate of the cursor's hotspot.
     */
    public int getHotspotY() {
        return _hotspotY;
    }

    /**
     * Set the foreground and background colors of the cursor.
     *
     * @param fg Foreground color.
     * @param bg Background color.
     */
    private void setColor(int fg, int bg) {
        if (fg == _foregroundColor && bg == _backgroundColor) return;

        int width = _bitmap.getWidth();
        int height = _bitmap.getHeight();
        int[] pixels = new int[width * height];

        _bitmap.getRGB(0, 0, width, height, pixels, 0, width);
        for (int i = 0; i < pixels.length; i++) {
            int pix = pixels[i];

            if (pix == _foregroundColor) pixels[i] = fg;
            else if (pix == _backgroundColor) pixels[i] = bg;
        }

        _bitmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        _bitmap.setRGB(0, 0, width, height, pixels, 0, width);
        _foregroundColor = fg;
        _backgroundColor = bg;
    }

    /**
     * Process an X request relating to this cursor.
     *
     * @param client         The remote client.
     * @param opcode         The request's opcode.
     * @param arg            Optional first argument.
     * @param bytesRemaining Bytes yet to be read in the request.
     * @throws IOException
     */
    @Override
    public void processRequest(Client client, byte opcode, byte arg,
        int bytesRemaining) throws IOException {

        InputOutput io = client.getInputOutput();

        switch (opcode) {
            case RequestCode.FreeCursor:
                if (bytesRemaining != 0) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.write(client, ErrorCode.Length, opcode, 0);
                } else {
                    _xServer.freeResource(_id);
                    if (_client != null) _client.freeResource(this);
                }
                break;
            case RequestCode.RecolorCursor:
                if (bytesRemaining != 12) {
                    io.readSkip(bytesRemaining);
                    ErrorCode.write(client, ErrorCode.Length, opcode, 0);
                } else {
                    int fgRed = io.readShort();
                    int fgGreen = io.readShort();
                    int fgBlue = io.readShort();
                    int bgRed = io.readShort();
                    int bgGreen = io.readShort();
                    int bgBlue = io.readShort();

                    setColor(Colormap.fromParts16(fgRed, fgGreen, fgBlue), Colormap.fromParts16(bgRed, bgGreen, bgBlue));
                }
                break;
            default:
                io.readSkip(bytesRemaining);
                ErrorCode.write(client, ErrorCode.Implementation, opcode, _id);
                break;
        }
    }

    /**
     * Process a create request.
     *
     * @param xServer        The X server.
     * @param client         The client issuing the request.
     * @param opcode         The request opcode.
     * @param id             The ID of the cursor to create.
     * @param bytesRemaining Bytes yet to be read in the request.
     * @throws IOException
     */
    public static void processCreateRequest(XServer xServer, Client client,
        byte opcode, int id, int bytesRemaining) throws IOException {

        InputOutput io = client.getInputOutput();

        if (opcode == RequestCode.CreateCursor) {
            int sid = io.readInt();    // Source pixmap ID.
            int mid = io.readInt();    // Mask pixmap ID.
            int fgRed = io.readShort();
            int fgGreen = io.readShort();
            int fgBlue = io.readShort();
            int bgRed = io.readShort();
            int bgGreen = io.readShort();
            int bgBlue = io.readShort();
            short x = (short) io.readShort();
            short y = (short) io.readShort();
            Resource r = xServer.getResource(sid);
            Resource mr = null;

            if (r == null || r.getType() != PIXMAP) {
                ErrorCode.write(client, ErrorCode.Pixmap, opcode, sid);
                return;
            } else if (mid != 0) {
                mr = xServer.getResource(mid);
                if (mr == null || mr.getType() != PIXMAP) {
                    ErrorCode.write(client, ErrorCode.Pixmap, opcode, mid);
                    return;
                }
            }

            Pixmap p = (Pixmap) r;
            Pixmap mp = (Pixmap) mr;

            if (p.getDepth() != 1) {
                ErrorCode.write(client, ErrorCode.Match, opcode, sid);
                return;
            } else if (mp != null) {
                if (mp.getDepth() != 1) {
                    ErrorCode.write(client, ErrorCode.Match, opcode, mid);
                    return;
                }

                Bitmap bm1 = p.getDrawable().getBitmap();
                Bitmap bm2 = mp.getDrawable().getBitmap();

                if (bm1.getWidth() != bm2.getWidth() || bm1.getHeight() != bm2.getHeight()) {
                    ErrorCode.write(client, ErrorCode.Match, opcode, mid);
                    return;
                }
            }

            int fg = Colormap.fromParts16(fgRed, fgGreen, fgBlue);
            int bg = Colormap.fromParts16(bgRed, bgGreen, bgBlue);
            Cursor c = new Cursor(id, xServer, client, p, mp, x, y, fg, bg);

            xServer.addResource(c);
            client.addResource(c);
        } else if (opcode == RequestCode.CreateGlyphCursor) {
            int sid = io.readInt();    // Source font ID.
            int mid = io.readInt();    // Mask font ID.
            int sourceChar = io.readShort();    // Source char.
            int maskChar = io.readShort();    // Mask char.
            int fgRed = io.readShort();
            int fgGreen = io.readShort();
            int fgBlue = io.readShort();
            int bgRed = io.readShort();
            int bgGreen = io.readShort();
            int bgBlue = io.readShort();
            Resource r = xServer.getResource(sid);
            Resource mr = null;

            if (r == null || r.getType() != FONT) {
                ErrorCode.write(client, ErrorCode.Font, opcode, sid);
                return;
            } else if (mid != 0) {
                mr = xServer.getResource(mid);
                if (mr == null || mr.getType() != FONT) {
                    ErrorCode.write(client, ErrorCode.Font, opcode, mid);
                    return;
                }
            }

            int fg = Colormap.fromParts16(fgRed, fgGreen, fgBlue);
            int bg = Colormap.fromParts16(bgRed, bgGreen, bgBlue);
            Cursor c = new Cursor(id, xServer, client, (Font) r, (Font) mr, sourceChar, maskChar, fg, bg);

            xServer.addResource(c);
            client.addResource(c);
        }
    }
}
