package ec.edu.espe.roomreservation.service;

import ec.edu.espe.roomreservation.dto.ReservationResponse;
import ec.edu.espe.roomreservation.model.RoomReservation;
import ec.edu.espe.roomreservation.repository.ReservationRepository;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserPolicyClient userPolicyClient;

    public ReservationService(ReservationRepository reservationRepository, UserPolicyClient userPolicyClient) {
        this.reservationRepository = reservationRepository;
        this.userPolicyClient = userPolicyClient;
    }

    //Crear una reserva si cumple con las reglas del negocio
    public ReservationResponse createReservation(String roomCode, String email, int hours) {
        //Validaciones de casos negativos

        if (roomCode == null || roomCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid room code");
        }
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (hours <= 0 || hours > 8) {
            throw new IllegalArgumentException("Hours must be between 1 and 8");
        }
        //Regla de negocio: usuario bloqueado
        if (userPolicyClient.isUserBlocked(email)) {
            throw new IllegalStateException("User blocked");
        }
        //Regla de negocio: No duplicar reserva por sala
        if (reservationRepository.existsByRoomCode(roomCode)) {
            throw new IllegalStateException("Room already reserved");
        }

        //Crear la reserva
        RoomReservation reservation = new RoomReservation(roomCode, email, hours);
        RoomReservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getId(), savedReservation.getRoomCode(),
                savedReservation.getReservedByEmail(), savedReservation.getHours(), savedReservation.getStatus());
    }


}
