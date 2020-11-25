package com.udacity.vehicles.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Implements testing of the CarController class.
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @BeforeEach
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        mvc.perform(
                get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$..condition").value("USED"))
                .andExpect(jsonPath("$..details.body").value("sedan"))
                .andExpect(jsonPath("$..details.model").value("Impala"))
                .andExpect(jsonPath("$..details.manufacturer.code").value(101))
                .andExpect(jsonPath("$..details.manufacturer.name").value("Chevrolet"))
                .andExpect(jsonPath("$..details.numberOfDoors").value(4))
                .andExpect(jsonPath("$..details.fuelType").value("Gasoline"))
                .andExpect(jsonPath("$..details.engine").value("3.6L V6"))
                .andExpect(jsonPath("$..details.mileage").value(32280))
                .andExpect(jsonPath("$..details.modelYear").value(2018))
                .andExpect(jsonPath("$..details.productionYear").value(2018))
                .andExpect(jsonPath("$..details.externalColor").value("white"))
                .andExpect(jsonPath("$..location.lat").value(40.73061))
                .andExpect(jsonPath("$..location.lon").value(-73.935242))
        ;
        verify(carService,times(1)).list();
    }

    /**
     * Tests the read operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        mvc.perform(
                get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(content().json("{}"))
                .andExpect(jsonPath("$..condition").value("USED"))
                .andExpect(jsonPath("$..details.body").value("sedan"))
                .andExpect(jsonPath("$..details.model").value("Impala"))
                .andExpect(jsonPath("$..details.manufacturer.code").value(101))
                .andExpect(jsonPath("$..details.manufacturer.name").value("Chevrolet"))
                .andExpect(jsonPath("$..details.numberOfDoors").value(4))
                .andExpect(jsonPath("$..details.fuelType").value("Gasoline"))
                .andExpect(jsonPath("$..details.engine").value("3.6L V6"))
                .andExpect(jsonPath("$..details.mileage").value(32280))
                .andExpect(jsonPath("$..details.modelYear").value(2018))
                .andExpect(jsonPath("$..details.productionYear").value(2018))
                .andExpect(jsonPath("$..details.externalColor").value("white"))
                .andExpect(jsonPath("$..location.lat").value(40.73061))
                .andExpect(jsonPath("$..location.lon").value(-73.935242))
        ;
        verify(carService,times(1)).findById(1L);
    }


    /**
     * Tests for successful update of new car in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void updateCar() throws Exception {
        Car car = getCar();
        car.getDetails().setNumberOfDoors(5);
        mvc.perform(
                put(new URI("/cars/1"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Tests the deletion of a single car by ID.
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        mvc.perform(
                delete("/cars/1"))
                .andExpect(status().is(204));
        verify(carService,times(1)).delete(1L);
    }

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}