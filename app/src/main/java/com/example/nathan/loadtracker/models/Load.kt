package com.example.nathan.loadtracker.models

class Load(
        var id: Int? = 0,
        var title: String? = null,
        var driver: String? = null,
        var unitId: String? = null,
        var material: String? = null,
        var timeLoaded: String? = null,
        var dateLoaded: String? = null,
        var created: String? = null,
        var modified: String? = null,
        var companyName: String? = null) {
    override fun toString(): String {
        return "ID: $id, Title: $title, Driver: $driver, Unit ID: $unitId, Material: $material, Company Name: $companyName, Time Loaded: $timeLoaded, Date Loaded: $dateLoaded, Created: $created, Modified: $modified"
    }
}
