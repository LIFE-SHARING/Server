package com.umc.lifesharing.location.repository;

import com.umc.lifesharing.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
