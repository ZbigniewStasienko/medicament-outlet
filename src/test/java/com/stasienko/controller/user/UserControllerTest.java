package com.stasienko.controller.user;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.model.Product;
import com.stasienko.model.User;
import com.stasienko.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private ReservedProductService reservedProductService;

    @MockBean
    private PharmacyService pharmacyService;

    @MockBean
    private RetrieveNameService retrieveNameService;

    @BeforeEach
    void setup() {
        when(userService.findUserById(any(UUID.class))).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setPrice(10.0);
        when(productService.getProducts(anyString(), anyString(), anyDouble(), anyDouble())).thenReturn(singletonList(product));

        when(pharmacyService.calculateDistances(anyList(), anyDouble(), anyDouble())).thenReturn(new HashMap<>());
    }

    @Test
    void testGetProductsWithoutSearchTerm() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("isUser"))
                .andExpect(model().attribute("areDistancesEmpty", true))
                .andExpect(view().name("user/default-view"));

        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void testGetProductsWithSearchTerm() throws Exception {
        mockMvc.perform(get("/")
                        .param("searchTerm", "Aspirin"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("user/default-view"));
    }

    @Test
    void testViewPharmacy() throws Exception {
        UUID pharmacyId = UUID.randomUUID();
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(pharmacyId);

        when(pharmacyService.getPharmacyById(pharmacyId)).thenReturn(pharmacy);

        Medicine medicine = new Medicine();
        medicine.setId(UUID.randomUUID());
        medicine.setName("Aspirin");

        Product p = new Product();
        p.setId(UUID.randomUUID());
        p.setPrice(5.0);
        p.setIsReserved(false);
        p.setMedicine(medicine);
        p.setExpirationDate(LocalDate.now());
        List<Product> filteredProducts = Collections.singletonList(p);

        when(productService.getProducts(anyString(), anyString(), isNull(), isNull())).thenReturn(Collections.singletonList(p));
        when(productService.filterProductsBasedOnPharmacyId(anyList(), eq(pharmacyId))).thenReturn(filteredProducts);

        mockMvc.perform(get("/viewPharmacy")
                        .param("pharmacyId", pharmacyId.toString())
                        .param("searchTerm", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pharmacy", pharmacy))
                .andExpect(model().attribute("products", filteredProducts))
                .andExpect(view().name("user/pharmacy-view"));
    }

}
