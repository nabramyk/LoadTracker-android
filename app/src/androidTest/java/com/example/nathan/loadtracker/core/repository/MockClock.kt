package com.example.nathan.loadtracker.core.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MockClock : Clock {

    val expectedInstantString = "2023-10-20T10:26:00Z"

    override fun now(): Instant {
        return Instant.parse(expectedInstantString)
    }
}