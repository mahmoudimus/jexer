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

import jexer.TAction;
import jexer.TApplication;
import jexer.TTimer;

/**
 * CountDownTimer class equivalent to android.os.CountDownTimer.
 */
public abstract class CountDownTimer {

    /**
     * Remaining time in millis.
     */
    private long millisInFuture;

    /**
     * Interval between ticks.
     */
    private long interval;

    /**
     * The application that can start a timer.
     */
    private TApplication application;
    
    /**
     * The timer object with the application.
     */
    private TTimer timer;
    
    /**
     * Public constructor.
     *
     * @param millisInFuture The number of millis in the future from the call
     * to start() until the countdown is done and onFinish() is called.
     * @param countDownInterval The interval along the way to receive
     * onTick(long) callbacks.
     */
    public CountDownTimer(long millisInFuture, long countDownInterval) {
        this.application = application;
        this.millisInFuture = millisInFuture;
        interval = countDownInterval;
    }

    /**
     * Callback fired when the time is up.
     */
    abstract void onFinish();

    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    abstract void onTick(long millisUntilFinished);

    /**
     * Start the countdown.
     *
     * @param application the application that can manage the timer.
     */
    public final CountDownTimer start(final TApplication application) {
        long startTime = System.currentTimeMillis();
        millisInFuture += startTime;
        timer = application.addTimer(interval, true,
            new TAction() {
                public void DO() {
                    long now = System.currentTimeMillis();
                    long remaining = millisInFuture - now;
                    if (remaining <= 0) {
                        onFinish();
                        timer.setRecurring(false);
                        return;
                    }
                    onTick(remaining);
                }
            }
        );
    }

}
