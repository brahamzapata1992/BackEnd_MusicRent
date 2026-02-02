package com.ecommerce_instrumentos.controller.customer;

import com.ecommerce_instrumentos.dto.FavoriteDto;
import com.ecommerce_instrumentos.services.customer.favorite.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<String> addFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.addFavorite(userId, productId);
        return ResponseEntity.ok("Product added to favorites successfully.");
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok("Product removed from favorites successfully.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteDto>> getFavoritesByUser(@PathVariable Long userId) {
        List<FavoriteDto> favorites = favoriteService.getFavoritesByUser(userId);
        return ResponseEntity.ok(favorites);
    }

}
