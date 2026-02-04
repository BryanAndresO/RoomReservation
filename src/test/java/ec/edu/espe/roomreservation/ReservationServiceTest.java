package ec.edu.espe.roomreservation;

import ec.edu.espe.roomreservation.dto.ReservationResponse;
import ec.edu.espe.roomreservation.model.RoomReservation;
import ec.edu.espe.roomreservation.repository.ReservationRepository;
import ec.edu.espe.roomreservation.service.ReservationService;
import ec.edu.espe.roomreservation.service.UserPolicyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private UserPolicyClient userPolicyClient;

    @BeforeEach
    public void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        userPolicyClient = Mockito.mock(UserPolicyClient.class);
        reservationService = new ReservationService(reservationRepository, userPolicyClient);
    }

    @Test
    void createReservation_validData_ShouldSaveReturnResponse(){
        //Arrange
        String roomCode = "LAB-101";
        String email = "baortiz7@espe.edu.ec";
        int hours = 4;

        when(reservationRepository.existsByRoomCode(roomCode)).thenReturn(Boolean.FALSE);
        when(userPolicyClient.isUserBlocked(email)).thenReturn(Boolean.FALSE);
        
        // Simulamos que al guardar, se devuelve una reserva con ID generado
        when(reservationRepository.save(any(RoomReservation.class))).thenAnswer(invocation -> {
            RoomReservation argument = invocation.getArgument(0);
            // Retornamos una nueva instancia simulando que la DB asignó un ID
            return new RoomReservation("generated-id-123", argument.getRoomCode(), 
                    argument.getReservedByEmail(), argument.getHours(), argument.getStatus());
        });

        //Act
        ReservationResponse response = reservationService.createReservation(roomCode, email, hours);

        //Assert
        assertNotNull(response.getId());
        assertEquals("generated-id-123", response.getId());
        assertEquals(roomCode, response.getRoomCode());
        assertEquals(email, response.getReservedByEmail());
        assertEquals(hours, response.getHours());
        assertEquals("CREATED", response.getStatus());

        verify(userPolicyClient).isUserBlocked(email);
        verify(reservationRepository).save(any(RoomReservation.class));
        verify(reservationRepository).existsByRoomCode(roomCode);
    }

    @Test
    void createReservation_invalidEmail_shouldThrow_andNotCallDependencies(){
        //Arrange
        String invalidEmail = "baortiz7-espe.edu.ec";

        //Act
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation("LAB-101", invalidEmail, 4));

        //No debe llamar a ninguna dependencia porque falla la validacion
        verifyNoInteractions(reservationRepository, userPolicyClient);
    }

    @Test
    void createReservation_hoursOutOfRange_shouldThrow_andNotCallDependencies(){
        //Arrange
        String roomCode = "LAB-101";
        String email = "baortiz7@espe.edu.ec";
        int invalidHours = 10;

        //Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> reservationService.createReservation(roomCode, email, invalidHours));

        assertEquals("Hours must be between 1 and 8", exception.getMessage());
        verifyNoInteractions(reservationRepository, userPolicyClient);
    }

    @Test
    void createReservation_roomAlreadyReserved_ShouldThrow(){
        //Arrange
        String roomCode = "LAB-101";
        String email = "baortiz7@espe.edu.ec";
        int hours = 4;

        when(userPolicyClient.isUserBlocked(email)).thenReturn(Boolean.FALSE);
        when(reservationRepository.existsByRoomCode(roomCode)).thenReturn(Boolean.TRUE);

        //Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
                () -> reservationService.createReservation(roomCode, email, hours));

        assertEquals("Room already reserved", exception.getMessage());
        verify(userPolicyClient).isUserBlocked(email);
        verify(reservationRepository).existsByRoomCode(roomCode);
        verify(reservationRepository, never()).save(any());
    }

}
