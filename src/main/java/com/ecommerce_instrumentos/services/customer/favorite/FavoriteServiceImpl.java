package com.ecommerce_instrumentos.services.customer.favorite;

import com.ecommerce_instrumentos.dto.FavoriteDto;
import com.ecommerce_instrumentos.entity.Favorite;
import com.ecommerce_instrumentos.entity.Product;
import com.ecommerce_instrumentos.entity.User;
import com.ecommerce_instrumentos.repository.FavoriteRepository;
import com.ecommerce_instrumentos.repository.ProductRepository;
import com.ecommerce_instrumentos.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public void addFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + productId));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {
        Optional<Favorite> favoriteOptional = favoriteRepository.findByUser_IdAndProduct_Id(userId, productId);
        favoriteOptional.ifPresent(favoriteRepository::delete);
    }

    @Override
    public List<FavoriteDto> getFavoritesByUser(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUser_Id(userId);
        return favorites.stream()
                .map(this::convertToFavoriteDto)
                .collect(Collectors.toList());
    }

    private FavoriteDto convertToFavoriteDto(Favorite favorite) {
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setId(favorite.getId());
        favoriteDto.setUserId(favorite.getUser().getId());
        favoriteDto.setProductId(favorite.getProduct().getId());
        favoriteDto.setProductName(favorite.getProduct().getName());


        return favoriteDto;
    }



}
