package ec.edu.espe.roomreservation.repository;

import ec.edu.espe.roomreservation.model.RoomReservation;

public interface ReservationRepository {

    boolean existsByRoomCode(String roomCode);

    RoomReservation save(RoomReservation reservation);
}
