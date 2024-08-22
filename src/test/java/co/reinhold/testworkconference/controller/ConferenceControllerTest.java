package co.reinhold.testworkconference.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import co.reinhold.testworkconference.dto.ConferenceCreateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConferenceControllerTest {

    @LocalServerPort private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Nested
    class ConferenceCreate {

        @Test
        public void shouldCreateConference_WhenAdminRole() {
            String conferenceTitle = "Conference Title";
            var startDate = LocalDateTime.of(2024, 11, 1, 1, 1, 1);
            var endDate = LocalDateTime.of(2024, 11, 5, 10, 10, 1);
            var createRequest = new ConferenceCreateRequest(conferenceTitle, 10, startDate, endDate);

            given()
                    .auth()
                    .preemptive()
                    .basic("admin_user", "adminpass")
                    .contentType(ContentType.JSON)
                    .body(createRequest)
                    .when()
                    .post("/api/v1/conferences/create")
                    .then()
                    .statusCode(201)
                    .body("name", is(conferenceTitle))
                    .body("startDate", is("2024-11-01T01:01:01"))
                    .body("endDate", is("2024-11-05T10:10:01"));
        }

        @Test
        public void shouldNotCreateConference_WhenNotAdminRole() {
            String conferenceTitle = "Conference Title";
            var startDate = LocalDateTime.of(2024, 11, 1, 1, 1, 1);
            var endDate = LocalDateTime.of(2024, 11, 5, 10, 10, 1);
            var createRequest = new ConferenceCreateRequest(conferenceTitle, 10, startDate, endDate);

            given()
                    .auth()
                    .preemptive()
                    .basic("jane_smith", "securepassword")
                    .contentType(ContentType.JSON)
                    .body(createRequest)
                    .when()
                    .post("/api/v1/conferences/create")
                    .then()
                    .statusCode(403);
        }

        @Test
        public void shouldNotCreateConference_WhenNotCorrectCredentials() {
            String conferenceTitle = "Conference Title";
            var startDate = LocalDateTime.of(2024, 11, 1, 1, 1, 1);
            var endDate = LocalDateTime.of(2024, 11, 5, 10, 10, 1);
            var createRequest = new ConferenceCreateRequest(conferenceTitle, 10, startDate, endDate);

            given()
                    .auth()
                    .preemptive()
                    .basic("admin_user", "incorrect_password")
                    .contentType(ContentType.JSON)
                    .body(createRequest)
                    .when()
                    .post("/api/v1/conferences/create")
                    .then()
                    .statusCode(401);
        }

    }

    @Nested
    class ConferenceCancel {

        @Test
        public void shouldCancelConference_WhenAdminRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic("admin_user", "adminpass")
                    .when()
                    .delete("/api/v1/conferences/{id}", 100L)
                    .then()
                    .statusCode(204);
        }

        @Test
        public void shouldNotCancelConference_WhenNotAdminRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic("jane_smith", "securepassword")
                    .when()
                    .delete("/api/v1/conferences/{id}", 1L)
                    .then()
                    .statusCode(403);
        }

        @Test
        public void shouldNotCancelConference_WhenIncorrectCredentials() {
            given()
                    .auth()
                    .preemptive()
                    .basic("admin_user", "incorrect_password")
                    .when()
                    .delete("/api/v1/conferences/{id}", 1L)
                    .then()
                    .statusCode(401);
        }

    }

    @Nested
    class ConferenceCheckAvailability {

        @Test
        public void shouldCheckRoomAvailability_WhenUserRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic("jane_smith", "securepassword")
                    .when()
                    .get("/api/v1/conferences/{id}/check-availability", 100L)
                    .then()
                    .statusCode(200)
                    .body(is("true"));
        }

        @Test
        public void shouldCheckRoomAvailability_WhenAdminRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic("admin_user", "adminpass")
                    .when()
                    .get("/api/v1/conferences/{id}/check-availability", 100L)
                    .then()
                    .statusCode(200)
                    .body(is("true"));
        }

        @Test
        public void shouldNotCheckRoomAvailability_WhenIncorrectCredentials() {
            given()
                    .auth()
                    .preemptive()
                    .basic("wrong_user", "wrongpass")
                    .when()
                    .get("/api/v1/conferences/{id}/check-availability", 1L)
                    .then()
                    .statusCode(401);
        }

        @Test
        public void shouldNotCheckRoomAvailability_WhenIncorrectRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic("john_doe", "password123456")
                    .when()
                    .get("/api/v1/conferences/{id}/check-availability", 1L)
                    .then()
                    .statusCode(403);
        }

    }

    @Nested
    class ConferenceAddParticipant {

        @Test
        public void shouldAddParticipant_WhenUserRole() {
            Long conferenceId = 300L;
            Long participantId = 4L;

            given()
                    .auth()
                    .preemptive()
                    .basic("jane_smith", "securepassword")
                    .when()
                    .post("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(200)
                    .body(is("Participant added successfully"));
        }

        @Test
        public void shouldAddParticipant_WhenAdminRole() {
            Long conferenceId = 300L;
            Long participantId = 4L;

            given()
                    .auth()
                    .preemptive()
                    .basic("admin_user", "adminpass")
                    .when()
                    .post("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(200)
                    .body(is("Participant added successfully"));
        }

        @Test
        public void shouldNotAddParticipant_WhenIncorrectCredentials() {
            Long conferenceId = 1L;
            Long participantId = 2L;

            given()
                    .auth()
                    .preemptive()
                    .basic("wrong_user", "wrongpass")
                    .when()
                    .post("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(401);
        }

        @Test
        public void shouldNotAddParticipant_WhenIncorrectRole() {
            Long conferenceId = 100L;
            Long participantId = 2L;

            given()
                    .auth()
                    .preemptive()
                    .basic("john_doe", "password123456")
                    .when()
                    .post("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(403);
        }

    }

    @Nested
    class ConferenceRemoveParticipant {

        @Test
        public void shouldRemoveParticipant_WhenUserRole() {
            Long conferenceId = 100L;
            Long participantId = 2L;

            given()
                    .auth()
                    .preemptive()
                    .basic("jane_smith", "securepassword")
                    .when()
                    .delete("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(204);
        }

        @Test
        public void shouldRemoveParticipant_WhenAdminRole() {
            Long conferenceId = 400L;
            Long participantId = 5L;

            given()
                    .auth()
                    .preemptive()
                    .basic("admin_user", "adminpass")
                    .when()
                    .delete("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(204);
        }

        @Test
        public void shouldNotRemoveParticipant_WhenNotCorrectCredentials() {
            Long conferenceId = 1L;
            Long participantId = 2L;

            given()
                    .auth()
                    .preemptive()
                    .basic("wrong_user", "wrongpass")
                    .when()
                    .delete("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(401);
        }

        @Test
        public void shouldNotRemoveParticipant_WhenNoRole() {
            Long conferenceId = 1L;
            Long participantId = 2L;

            given()
                    .auth()
                    .preemptive()
                    .basic("john_doe", "password123456")
                    .when()
                    .delete("/api/v1/conferences/{conferenceId}/participants/{participantId}", conferenceId, participantId)
                    .then()
                    .statusCode(403);
        }

    }

}