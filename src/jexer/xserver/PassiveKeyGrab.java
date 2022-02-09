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

/**
 * This class records details of a passive key grab.
 *
 * @author Matthew Kwan
 */
public class PassiveKeyGrab {
    private final Client _grabClient;
    private final Window _grabWindow;
    private final byte _key;
    private final int _modifiers;
    private final boolean _ownerEvents;
    private final boolean _pointerSynchronous;
    private final boolean _keyboardSynchronous;

    /**
     * Constructor.
     *
     * @param grabClient          The grabbing client.
     * @param grabWindow          The grab window.
     * @param key                 The key being grabbed, or 0 for any.
     * @param modifiers           The modifier mask, or 0x8000 for any.
     * @param ownerEvents         Owner-events flag.
     * @param pointerSynchronous  Are pointer events synchronous?
     * @param keyboardSynchronous Are keyboard events synchronous?
     */
    public PassiveKeyGrab(Client grabClient, Window grabWindow, byte key,
        int modifiers, boolean ownerEvents, boolean pointerSynchronous,
        boolean keyboardSynchronous) {

        _grabClient = grabClient;
        _grabWindow = grabWindow;
        _key = key;
        _modifiers = modifiers;
        _ownerEvents = ownerEvents;
        _pointerSynchronous = pointerSynchronous;
        _keyboardSynchronous = keyboardSynchronous;
    }

    /**
     * Does the event trigger the passive grab?
     *
     * @param key       The key that was pressed.
     * @param modifiers The current state of the modifiers.
     * @return True if the event matches.
     */
    public boolean matchesEvent(int key, int modifiers) {
        if (_key != 0 && _key != key) return false;

        if (_modifiers != 0x8000 && _modifiers != modifiers) return false;

        return true;
    }

    /**
     * Does this match the parameters of the grab?
     *
     * @param key       The key being grabbed, or 0 for any.
     * @param modifiers The modifier mask, or 0x8000 for any.
     * @return True if it matches the parameters.
     */
    public boolean matchesGrab(int key, int modifiers) {
        if (key != 0 && _key != 0 && key != _key) return false;

        if (modifiers != 0x8000 && _modifiers != 0x8000 && modifiers != _modifiers) return false;

        return true;
    }

    /**
     * Return the key code.
     *
     * @return The key code.
     */
    public byte getKey() {
        return _key;
    }

    /**
     * Return the modifier mask.
     *
     * @return The modifier mask.
     */
    public int getModifiers() {
        return _modifiers;
    }

    /**
     * Return the grab client.
     *
     * @return The grab client.
     */
    public Client getGrabClient() {
        return _grabClient;
    }

    /**
     * Return the grab window.
     *
     * @return The grab window.
     */
    public Window getGrabWindow() {
        return _grabWindow;
    }

    /**
     * Return the owner-events flag.
     *
     * @return The owner-events flag.
     */
    public boolean getOwnerEvents() {
        return _ownerEvents;
    }

    /**
     * Return whether pointer events are synchronous.
     *
     * @return Whether pointer events are synchronous.
     */
    public boolean getPointerSynchronous() {
        return _pointerSynchronous;
    }

    /**
     * Return whether pointer events are synchronous.
     *
     * @return Whether pointer events are synchronous.
     */
    public boolean getKeyboardSynchronous() {
        return _keyboardSynchronous;
    }
}
