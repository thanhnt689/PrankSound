package com.example.soundprank.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.os.SystemClock

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/**
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 *
 * Example of showing a 30 second countdown in a text field:
 *
 * <pre class="prettyprint">
 * new CountdownTimer(30000, 1000) {
 *
 * public void onTick(long millisUntilFinished) {
 * mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
 * }
 *
 * public void onFinish() {
 * mTextField.setText("done!");
 * }
 * }.start();
</pre> *
 *
 * The calls to [.onTick] are synchronized to this object so that
 * one call to [.onTick] won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * [.onTick] takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */
abstract class CountDownTimer(
    /**
     * Millis since epoch when alarm should stop.
     */
    private val mMillisInFuture: Long,
    /**
     * The interval in millis that the user receives callbacks
     */
    private var mCountdownInterval: Long
) {
    private var mStopTimeInFuture: Long = 0
    private var mPauseTime: Long = 0
    private var mCancelled = false
    private var mPaused = false

    /**
     * Cancel the countdown.
     *
     * Do not call it from inside CountDownTimer threads
     */
    fun cancel() {
        mHandler.removeMessages(MSG)
        mCancelled = true
    }

    /**
     * Start the countdown.
     */
    @Synchronized
    fun start(): CountDownTimer {
        if (mMillisInFuture <= 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        mCancelled = false
        mPaused = false
        return this
    }

    /**
     * Pause the countdown.
     */
    fun pause(): Long {
        mPauseTime = mStopTimeInFuture - SystemClock.elapsedRealtime()
        mPaused = true
        return mPauseTime
    }

    /**
     * Resume the countdown.
     */
    fun resume(): Long {
        mStopTimeInFuture = mPauseTime + SystemClock.elapsedRealtime()
        mPaused = false
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return mPauseTime
    }

    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    abstract fun onTick(millisUntilFinished: Long)

    /**
     * Callback fired when the time is up.
     */
    abstract fun onFinish()

    // handles counting down
    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            synchronized(this@CountDownTimer) {
                if (!mPaused) {
                    val millisLeft =
                        mStopTimeInFuture - SystemClock.elapsedRealtime()
                    if (millisLeft <= 0) {
                        onFinish()
                    } else if (millisLeft < mCountdownInterval) {
                        // no tick, just delay until done
                        sendMessageDelayed(obtainMessage(MSG), millisLeft)
                    } else {
                        val lastTickStart = SystemClock.elapsedRealtime()
                        onTick(millisLeft)

                        // take into account user's onTick taking time to execute
                        var delay =
                            lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime()

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval
                        if (!mCancelled) {
                            sendMessageDelayed(obtainMessage(MSG), delay)
                        }
                    }
                }
            }
        }
    }

    /**
     * @param millisInFuture The number of millis in the future from the call
     * to [.start] until the countdown is done and [.onFinish]
     * is called.
     * @param countDownInterval The interval along the way to receive
     * [.onTick] callbacks.
     */
    init {
        mCountdownInterval = mCountdownInterval
    }

    companion object {
        private const val MSG = 1
    }
}