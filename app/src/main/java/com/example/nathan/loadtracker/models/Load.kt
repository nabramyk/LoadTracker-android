package com.example.nathan.loadtracker.models

class Load(
        var id: Int? = null,
        var title: String,
        var driver: String,
        var unitId: String,
        var material: String,
        var timeLoaded: String,
        var dateLoaded: String,
        var created: String?,
        var modified: String? = null,
        var companyName: String?) {
    override fun toString(): String {
        return "ID: $id, Title: $title, Driver: $driver, Unit ID: $unitId, Material: $material, Company Name: $companyName, Time Loaded: $timeLoaded, Date Loaded: $dateLoaded, Created: $created, Modified: $modified"
    }
}