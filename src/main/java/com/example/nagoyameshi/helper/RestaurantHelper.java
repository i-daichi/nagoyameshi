package com.example.nagoyameshi.helper;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.nagoyameshi.dto.RestaurantDTO;
import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.ReviewService;

@Component
public class RestaurantHelper {
    private final ReviewService reviewService;
    private final FavoriteService favoriteService;

    public RestaurantHelper(ReviewService reviewService, FavoriteService favoriteService) {
        this.reviewService = reviewService;
        this.favoriteService = favoriteService;
    }

    public void AddRestaurantDetails(
            Model model,
            Restaurant restaurant,
            User user,
            ReservationInputForm reservationInputForm) {
        boolean hasUserAlreadyReviewed = false;
        boolean isFavorite = false;
        Favorite favorite = null;

        if (user != null) {
            hasUserAlreadyReviewed = reviewService.hasUserAlreadyReviewed(restaurant, user);
            isFavorite = favoriteService.isFavorite(restaurant, user);
            if (isFavorite) {
                favorite = favoriteService.getFavorite(restaurant, user);
            }
        }

        List<Review> newReviews = reviewService.findTop6ByRestaurantOrderByCreatedAtDesc(restaurant);
        long totalReviewCount = reviewService.countByRestaurant(restaurant);

        model.addAttribute("restaurant", new RestaurantDTO(restaurant));
        model.addAttribute("reservationInputForm", reservationInputForm);
        model.addAttribute("hasUserAlreadyReviewed", hasUserAlreadyReviewed);
        model.addAttribute("newReviews", newReviews);
        model.addAttribute("totalReviewCount", totalReviewCount);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("favorite", favorite);
    }
}