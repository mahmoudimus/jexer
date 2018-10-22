/*
 * Jexer - Java Text User Interface
 *
 * The MIT License (MIT)
 *
 * Copyright (C) 2019 Kevin Lamonte
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
 * @author Kevin Lamonte [kevin.lamonte@gmail.com]
 * @version 1
 */
package jexer;

import java.util.List;

import jexer.bits.CellAttributes;
import jexer.bits.GraphicsChars;
import jexer.event.TKeypressEvent;
import jexer.event.TMouseEvent;
import static jexer.TKeypress.*;

/**
 * TComboBox implements a combobox containing a drop-down list and edit
 * field.  Alt-Down can be used to show the drop-down.
 */
public class TComboBox extends TWidget {

    // ------------------------------------------------------------------------
    // Variables --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * The list of items in the drop-down.
     */
    private TList list;

    /**
     * The edit field containing the value to return.
     */
    private TField field;

    /**
     * The action to perform when the user selects an item (clicks or enter).
     */
    private TAction updateAction = null;

    /**
     * If true, the field cannot be updated to a value not on the list.
     */
    private boolean limitToListValue = true;

    // ------------------------------------------------------------------------
    // Constructors -----------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Public constructor.
     *
     * @param parent parent widget
     * @param x column relative to parent
     * @param y row relative to parent
     * @param width visible combobox width, including the down-arrow
     * @param values the possible values for the box, shown in the drop-down
     * @param valuesIndex the initial index in values, or -1 for no default
     * value
     * @param valuesHeight the height of the values drop-down when it is
     * visible
     * @param updateAction action to call when a new value is selected from
     * the list or enter is pressed in the edit field
     */
    public TComboBox(final TWidget parent, final int x, final int y,
        final int width, final List<String> values, final int valuesIndex,
        final int valuesHeight, final TAction updateAction) {

        // Set parent and window
        super(parent, x, y, width, 1);

        assert (values != null);

        this.updateAction = updateAction;

        field = new TField(this, 0, 0, width - 3, false, "",
            updateAction, null);
        if (valuesIndex >= 0) {
            field.setText(values.get(valuesIndex));
        }

        list = new TList(this, values, 0, 1, width, valuesHeight,
            new TAction() {
                public void DO() {
                    field.setText(list.getSelected());
                    list.setEnabled(false);
                    list.setVisible(false);
                    TComboBox.this.setHeight(1);
                    if (TComboBox.this.limitToListValue == false) {
                        TComboBox.this.activate(field);
                    }
                    if (updateAction != null) {
                        updateAction.DO();
                    }
                }
            }
        );
        if (valuesIndex >= 0) {
            list.setSelectedIndex(valuesIndex);
        }

        list.setEnabled(false);
        list.setVisible(false);
        setHeight(1);
        if (limitToListValue) {
            field.setEnabled(false);
        } else {
            activate(field);
        }
    }

    // ------------------------------------------------------------------------
    // Event handlers ---------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Returns true if the mouse is currently on the down arrow.
     *
     * @param mouse mouse event
     * @return true if the mouse is currently on the down arrow
     */
    private boolean mouseOnArrow(final TMouseEvent mouse) {
        if ((mouse.getY() == 0)
            && (mouse.getX() >= getWidth() - 3)
            && (mouse.getX() <= getWidth() - 1)
        ) {
            return true;
        }
        return false;
    }

    /**
     * Handle mouse down clicks.
     *
     * @param mouse mouse button down event
     */
    @Override
    public void onMouseDown(final TMouseEvent mouse) {
        if ((mouseOnArrow(mouse)) && (mouse.isMouse1())) {
            // Make the list visible or not.
            if (list.isActive()) {
                list.setEnabled(false);
                list.setVisible(false);
                setHeight(1);
                if (limitToListValue == false) {
                    activate(field);
                }
            } else {
                list.setEnabled(true);
                list.setVisible(true);
                setHeight(list.getHeight() + 1);
                activate(list);
            }
        }

        // Pass to parent for the things we don't care about.
        super.onMouseDown(mouse);
    }

    /**
     * Handle keystrokes.
     *
     * @param keypress keystroke event
     */
    @Override
    public void onKeypress(final TKeypressEvent keypress) {
        if (keypress.equals(kbEsc)) {
            if (list.isActive()) {
                list.setEnabled(false);
                list.setVisible(false);
                setHeight(1);
                if (limitToListValue == false) {
                    activate(field);
                }
                return;
            }
        }

        if (keypress.equals(kbAltDown)) {
            list.setEnabled(true);
            list.setVisible(true);
            setHeight(list.getHeight() + 1);
            activate(list);
            return;
        }

        if (keypress.equals(kbTab)
            || (keypress.equals(kbShiftTab))
            || (keypress.equals(kbBackTab))
        ) {
            if (list.isActive()) {
                list.setEnabled(false);
                list.setVisible(false);
                setHeight(1);
                if (limitToListValue == false) {
                    activate(field);
                }
                return;
            }
        }

        // Pass to parent for the things we don't care about.
        super.onKeypress(keypress);
    }

    // ------------------------------------------------------------------------
    // TWidget ----------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Draw the combobox down arrow.
     */
    @Override
    public void draw() {
        CellAttributes comboBoxColor;

        if (!isAbsoluteActive()) {
            // We lost focus, turn off the list.
            if (list.isActive()) {
                list.setEnabled(false);
                list.setVisible(false);
                setHeight(1);
                if (limitToListValue == false) {
                    activate(field);
                }
            }
        }

        if (isAbsoluteActive() && (limitToListValue == false)) {
            comboBoxColor = getTheme().getColor("tcombobox.active");
        } else {
            comboBoxColor = getTheme().getColor("tcombobox.inactive");
        }

        putCharXY(getWidth() - 3, 0, GraphicsChars.DOWNARROWLEFT,
            comboBoxColor);
        putCharXY(getWidth() - 2, 0, GraphicsChars.DOWNARROW,
            comboBoxColor);
        putCharXY(getWidth() - 1, 0, GraphicsChars.DOWNARROWRIGHT,
            comboBoxColor);
    }

    // ------------------------------------------------------------------------
    // TComboBox --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Get combobox text value.
     *
     * @return text in the edit field
     */
    public String getText() {
        return field.getText();
    }

    /**
     * Set combobox text value.
     *
     * @param text the new text in the edit field
     */
    public void setText(final String text) {
        field.setText(text);
        for (int i = 0; i < list.getMaxSelectedIndex(); i++) {
            if (list.getSelected().equals(text)) {
                list.setSelectedIndex(i);
                return;
            }
        }
        list.setSelectedIndex(-1);
    }

    /**
     * Set combobox text to one of the list values.
     *
     * @param index the index in the list
     */
    public void setIndex(final int index) {
        list.setSelectedIndex(index);
        field.setText(list.getSelected());
    }

    /**
     * Get a copy of the list of strings to display.
     *
     * @return the list of strings
     */
    public final List<String> getList() {
        return list.getList();
    }

    /**
     * Set the new list of strings to display.
     *
     * @param list new list of strings
     */
    public final void setList(final List<String> list) {
        this.list.setList(list);
        field.setText("");
    }

}