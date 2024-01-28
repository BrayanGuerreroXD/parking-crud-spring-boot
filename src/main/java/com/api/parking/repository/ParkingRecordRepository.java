package com.api.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.parking.entity.ParkingRecord;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    List<ParkingRecord> findByVehicleIdAndExitDateIsNull(Long vehicleId);

    List<ParkingRecord> findByParkingIdAndExitDateIsNull(Long parkingId);

    Integer countByParkingIdAndExitDateIsNull(Long parkingId);

    List<ParkingRecord> findByParkingIdAndParkingUserIdAndExitDateIsNull(Long parkingId, Long userId);

    ParkingRecord findFirstByVehicleIdAndParkingIdAndExitDateIsNull(Long vehicleId, Long parkingId);

    @Query("SELECT pr FROM ParkingRecord pr " +
        "WHERE pr.parking.id = :parkingId " +
        "ANd pr.exitDate IS NULL " +
        "AND (SELECT COUNT(r) FROM ParkingRecord r WHERE r.vehicle.id = pr.vehicle.id AND r.parking.id = :parkingId) = 1")
    List<ParkingRecord> getParkingRecordsFirstTimeWithExitDateNullByParkingId(@Param("parkingId") Long parkingId);

    @Query("SELECT pr FROM ParkingRecord pr " +
        "WHERE pr.parking.id = :parkingId " +
        "ANd pr.exitDate IS NULL " +
        "AND pr.parking.user.id = :userId " +
        "AND (SELECT COUNT(r) FROM ParkingRecord r WHERE r.vehicle.id = pr.vehicle.id AND r.parking.id = :parkingId) = 1")
    List<ParkingRecord> getParkingRecordsFirstTimeWithExitDateNullByParkingIdAndUserId(@Param("parkingId") Long parkingId, @Param("userId") Long userId);

    @Query("SELECT pr FROM ParkingRecord pr " +
        "WHERE pr.exitDate IS NULL " +
        "ANd pr.vehicle.plate LIKE %:plate%")
    List<ParkingRecord> getParkingRecordsByVehiclePlateMatches(@Param("plate") String plate);
    
    @Query("SELECT pr FROM ParkingRecord pr " +
        "WHERE pr.exitDate IS NULL " +
        "AND pr.parking.user.id = :userId " +
        "ANd pr.vehicle.plate LIKE %:plate%")
    List<ParkingRecord> getParkingRecordsByUserIdAndVehiclePlateMatches(@Param("userId") Long userId, @Param("plate") String plate);

    @Query(value = "SELECT v.vehi_id, v.vehi_plate, COUNT(pr.vehi_id) AS recordCount " +
        "FROM parkingrecord pr " +
        "JOIN vehicles v ON pr.vehi_id = v.vehi_id " +
        "GROUP BY v.vehi_id, v.vehi_plate " +
        "ORDER BY recordCount DESC " +
        "LIMIT 10", nativeQuery = true)
    List<Object[]> getMostRegisteredVehiclesAtAllParking();
    
    @Query(value = "SELECT v.vehi_id, v.vehi_plate, COUNT(pr.vehi_id) AS recordCount " +
        "FROM parkingrecord pr " +
        "JOIN vehicles v ON pr.vehi_id = v.vehi_id " +
        "JOIN parking p ON p.park_id = pr.park_id " +
        "WHERE p.user_id = :userId " +
        "GROUP BY v.vehi_id, v.vehi_plate " +
        "ORDER BY recordCount DESC " +
        "LIMIT 10", nativeQuery = true)
    List<Object[]> getMostRegisteredVehiclesAtAllParkingByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT v.vehi_id, v.vehi_plate, COUNT(v.vehi_id) AS recordCount " +
        "FROM parkingrecord pr " +
        "JOIN vehicles v ON pr.vehi_id = v.vehi_id " +
        "WHERE pr.park_id = :parkingId " +
        "GROUP BY v.vehi_id, v.vehi_plate " +
        "ORDER BY recordCount DESC LIMIT 10;", nativeQuery = true)
    List<Object[]> getMostRegisteredVehiclesAtAllParkingByParkingId(@Param("parkingId") Long parkingId);

    
    @Query(value = "SELECT v.vehi_id, v.vehi_plate, COUNT(v.vehi_id) AS recordCount " +
        "FROM parkingrecord pr " +
        "JOIN vehicles v ON pr.vehi_id = v.vehi_id " +
        "JOIN parking p ON p.park_id = pr.park_id " +
        "WHERE pr.park_id = :parkingId AND p.user_id = :userId " +
        "GROUP BY v.vehi_id, v.vehi_plate " +
        "ORDER BY recordCount DESC " +
        "LIMIT 10", nativeQuery = true)
    List<Object[]> getMostRegisteredVehiclesAtAllParkingByParkingIdAndUserId(@Param("parkingId") Long parkingId, @Param("userId") Long userId);
}
