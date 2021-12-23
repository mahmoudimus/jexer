/*
 * Jexer - Java Text User Interface
 *
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 Autumn Lamonte
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
 *
 * @author Autumn Lamonte [AutumnWalksTheLake@gmail.com] ⚧ Trans Liberation Now
 * @version 1
 */
package jexer.tackboard;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jexer.backend.GlyphMaker;
import jexer.backend.Screen;
import jexer.bits.Cell;

/**
 * Tackboard maintains a collection of TackboardItems to draw on a Screen.
 *
 * <p>Each item has a set of X, Y, Z pixel (not text cell) coordinates.  The
 * coordinate system is right-handed: (0, 0, 0) is the top-left pixel on the
 * screen, and positive Z points away from the user.</p>
 *
 * <p>When draw() is called, all the items will be rendered in descending Z,
 * ascending Y, ascending X order (painter's algorithm) onto the cell grid,
 * and using transparent pixels.  If the Screen's backend does not support
 * imagesOverText, then the text of the Cell under transparent images will be
 * rendered via GlyphMaker, which might not look ideal if the internal font
 * is quite different from the terminal's.</p>
 *
 * <p>Tackboards were directly inspired by the Visuals (ncvisuals) of <a
 * href="https://github.com/dankamongmen/notcurses">notcurses</a>. Jexer's
 * performance is unlikely to come close to notcurses, so users requiring
 * low-latency pixel-based rendering are recommended to check out
 * notcurses.</p>
 */
public class Tackboard {

    // ------------------------------------------------------------------------
    // Constants --------------------------------------------------------------
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // Variables --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * The items on this board.
     */
    private ArrayList<TackboardItem> items = new ArrayList<TackboardItem>();

    // ------------------------------------------------------------------------
    // Constructors -----------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Public constructor.
     */
    public Tackboard() {
        // NOP
    }

    // ------------------------------------------------------------------------
    // Tackboard --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Add an item to the board.
     *
     * @param item the item to add
     */
    public void addItem(final TackboardItem item) {
        items.add(item);
    }

    /**
     * Get the list of items.
     *
     * @return the list of items
     */
    public List<TackboardItem> getItems() {
        return items;
    }

    /**
     * Draw everything to the screen.
     *
     * @param screen the screen to render to
     */
    public void draw(final Screen screen) {
        Collections.sort(items);
        int cellWidth = screen.getTextWidth();
        int cellHeight = screen.getTextHeight();

        for (TackboardItem item: items) {
            BufferedImage image = item.getImage(cellWidth, cellHeight);
            if (image == null) {
                continue;
            }

            int x = item.getX();
            int y = item.getY();
            int textX = x / cellWidth;
            int textY = y / cellHeight;
            int width = image.getWidth();
            int height = image.getHeight();

            assert (width % cellWidth == 0);
            assert (height % cellHeight == 0);

            int columns = width / cellWidth;
            int rows = height / cellHeight;

            int screenWidth = screen.getWidth() * screen.getTextWidth();
            int screenHeight = screen.getHeight() * screen.getTextHeight();
            if ((textX + columns < 0)
                || (textY + rows < 0)
                || (textX >= screen.getWidth())
                || (textY >= screen.getHeight())
            ) {
                // No cells of this item will be visible on the screen.
                continue;
            }

            int dx = x % cellWidth;
            int dy = y % cellHeight;

            // TODO: handle images that have negative X or Y coordinates.
            int left = 0;
            int top = 0;

            for (int sy = 0; sy < rows; sy++) {
                if ((sy + textY + top < 0)
                    || (sy + textY + top >= screen.getHeight())
                ) {
                    // This row of cells is off-screen, skip it.
                    continue;
                }
                for (int sx = 0; sx < columns; sx++) {
                    while (sx + textX + left < 0) {
                        // This cell is off-screen, advance.
                        sx++;
                    }
                    if (sx + textX + left >= screen.getWidth()) {
                        // This cell is off-screen, done with this entire row.
                        break;
                    }

                    // This cell is visible on the screen.
                    assert (sx + textX + left < screen.getWidth());
                    assert (sy + textY + top < screen.getHeight());

                    BufferedImage newImage;
                    newImage = image.getSubimage(sx * cellWidth,
                        sy * cellHeight, cellWidth, cellHeight);

                    // newImage has the image that needs to be overlaid on
                    // (sx + textX + left, sy + textY + top)

                    Cell oldCell = screen.getCharXY(sx + textX + left,
                        sy + textY + top);
                    if (oldCell.isImage()) {
                        // Blit this image over that one.
                        BufferedImage oldImage = oldCell.getImage();
                        java.awt.Graphics gr = oldImage.getGraphics();
                        gr.setColor(java.awt.Color.BLACK);
                        gr.drawImage(newImage, 0, 0, null, null);
                        gr.dispose();
                        oldCell.setImage(oldImage);
                    } else {
                        // Old cell is text only, just add the image.
                        oldCell.setImage(newImage);
                    }
                    screen.putCharXY(sx + textX + left, sy + textY + top,
                        oldCell);

                } // for (int sx = 0; sx < columns; sx++)

            } // for (int sy = 0; sy < rows; sy++)

        } // for (TackboardItem item: items)
    }

}
