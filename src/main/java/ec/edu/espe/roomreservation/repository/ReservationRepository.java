package ec.edu.espe.roomreservation.repository;

import ec.edu.espe.roomreservation.model.RoomReservation;

import java.util.Optional;

public interface ReservationRepository {

    boolean existsByRoomCode(String roomCode);

    RoomReservation save(RoomReservation reservation);

    Optional<RoomReservation> findById(String id);
}
