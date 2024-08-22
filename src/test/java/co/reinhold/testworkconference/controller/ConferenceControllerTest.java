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
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConferenceControllerTest {

    private static final String API_URL = "/api/v1/conferences";
    private static final String ADMIN_USER = "admin_user";
    private static final String ADMIN_PASS = "adminpass";
    private static final String USER_JANE = "jane_smith";
    private static final String USER_JANE_PASS = "securepassword";
    private static final String CONFERENCE_TITLE = "Conference Title";
    private static final String INCORRECT_PASSWORD = "incorrect_password";

    @LocalServerPort private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Nested
    class ConferenceCreate {

        private final String CREATE_URL = "%s/create".formatted(API_URL);
        private final LocalDateTime START_DATE = LocalDateTime.of(2024, 11, 1, 1, 1, 1);
        private final LocalDateTime END_DATE = LocalDateTime.of(2024, 11, 5, 10, 10, 1);

        @Test
        public void shouldCreateConference_WhenAdminRole() {
            var createRequest = new ConferenceCreateRequest(CONFERENCE_TITLE, 10, START_DATE, END_DATE);
            commonSetup(ADMIN_USER, ADMIN_PASS, createRequest)
                    .post(CREATE_URL)
                    .then()
                    .statusCode(201)
                    .body("name", is(CONFERENCE_TITLE))
                    .body("startDate", is("2024-11-01T01:01:01"))
                    .body("endDate", is("2024-11-05T10:10:01"));
        }

        @Test
        public void shouldNotCreateConference_WhenNotAdminRole() {
            var createRequest = new ConferenceCreateRequest(CONFERENCE_TITLE, 10, START_DATE, END_DATE);
            commonSetup(USER_JANE, USER_JANE_PASS, createRequest)
                    .post(CREATE_URL)
                    .then()
                    .statusCode(403);
        }

        @Test
        public void shouldNotCreateConference_WhenNotCorrectCredentials() {
            var createRequest = new ConferenceCreateRequest(CONFERENCE_TITLE, 10, START_DATE, END_DATE);
            commonSetup(ADMIN_USER, INCORRECT_PASSWORD, createRequest)
                    .post(API_URL + "/create")
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
                    .basic(ADMIN_USER, ADMIN_PASS)
                    .when()
                    .delete("%s/{id}".formatted(API_URL), 100L)
                    .then()
                    .statusCode(204);
        }

        @Test
        public void shouldNotCancelConference_WhenNotAdminRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic(USER_JANE, USER_JANE_PASS)
                    .when()
                    .delete("%s/{id}".formatted(API_URL), 1L)
                    .then()
                    .statusCode(403);
        }

        @Test
        public void shouldNotCancelConference_WhenIncorrectCredentials() {
            given()
                    .auth()
                    .preemptive()
                    .basic(ADMIN_USER, INCORRECT_PASSWORD)
                    .when()
                    .delete("%s/{id}".formatted(API_URL), 1L)
                    .then()
                    .statusCode(401);
        }

    }

    @Nested
    class ConferenceCheckAvailability {

        private static final String CHECK_AVAILABILITY_URL = "/{id}/check-availability";

        @Test
        public void shouldCheckRoomAvailability_WhenUserRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic(USER_JANE, USER_JANE_PASS)
                    .when()
                    .get("%s%s".formatted(API_URL, CHECK_AVAILABILITY_URL), 100L)
                    .then()
                    .statusCode(200)
                    .body(is("true"));
        }

        @Test
        public void shouldCheckRoomAvailability_WhenAdminRole() {
            given()
                    .auth()
                    .preemptive()
                    .basic(ADMIN_USER, ADMIN_PASS)
                    .when()
                    .get("%s%s".formatted(API_URL, CHECK_AVAILABILITY_URL), 100L)
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
                    .get("%s%s".formatted(API_URL, CHECK_AVAILABILITY_URL), 1L)
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
                    .get("%s%s".formatted(API_URL, CHECK_AVAILABILITY_URL), 1L)
                    .then()
                    .statusCode(403);
        }

    }

    @Nested
    class ConferenceAddParticipant {

        private static final String CONFERENCE_PARTICIPANT_URL = "/{conferenceId}/participants/{participantId}";

        @Test
        public void shouldAddParticipant_WhenUserRole() {
            Long conferenceId = 300L;
            Long participantId = 4L;

            given()
                    .auth()
                    .preemptive()
                    .basic(USER_JANE, USER_JANE_PASS)
                    .when()
                    .post("%s%s".formatted(API_URL, CONFERENCE_PARTICIPANT_URL), conferenceId, participantId)
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
                    .basic(ADMIN_USER, ADMIN_PASS)
                    .when()
                    .post("%s%s".formatted(API_URL, CONFERENCE_PARTICIPANT_URL), conferenceId, participantId)
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
                    .post("%s%s".formatted(API_URL, CONFERENCE_PARTICIPANT_URL), conferenceId, participantId)
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
                    .post("%s%s".formatted(API_URL, CONFERENCE_PARTICIPANT_URL), conferenceId, participantId)
                    .then()
                    .statusCode(403);
        }

    }

    @Nested
    class ConferenceRemoveParticipant {

        private static final String CONFERENCE_PARTICIPANT_URL = "/{conferenceId}/participants/{participantId}";

        @Test
        public void shouldRemoveParticipant_WhenUserRole() {
            Long conferenceId = 100L;
            Long participantId = 2L;

            given()
                    .auth()
                    .preemptive()
                    .basic(USER_JANE, USER_JANE_PASS)
                    .when()
                    .delete(API_URL + CONFERENCE_PARTICIPANT_URL, conferenceId, participantId)
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
                    .basic(ADMIN_USER, ADMIN_PASS)
                    .when()
                    .delete("%s%s".formatted(API_URL, CONFERENCE_PARTICIPANT_URL), conferenceId, participantId)
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
                    .delete("%s%s".formatted(API_URL, CONFERENCE_PARTICIPANT_URL), conferenceId, participantId)
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
                    .delete("%s%s".formatted(API_URL, CONFERENCE_PARTICIPANT_URL), conferenceId, participantId)
                    .then()
                    .statusCode(403);
        }

    }

    private RequestSpecification commonSetup(String username, String password, Object body) {
        return given()
                .auth()
                .preemptive()
                .basic(username, password)
                .contentType(ContentType.JSON)
                .body(body)
                .when();
    }

}