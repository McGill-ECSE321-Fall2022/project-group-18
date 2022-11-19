package com.example.museum.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.example.museum.dto.EmployeeHourDto;
import com.example.museum.model.EmployeeHour;
import com.example.museum.repository.EmployeeHourRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeHourIntegrationTest {
        @Autowired
        private TestRestTemplate client;
        @Autowired
        private EmployeeHourRepository employeeHourRepository;

        @BeforeEach
        @AfterEach
        public void clearDatabase() {
                employeeHourRepository.deleteAll();
        }

        @Test
        public void testCreateGetUpdateEmployeeHour() {
                int id = testCreateEmployeeHour();
                testCreateInvalidEmployeeHour();
                testGetEmployeeHour(id);
                testUpdateEmployeeHour(id);
                testGetAllEmployeeHours(id);
        }

        private int testCreateEmployeeHour() {
                final Date day = Date.valueOf("2022-11-08");
                final Time startTime = Time.valueOf("08:29:00");
                final Time endTime = Time.valueOf("16:45:00");
                final EmployeeHourDto employeeHourDto = new EmployeeHourDto(
                                new EmployeeHour(0, day, startTime, endTime));

                ResponseEntity<EmployeeHourDto> response = client.postForEntity("/employeeHour", employeeHourDto,
                                EmployeeHourDto.class);

                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertNotNull(response.getBody());
                assertTrue(response.getBody().getEmployeeHourID() > 0);
                assertEquals(day, response.getBody().getDay());
                assertEquals(startTime, response.getBody().getStartTime());
                assertEquals(endTime, response.getBody().getEndTime());

                return response.getBody().getEmployeeHourID();
        }

        public void testCreateInvalidEmployeeHour(){
                final Date day = Date.valueOf("2022-11-08");
                final Time startTime = Time.valueOf("08:45:00");
                final Time endTime = Time.valueOf("16:55:00");
                final EmployeeHourDto employeeHourDto = new EmployeeHourDto(
                        new EmployeeHour(0, day, startTime, endTime));

                try{
                        ResponseEntity<EmployeeHourDto> response = client.postForEntity("/employeeHour", employeeHourDto, EmployeeHourDto.class);
                        //we should not hit this line - an exception should be called before this
                        assertEquals(1,2);
                }catch(Exception e){
                        ResponseEntity<String> response = client.postForEntity("/employeeHour", employeeHourDto, String.class);
                        assertNotNull(response);
                        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
                }


        }

        private void testGetEmployeeHour(int id) {
                final Date day = Date.valueOf("2022-11-08");
                final Time startTime = Time.valueOf("08:29:00");
                final Time endTime = Time.valueOf("16:45:00");
                ResponseEntity<EmployeeHourDto> response = client.getForEntity("/employeeHour/" + id,
                                EmployeeHourDto.class);

                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(id, response.getBody().getEmployeeHourID());
                assertEquals(day, response.getBody().getDay());
                assertEquals(startTime, response.getBody().getStartTime());
                assertEquals(endTime, response.getBody().getEndTime());
        }

        // Testing for update, does not seem to be very simple, might not be needed
        private void testUpdateEmployeeHour(int id) {
                ResponseEntity<EmployeeHourDto> response = client.getForEntity("/employeeHour/" + id,
                                EmployeeHourDto.class);
                assertNotNull(response);

                final Time prevStartTime = response.getBody().getStartTime();
                final Time updatedStartTime = Time.valueOf("09:29:00");
                final EmployeeHourDto employeeHourDto = new EmployeeHourDto(new EmployeeHour(0,
                                response.getBody().getDay(), updatedStartTime, response.getBody().getEndTime()));
                ResponseEntity<EmployeeHourDto> response2 = client.postForEntity("/employeeHour/update/" + id, employeeHourDto,
                                EmployeeHourDto.class);

                assertNotNull(response2);
                assertEquals(HttpStatus.OK, response2.getStatusCode());
                assertNotNull(response2.getBody());
                assertEquals(id, response2.getBody().getEmployeeHourID());
                assertEquals(response.getBody().getDay(), response2.getBody().getDay());
                assertNotEquals(prevStartTime, response2.getBody().getStartTime());
        }

        private void testGetAllEmployeeHours(int id){
                final Date day = Date.valueOf("2022-11-08");
                final Time startTime = Time.valueOf("09:29:00");
                final Time endTime = Time.valueOf("16:45:00");



                ResponseEntity<List<EmployeeHourDto>> responseEntity =
                        client.exchange(
                                "/employeeHour/all",
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<EmployeeHourDto>>() {}
                        );

                assertNotNull(responseEntity);
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

                List<EmployeeHourDto> response = responseEntity.getBody();

                assertNotNull(response);
                assertEquals(1, response.size());
                assertEquals(day, response.get(0).getDay());
                assertEquals(id, response.get(0).getEmployeeHourID());
                assertEquals(startTime, response.get(0).getStartTime());
                assertEquals(endTime, response.get(0).getEndTime());
        }

        @Test
        public void testGetAllEmployeeHoursEmpty(){
                ResponseEntity<List> response = client.getForEntity("/employeeHour/all", List.class);
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        public void testGetInvalidEmployeeHour() {
                ResponseEntity<String> response = client.getForEntity("/employeeHour/" + Integer.MAX_VALUE,
                                String.class);

                assertNotNull(response);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                assertEquals("EmployeeHour not found", response.getBody());
        }

        @Test
        public void testUpdateInvalidEmployeeHour(){
                int id = Integer.MAX_VALUE;
                EmployeeHourDto employeeHourDto = new EmployeeHourDto();
                try{
                        ResponseEntity<EmployeeHourDto> response = client.postForEntity("/employeeHour/update/" + id, employeeHourDto, EmployeeHourDto.class);
                        //we should not hit this line - an exception should be called before this
                        assertEquals(1,2);
                }catch(Exception e){
                        ResponseEntity<String> response = client.postForEntity("/employeeHour/update/" + id, employeeHourDto, String.class);
                        assertNotNull(response);
                        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                }
        }

}
