/*
 * Jexer - Java Text User Interface
 *
 * The MIT License (MIT)
 *
 * Copyright (C) 2022 Autumn Lamonte
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

import java.util.ResourceBundle;

import jexer.TApplication;
import jexer.TText;
import jexer.TWindow;
import jexer.event.TResizeEvent;

/**
 * Display messages from the X server engine in a nice scrollable window.
 */
public class XserverLoggerWindow extends TWindow {

    /**
     * Translated strings.
     */
    private static final ResourceBundle i18n = ResourceBundle.getBundle(XserverLoggerWindow.class.getName());

    // ------------------------------------------------------------------------
    // Constants --------------------------------------------------------------
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // Variables --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * The log lines showing on screen.
     */
    private TText text;

    // ------------------------------------------------------------------------
    // Constructors -----------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Public constructor.
     *
     * @param application TApplication that manages this window
     */
    public XserverLoggerWindow(final TApplication application) {

        super(application, i18n.getString("windowTitle"), 0, 0,
            application.getScreen().getWidth(),
            application.getDesktopBottom() - application.getDesktopTop(),
            RESIZABLE);

        text = addText(String.format(i18n.getString("firstLine"),
                System.currentTimeMillis()),
            0, 0, getWidth() - 2, getHeight() - 2, "ttext");
        text.setLineSpacing(0);
    }

    // ------------------------------------------------------------------------
    // Event handlers ---------------------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public void onResize(final TResizeEvent event) {
        if (event.getType() == TResizeEvent.Type.WIDGET) {
            TResizeEvent textSize;
            textSize = new TResizeEvent(event.getBackend(),
                TResizeEvent.Type.WIDGET, event.getWidth() - 2,
                event.getHeight() - 2);
            text.onResize(textSize);
        }
    }

    // ------------------------------------------------------------------------
    // XserverLoggerWindow ----------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Emit a string to the log.
     *
     * @param str the string
     */
    public void log(String str) {
        text.addLine(str);
        text.toLeft();
        text.toBottom();
    }

    /**
     * Emit a format string and arguments to the log.
     *
     * @param format the string
     * @param args... the arguments
     */
    public void logf(final String format, final Object... args) {
        String str = String.format(format, args);
        text.addLine(str);
        text.toLeft();
        text.toBottom();
    }

}
