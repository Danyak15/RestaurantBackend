package org.example.restaurantbackend.controller.api

import org.example.restaurantbackend.dto.sync.SyncResponse
import org.example.restaurantbackend.service.SyncService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sync")
class SyncController(
    private val syncService: SyncService
) {
    @GetMapping
    fun sync(): ResponseEntity<SyncResponse> {
        return ResponseEntity.ok(syncService.getFullSync())
    }
}