package com.stasienko.service;

import com.stasienko.model.*;
import com.stasienko.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ReservedProductService reservedProductService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ProductService productService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationService reservationService;

    private UUID userId;
    private UUID pharmacyId;
    private UUID reservationId;
    private Pharmacy pharmacy;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        pharmacyId = UUID.randomUUID();
        reservationId = UUID.randomUUID();

        pharmacy = new Pharmacy();
        pharmacy.setId(pharmacyId);
        pharmacy.setName("Test Pharmacy");

        user = new User();
        user.setId(userId);
    }

    @Test
    void testSaveProducts_NewUserAndReservationsCreated() {
        Product product = new Product();
        Medicine medicine = new Medicine();
        medicine.setPharmacy(pharmacy);
        product.setMedicine(medicine);

        List<Product> products = Collections.singletonList(product);

        when(userService.findUserById(userId)).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenReturn(user);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation r = invocation.getArgument(0);
            r.setId(UUID.randomUUID());
            return r;
        });

        int result = reservationService.saveProducts(userId, products);

        assertEquals(1, result, "Should have created one reservation");
        verify(userService).findUserById(userId);
        verify(userService).saveUser(any(User.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testSaveProducts_ExistingUser() {
        Product product = new Product();
        Medicine medicine = new Medicine();
        medicine.setPharmacy(pharmacy);
        product.setMedicine(medicine);

        when(userService.findUserById(userId)).thenReturn(user);

        reservationService.saveProducts(userId, Collections.singletonList(product));

        verify(userService, never()).saveUser(any(User.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(reservedProductService, times(1)).saveReservedProduct(eq(product), any(Reservation.class));
    }

    @Test
    void testGetReservationByPharmacyId() {
        List<Reservation> reservations = Collections.singletonList(new Reservation());
        when(reservationRepository.findByPharmacyId(pharmacyId)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationByPharmacyId(pharmacyId);
        assertEquals(reservations, result);
        verify(reservationRepository).findByPharmacyId(pharmacyId);
    }

    @Test
    void testGetReservationByUserId() {
        List<Reservation> reservations = Collections.singletonList(new Reservation());
        when(reservationRepository.findByUserId(userId)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationByUserId(userId);
        assertEquals(reservations, result);
        verify(reservationRepository).findByUserId(userId);
    }

    @Test
    void testNumOfPendingReservations() {
        Reservation res1 = new Reservation();
        res1.setStatus(0);
        Reservation res2 = new Reservation();
        res2.setStatus(1);
        Reservation res3 = new Reservation();
        res3.setStatus(0);

        when(reservationRepository.findByPharmacyId(pharmacyId)).thenReturn(Arrays.asList(res1, res2, res3));

        int count = reservationService.numOfPendingReservations(pharmacyId);
        assertEquals(2, count);
    }

    @Test
    void testUpdateReservationStatus() {
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setStatus(0);
        reservation.setPharmacy(pharmacy);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        Reservation updated = reservationService.updateReservationStatus(reservationId, 1);
        assertNotNull(updated);
        assertEquals(1, updated.getStatus());
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void testGetPharmacyId() {
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setPharmacy(pharmacy);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        UUID result = reservationService.getPharmacyId(reservationId);
        assertEquals(pharmacyId, result);
        verify(reservationRepository).findById(reservationId);
    }

    @Test
    void testReservationCollected() {
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        ReservedProduct rp1 = new ReservedProduct();
        rp1.setId(UUID.randomUUID());
        ReservedProduct rp2 = new ReservedProduct();
        rp2.setId(UUID.randomUUID());

        List<ReservedProduct> reservedProducts = Arrays.asList(rp1, rp2);
        when(reservedProductService.getProductsAssignedForReservation(reservationId)).thenReturn(reservedProducts);

        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        when(productService.getProductsForReservation(reservedProducts)).thenReturn(Arrays.asList(product1, product2));

        reservationService.reservationCollected(reservationId);

        verify(reservedProductService).deleteReservedProduct(rp1.getId());
        verify(reservedProductService).deleteReservedProduct(rp2.getId());
        verify(productService).deleteProductById(product1.getId());
        verify(productService).deleteProductById(product2.getId());
        verify(reservationRepository).deleteById(reservationId);
    }

    @Test
    void testDeleteReservation() {
        ReservedProduct rp1 = new ReservedProduct();
        rp1.setId(UUID.randomUUID());
        ReservedProduct rp2 = new ReservedProduct();
        rp2.setId(UUID.randomUUID());

        List<ReservedProduct> reservedProducts = Arrays.asList(rp1, rp2);
        when(reservedProductService.getProductsAssignedForReservation(reservationId)).thenReturn(reservedProducts);

        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getProductsForReservation(reservedProducts)).thenReturn(products);

        reservationService.deleteReservation(reservationId);

        verify(productService).updateProductAvailability(product1.getId(), false);
        verify(productService).updateProductAvailability(product2.getId(), false);

        verify(reservedProductService).deleteReservedProduct(rp1.getId());
        verify(reservedProductService).deleteReservedProduct(rp2.getId());

        verify(reservationRepository).deleteById(reservationId);
    }
}
