package com.ecommerce_instrumentos.services.customer.favorite;

import com.ecommerce_instrumentos.dto.FavoriteDto;

import java.util.List;

public interface FavoriteService {

    void addFavorite(Long userId, Long productId);

    void removeFavorite(Long userId, Long productId);

    List<FavoriteDto> getFavoritesByUser(Long userId);

}
