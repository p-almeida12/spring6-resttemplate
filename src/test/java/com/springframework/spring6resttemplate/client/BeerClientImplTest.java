package com.springframework.spring6resttemplate.client;

import com.springframework.spring6resttemplate.model.BeerDTO;
import com.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void testListBeers() {
        beerClient.listBeersWithFilters("ALE", null, null, null, null);
    }

    @Test
    void testListBeersNullName() {
        beerClient.listBeersWithFilters(null, null, null, null, null);
    }

    @Test
    void testGetBeerById() {

        Page<BeerDTO> beers = beerClient.listBeers();

        BeerDTO beerDTO = beers.getContent().get(0);

        BeerDTO beerById = beerClient.getBeerById(beerDTO.getId());

        assertNotNull(beerById);
    }

    @Test
    void testAddNewBeer() {
        BeerDTO beerDtoToAdd = BeerDTO.builder()
                .beerName("Beer Added")
                .beerStyle(BeerStyle.GOSE)
                .price(new BigDecimal("12.23"))
                .quantityOnHand(123)
                .upc("232342345")
                .build();

        BeerDTO savedBeerDto = beerClient.createBeer(beerDtoToAdd);
        assertNotNull(savedBeerDto);
    }

    @Test
    void testUpdateBeer() {
        BeerDTO newBeerDTO = BeerDTO.builder()
                .beerName("Beer Created")
                .beerStyle(BeerStyle.GOSE)
                .price(new BigDecimal("12.23"))
                .quantityOnHand(123)
                .upc("232342345")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newBeerDTO);

        final String newBeerName = "Beer Updated";
        beerDTO.setBeerName(newBeerName);
        BeerDTO updatedBeerDto = beerClient.updateBeer(beerDTO);

        assertEquals(newBeerName, updatedBeerDto.getBeerName());
    }

    @Test
    void testDeleteBeer() {
        BeerDTO newBeerDTO = BeerDTO.builder()
                .beerName("Beer Created")
                .beerStyle(BeerStyle.GOSE)
                .price(new BigDecimal("12.23"))
                .quantityOnHand(123)
                .upc("232342345")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newBeerDTO);

        beerClient.deleteBeer(beerDTO.getId());

        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(beerDTO.getId());
        });
    }
}