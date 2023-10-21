package com.example.nathan.loadtracker.core.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class LoadTrackerClock: Clock {
    override fun now(): Instant {
        return Clock.System.now()
    }
}