package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findUserByEmail(String email);
	//idを指定してuserを取得する
	public User findUserById(int id);

	public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);

	@Modifying
	@Transactional
    @Query("UPDATE User u SET u.stripe_customer_id = :stripeCustomerId WHERE u.id = :userId")
    void updateStripeCustomerIdByUserId(@Param("userId") Integer userId, @Param("stripeCustomerId") String stripeCustomerId);
}
