package com.api.parking.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.parking.entity.History;

public interface HistoryRepository extends JpaRepository<History, Long>{

    @Query("SELECT SUM(hr.totalCost) FROM History hr WHERE hr.parkingRecord.parking.id = :parkingId AND "
        + "hr.parkingRecord.entryDate BETWEEN :startDate AND :endDate AND hr.parkingRecord.exitDate BETWEEN :startDate AND :endDate")
    Double calculateEarningsByDateRange(@Param("parkingId") Long parkingId, 
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    // @Query(value = "SELECT SUM(h.pago_total) " +
    //         "FROM parqueaderos_vehiculos pv JOIN historial h " +
    //         "ON(pv.id=h.parqueadero_vehiculo_id) " +
    //         "WHERE pv.parqueadero_id = :parqueaderoId " +
    //         "AND (SELECT EXTRACT(WEEK FROM h.fecha_salida) AS semana_fecha_especifica) = :semanaActual " +
    //         "AND EXTRACT(MONTH FROM h.fecha_salida) = :mes " +
    //         "AND EXTRACT(YEAR FROM h.fecha_salida) = :anio", nativeQuery = true)
    // Long obtenerGananciasSemana(Long parqueaderoId, Long semanaActual, Long mes, Long anio);


    // @Query(value = "SELECT SUM(h.pago_total) " +
    //         "FROM parqueaderos_vehiculos pv JOIN historial h " +
    //         "ON(pv.id=h.parqueadero_vehiculo_id) " +
    //         "WHERE pv.parqueadero_id = :parqueaderoId " +
    //         "AND EXTRACT(MONTH FROM h.fecha_salida) = :mes " +
    //         "AND EXTRACT(YEAR FROM h.fecha_salida) = :anio", nativeQuery = true)
    // Long obtenerGananciasMes(Long parqueaderoId, Long mes, Long anio);

    // @Query(nativeQuery = true, value = "SELECT SUM(h.pago_total) " +
    //         "FROM parqueaderos_vehiculos pv JOIN historial h " +
    //         "ON(pv.id=h.parqueadero_vehiculo_id) " +
    //         "WHERE pv.parqueadero_id = :parqueaderoId " +
    //         "AND EXTRACT(YEAR FROM h.fecha_salida) = :anio")
    // Long obtenerGananciasAnio(Long parqueaderoId, Long anio);


    // @Query(value = "SELECT SUM(h.pago_total) " +
    //         "FROM parqueaderos_vehiculos pv JOIN historial h " +
    //         "ON(pv.id=h.parqueadero_vehiculo_id) " +
    //         "WHERE pv.parqueadero_id = :parqueaderoId AND TO_CHAR(fecha_salida, 'YYYY-MM-DD') LIKE :fechaHoy", nativeQuery = true)
    // Long obtenerGananciasHoy(Long parqueaderoId, String fechaHoy);
}