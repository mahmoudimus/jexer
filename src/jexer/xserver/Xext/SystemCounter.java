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

public class SystemCounter {
	public final byte		resHi;
	public final byte		resLo;
	public int COUNTER;
	public short nameLength;
	public byte[] name;
	public int nameLengthInt;
	public int padLen;

	/**
	 * Constructor.
	 *
	 * @param pmajorOpcode	Major opcode of the extension, or zero.
	 * @param pfirstEvent	Base event type code, or zero.
	 * @param pfirstError	Base error code, or zero.
	 */
	public SystemCounter (int ID, int presHi, int presLo, String pname) {
		COUNTER=ID;
		resHi = (byte) presHi;
		resLo = (byte) presLo;
		name=pname.getBytes();
		nameLengthInt=name.length;
		nameLength=(short)nameLengthInt;
		padLen=2;
	}
}
