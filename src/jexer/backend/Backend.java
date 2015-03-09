/**
 * Jexer - Java Text User Interface
 *
 * Version: $Id$
 *
 * Author: Kevin Lamonte, <a href="mailto:kevin.lamonte@gmail.com">kevin.lamonte@gmail.com</a>
 *
 * License: LGPLv3 or later
 *
 * Copyright: This module is licensed under the GNU Lesser General
 * Public License Version 3.  Please see the file "COPYING" in this
 * directory for more information about the GNU Lesser General Public
 * License Version 3.
 *
 *     Copyright (C) 2015  Kevin Lamonte
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see
 * http://www.gnu.org/licenses/, or write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package jexer.backend;

import jexer.event.TInputEvent;
import jexer.io.Screen;
import jexer.session.SessionInfo;

/**
 * This abstract class provides a screen, keyboard, and mouse to
 * TApplication.  It also exposes session information as gleaned from lower
 * levels of the communication stack.
 */
public abstract class Backend {

    /**
     * The session information
     */
    public SessionInfo session;

    /**
     * The screen to draw on
     */
    public Screen screen;

    /**
     * Subclasses must provide an implementation that syncs the logical
     * screen to the physical device.
     */
    abstract public void flushScreen();

    /**
     * Subclasses must provide an implementation to get keyboard, mouse, and
     * screen resize events.
     *
     * @param timeout maximum amount of time to wait for an event
     * @return events received, or an empty list if the timeout was reached
     */
    abstract public TInputEvent [] getEvents(int timeout);

    /**
     * Subclasses must provide an implementation that closes sockets,
     * restores console, etc.
     */
    abstract public void shutdown();

}
