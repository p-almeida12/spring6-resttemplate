package com.springframework.spring6resttemplate.client;

import com.springframework.spring6resttemplate.model.BeerDTO;
import com.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import com.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    public static final String GET_BEER_URL = "/api/v1/beer";
    public static final String GET_BEER_BY_ID_URL = "/api/v1/beer/{beerId}";

    @Override
    public Page<BeerDTO> listBeers() {
        return this.listBeersWithFilters(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeersWithFilters(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_URL);

        if(beerName != null){
            uriComponentsBuilder.queryParam("beerName", beerName);
        }

        if(beerStyle != null){
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }

        if(showInventory != null){
            uriComponentsBuilder.queryParam("showInventory", showInventory);
        }

        if(pageNumber != null){
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }

        if(pageSize != null){
            uriComponentsBuilder.queryParam("pageSize", pageSize);
        }

        ResponseEntity<BeerDTOPageImpl> pageResponseEntity = restTemplate
                .getForEntity(uriComponentsBuilder.toUriString(), BeerDTOPageImpl.class); //url for the spring6-playground repository

        return pageResponseEntity.getBody();
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(GET_BEER_BY_ID_URL, BeerDTO.class, beerId);
    }

    @Override
    public BeerDTO createBeer(BeerDTO beerDtoToAdd) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        URI uri = restTemplate.postForLocation(GET_BEER_URL, beerDtoToAdd);
        return restTemplate.getForObject(uri.getPath(), BeerDTO.class);
    }

    @Override
    public BeerDTO updateBeer(BeerDTO beerDTO) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(GET_BEER_BY_ID_URL, beerDTO, beerDTO.getId());
        return getBeerById(beerDTO.getId());
    }

    @Override
    public void deleteBeer(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(GET_BEER_BY_ID_URL, beerId);
    }
}
