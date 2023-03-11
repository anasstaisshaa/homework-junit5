package com.dmdev.dao;

import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubscriptionDaoTest extends IntegrationTestBase {
    private final SubscriptionDao subscriptionDao = SubscriptionDao.getInstance();

    @Test
    void findAll() {
        Subscription subscription1 = getSubscription("Ivan", 1);
        Subscription subscription2 = getSubscription("Petr", 2);
        Subscription subscription3 = getSubscription("Alex", 3);

        subscriptionDao.insert(subscription1);
        subscriptionDao.insert(subscription2);
        subscriptionDao.insert(subscription3);

        List<Subscription> actualResult = subscriptionDao.findAll();
        assertThat(actualResult).hasSize(3);
        List<Integer> subscriptionIds = actualResult.stream()
                .map(Subscription::getId)
                .toList();
        assertThat(subscriptionIds).contains(subscription1.getId(), subscription2.getId(), subscription3.getId());
    }

    @Test
    void findById() {
        Subscription subscription = getSubscription("Ivan", 1);

        subscriptionDao.insert(subscription);
        Optional<Subscription> actualResult = subscriptionDao.findById(subscription.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(subscription);
    }
    @Test
    void findByIdFailed(){
        Subscription subscription = getSubscription("Ivan", 1);

        subscriptionDao.insert(subscription);
        Optional<Subscription> actualResult = subscriptionDao.findById(123);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void delete() {
        Subscription subscription = getSubscription("Ivan", 1);

        subscriptionDao.insert(subscription);
        boolean actualResult = subscriptionDao.delete(subscription.getId());

        assertTrue(actualResult);
    }
    @Test
    void deleteFailed() {
        Subscription subscription = getSubscription("Ivan", 1);

        subscriptionDao.insert(subscription);
        boolean actualResult = subscriptionDao.delete(123);

        assertFalse(actualResult);
    }

    @Test
    void update() {
        Subscription subscription = getSubscription("Ivan", 1);
        subscriptionDao.insert(subscription);
        subscription.setName("new Ivan");

        subscriptionDao.update(subscription);

        Subscription updatedSubscription = subscriptionDao.findById(subscription.getId()).get();
        assertThat(updatedSubscription).isEqualTo(subscription);
    }

    @Test
    void insert() {
        Subscription subscription = getSubscription("Ivan", 1);

        Subscription actualResult = subscriptionDao.insert(subscription);

        assertThat(actualResult).isNotNull();
    }

    @Test
    void findByUserId() {
        Subscription subscription = getSubscription("Ivan", 1);
        subscriptionDao.insert(subscription);

        List<Subscription> actualResult = subscriptionDao.findByUserId(subscription.getUserId());

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0)).isEqualTo(subscription);
    }
    private static Subscription getSubscription(String name, Integer userId){
        return Subscription.builder()
                .userId(userId)
                .name(name)
                .provider(Provider.APPLE)
                .expirationDate(Instant.ofEpochSecond(12345))
                .status(Status.ACTIVE)
                .build();
    }
}