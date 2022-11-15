package com.example.museum.integration;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.example.museum.dto.BusinessHourDto;
import com.example.museum.model.BusinessHour;
import com.example.museum.repository.BusinessHourRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BusinessHourIntegrationTest {
        @Autowired
        private TestRestTemplate client;
        @Autowired
        private BusinessHourRepository businessHourRepository;

        @BeforeEach
        @AfterEach
        public void clearDatabase() {
                businessHourRepository.deleteAll();
        }

        @Test
        public void testCreateGetUpdateBusinessHour() {
                int id = testCreateBusinessHour();
                testGetBusinessHour(id);
                testUpdateBusinessHour(id);
                testGetAllBusinessHours(id);
        }

        private int testCreateBusinessHour() {
                final Date day = Date.valueOf("2022-11-08");
                final Time openTime = Time.valueOf("08:29:00");
                final Time closeTime = Time.valueOf("16:45:00");
                final BusinessHourDto businessHourDto = new BusinessHourDto(
                                new BusinessHour(0, day, openTime, closeTime));

                ResponseEntity<BusinessHourDto> response = client.postForEntity("/businessHour", businessHourDto,
                                BusinessHourDto.class);

                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertNotNull(response.getBody());
                assertTrue(response.getBody().getBusinessHourID() > 0);
                // assertEquals(day, response.getBody().getDay());
                assertEquals(openTime, response.getBody().getOpenTime());
                assertEquals(closeTime, response.getBody().getCloseTime());

                return response.getBody().getBusinessHourID();
        }

        private void testGetBusinessHour(int id) {
                final Date day = Date.valueOf("2022-11-08");
                final Time openTime = Time.valueOf("08:29:00");
                final Time closeTime = Time.valueOf("16:45:00");
                ResponseEntity<BusinessHourDto> response = client.getForEntity("/businessHour/" + id,
                                BusinessHourDto.class);

                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(id, response.getBody().getBusinessHourID());
                // assertEquals(day, response.getBody().getDay());
                assertEquals(openTime, response.getBody().getOpenTime());
                assertEquals(closeTime, response.getBody().getCloseTime());
        }

        private void testGetAllBusinessHours(int id){
                final Date day = Date.valueOf("2022-11-08");
                final Time openTime = Time.valueOf("08:29:00");
                final Time closeTime = Time.valueOf("16:45:00");
//                restTemplate.postForObject("/businessHour/all", new BusinessHourList)
                ResponseEntity<Stream> responseDto = client.getForEntity("/businessHour/",
                        Stream.class);
//                Object[] forNow = template.getForObject("URL", Object[].class);
//                searchList = Arrays.asList(forNow);

                List<BusinessHourDto> response = (List<BusinessHourDto>) responseDto;
                assertNotNull(response);
                assertEquals(day, response.get(0).getDay());
                assertEquals(id, response.get(0).getBusinessHourID());
                assertEquals(openTime, response.get(0).getOpenTime());
                assertEquals(closeTime, response.get(0).getCloseTime());
        }

        // Testing for update, does not seem to be very simple, might not be needed
        private void testUpdateBusinessHour(int id) {
                ResponseEntity<BusinessHourDto> response = client.getForEntity("/businessHour/" + id,
                                BusinessHourDto.class);
                final Time prevOpenTime = response.getBody().getOpenTime();
                final Time updatedOpenTime = Time.valueOf("09:29:00");
                final BusinessHourDto businessHourDto = new BusinessHourDto(new BusinessHour(0,
                                response.getBody().getDay(), updatedOpenTime, response.getBody().getCloseTime()));
                ResponseEntity<BusinessHourDto> response2 = client.postForEntity("/businessHour/" + id, businessHourDto,
                                BusinessHourDto.class);

                assertNotNull(response2);
                assertEquals(HttpStatus.OK, response2.getStatusCode());
                assertNotNull(response2.getBody());
                assertEquals(id, response2.getBody().getBusinessHourID());
                // assertEquals(day, response2.getBody().getDay());
                assertNotEquals(prevOpenTime, response2.getBody().getOpenTime());
        }

        // @Test
        // public void testCreateInvalidBusinessHour() {
        // // Create a BusinessHour object and post it
        // final Date day1 = Date.valueOf("2022-11-08");
        // final Time openTime1 = Time.valueOf("08:29:00");
        // final Time closeTime1 = Time.valueOf("16:45:00");
        // final BusinessHourDto businessHourDto1 = new BusinessHourDto(
        // new BusinessHour(0, day1, openTime1, closeTime1));
        // ResponseEntity<BusinessHourDto> response1 =
        // client.postForEntity("/businessHour", businessHourDto1,
        // BusinessHourDto.class);

        // // Create another one with the same Date to test for conflicting Date
        // final Date day3 = response1.getBody().getDay();
        // final Time openTime3 = response1.getBody().getOpenTime();
        // final Time closeTime3 = response1.getBody().getCloseTime();
        // final BusinessHourDto businessHourDto3 = new BusinessHourDto(
        // new BusinessHour(0, day3, openTime3, closeTime3));

        // ResponseEntity<String> response3 = client.postForEntity("/businessHour",
        // businessHourDto3,
        // String.class);

        // assertNotNull(response3);
        // assertEquals(HttpStatus.CONFLICT, response3.getStatusCode());
        // assertEquals("A BusinessHour with the given date already exists",
        // response3.getBody());
        // }

        @Test
        public void testGetAllBusinessHoursEmpty(){
                ResponseEntity<List> response = client.getForEntity("/businessHour/all", List.class);
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        public void testGetInvalidBusinessHour() {
                ResponseEntity<String> response = client.getForEntity("/businessHour/" + Integer.MAX_VALUE,
                                String.class);

                assertNotNull(response);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                assertEquals("BusinessHour not found", response.getBody());
        }
}
