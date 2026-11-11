package com.example.backendgigsapp.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GigsAppSteps {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final Map<String, String> tokens = new ConcurrentHashMap<>();
    private final Map<String, String> gigIds = new ConcurrentHashMap<>();
    private final Map<String, String> ownerIds = new ConcurrentHashMap<>();

    private ResponseEntity<String> lastResponse;

    public GigsAppSteps(@Value("${local.server.port}") int port, ObjectMapper objectMapper) {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
        this.objectMapper = objectMapper;
    }

    @Given("user {string} is registered with password {string}")
    public void userIsRegistered(String login, String password) {
        register(login, password, null, null);
        assertEquals(201, lastResponse.getStatusCode().value(), responseBody());
    }

    @When("user {string} registers with password {string}")
    public void userRegisters(String login, String password) {
        register(login, password, null, null);
    }

    @When("user {string} registers with password {string}, language {string} and currency {string}")
    public void userRegistersWithPreferences(String login, String password, String language, String currency) {
        register(login, password, language, currency);
    }

    @When("user {string} logs in with password {string}")
    public void userLogsIn(String login, String password) {
        requestText(HttpMethod.POST, "/login", null, Map.of("login", login, "password", password));
        if (lastResponse.getStatusCode().is2xxSuccessful() && lastResponse.getBody() != null) {
            tokens.put(login, lastResponse.getBody());
        }
    }

    @Then("response status is {int}")
    public void responseStatusIs(int expectedStatus) {
        assertNotNull(lastResponse, "No HTTP response has been captured");
        assertEquals(expectedStatus, lastResponse.getStatusCode().value(), responseBody());
    }

    @Then("response contains a JWT")
    public void responseContainsJwt() {
        assertNotNull(lastResponse);
        assertNotNull(lastResponse.getBody());
        assertTrue(lastResponse.getBody().matches(
                "[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+"
        ), "The login response should be a signed JWT");
    }

    @When("an unauthenticated client requests preferences")
    public void unauthenticatedClientRequestsPreferences() {
        requestText(HttpMethod.GET, "/user/preferences", null, null);
    }

    @When("an unauthenticated client updates preferences")
    public void unauthenticatedClientUpdatesPreferences() {
        requestText(HttpMethod.PUT, "/user/preferences", null,
                Map.of("language", "fr", "currency", "EUR"));
    }

    @When("user {string} requests their preferences")
    public void userRequestsPreferences(String login) {
        requestMap(HttpMethod.GET, "/user/preferences", login, null);
    }

    @When("user {string} updates preferences to language {string} and currency {string}")
    public void userUpdatesPreferences(String login, String language, String currency) {
        requestMap(HttpMethod.PUT, "/user/preferences", login,
                Map.of("language", language, "currency", currency));
    }

    @Then("response contains venue {string}")
    public void responseContainsVenue(String venue) {
        assertEquals(venue, requireMapBody().get("venue"));
    }

    @Then("response preferences are language {string} and currency {string}")
    public void responsePreferencesAre(String language, String currency) {
        Map body = requireMapBody();
        assertEquals(language, body.get("language"));
        assertEquals(currency, body.get("currency"));
    }

    @When("user {string} creates a gig")
    public void userCreatesGig(String login) {
        requestMap(HttpMethod.POST, "/addGigToList", login, gigPayload(null, "Le Splendid"));
        Map response = requireMapBody();
        gigIds.put(login, String.valueOf(response.get("id")));
        ownerIds.put(login, String.valueOf(response.get("userId")));
    }

    @When("user {string} reads their gig")
    public void userReadsTheirGig(String login) {
        requestMap(HttpMethod.GET, "/getGigById?id=" + gigId(login), login, null);
    }

    @When("user {string} updates their gig venue to {string}")
    public void userUpdatesTheirGig(String login, String venue) {
        requestMap(HttpMethod.POST, "/editGig", login, gigPayload(gigId(login), venue));
    }

    @When("user {string} deletes their gig")
    public void userDeletesTheirGig(String login) {
        requestText(HttpMethod.DELETE, "/deleteGig?id=" + gigId(login), login, null);
    }

    @When("user {string} tries to update {string}'s gig to venue {string}")
    public void userTriesToUpdateAnotherUsersGig(String login, String owner, String venue) {
        requestMap(HttpMethod.POST, "/editGig", login, gigPayload(gigId(owner), venue));
    }

    @When("user {string} tries to read {string}'s gig")
    public void userTriesToReadAnotherUsersGig(String login, String owner) {
        requestMap(HttpMethod.GET, "/getGigById?id=" + gigId(owner), login, null);
    }

    @When("user {string} tries to delete {string}'s gig")
    public void userTriesToDeleteAnotherUsersGig(String login, String owner) {
        requestText(HttpMethod.DELETE, "/deleteGig?id=" + gigId(owner), login, null);
    }

    @Then("the gig created by {string} still has venue {string}")
    public void gigStillHasVenue(String owner, String venue) {
        requestMap(HttpMethod.GET, "/getGigById?id=" + gigId(owner), owner, null);
        assertEquals(200, lastResponse.getStatusCode().value(), responseBody());
        assertEquals(venue, requireMapBody().get("venue"));
    }

    @Then("the gig created by {string} still belongs to that user")
    public void gigStillBelongsToOwner(String owner) {
        requestMap(HttpMethod.GET, "/getGigById?id=" + gigId(owner), owner, null);
        assertEquals(200, lastResponse.getStatusCode().value(), responseBody());
        assertEquals(ownerIds.get(owner), requireMapBody().get("userId"));
    }

    @Then("the gig created by {string} no longer exists")
    public void gigNoLongerExists(String owner) {
        requestMap(HttpMethod.GET, "/getGigById?id=" + gigId(owner), owner, null);
        assertEquals(404, lastResponse.getStatusCode().value(), responseBody());
    }

    @When("an unauthenticated client requests the gigs list")
    public void unauthenticatedClientRequestsGigs() {
        requestText(HttpMethod.GET, "/getGigsByUserId", null, null);
    }

    @When("user {string} requests the gigs list")
    public void userRequestsGigs(String login) {
        requestText(HttpMethod.GET, "/getGigsByUserId", login, null);
    }

    @When("user {string} logs out")
    public void userLogsOut(String login) {
        requestText(HttpMethod.POST, "/logout", login, null);
    }

    private void register(String login, String password, String language, String currency) {
        Map<String, Object> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);
        if (language != null) {
            body.put("language", language);
        }
        if (currency != null) {
            body.put("currency", currency);
        }
        requestText(HttpMethod.POST, "/register", null, body);
    }

    private void requestMap(HttpMethod method, String path, String login, Map<String, ?> body) {
        request(method, path, login, body);
    }

    private void requestText(HttpMethod method, String path, String login, Object body) {
        request(method, path, login, body);
    }

    private void request(HttpMethod method, String path, String login, Object body) {
        RestClient.RequestBodySpec request = restClient.method(method)
                .uri(path)
                .headers(headers -> headers.addAll(jsonHeaders(login)));
        if (body != null) {
            request.body(body);
        }
        lastResponse = request.exchange((clientRequest, clientResponse) -> {
            String payload;
            try {
                payload = new String(clientResponse.getBody().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            } catch (IOException exception) {
                throw new IllegalStateException("Unable to read HTTP response body", exception);
            }
            return ResponseEntity.status(clientResponse.getStatusCode())
                    .headers(headers -> headers.putAll(clientResponse.getHeaders()))
                    .body(payload);
        });
    }

    private HttpHeaders jsonHeaders(String login) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (login != null && tokens.containsKey(login)) {
            headers.setBearerAuth(tokens.get(login));
        }
        return headers;
    }

    private Map<String, Object> gigPayload(String id, String venue) {
        Map<String, Object> body = new HashMap<>();
        if (id != null) {
            body.put("id", id);
        }
        body.put("bands", List.of("Architects"));
        body.put("date", "2026-11-10");
        body.put("city", "Lille");
        body.put("country", "France");
        body.put("venue", venue);
        body.put("price", 35.0);
        return body;
    }

    private String gigId(String login) {
        String id = gigIds.get(login);
        assertNotNull(id, "No gig has been created for user " + login);
        return id;
    }

    private Map requireMapBody() {
        assertNotNull(lastResponse);
        assertNotNull(lastResponse.getBody(), responseBody());
        try {
            return objectMapper.readValue(lastResponse.getBody(), Map.class);
        } catch (Exception exception) {
            throw new AssertionError("The response body is not valid JSON: " + responseBody(), exception);
        }
    }

    private String responseBody() {
        return lastResponse == null ? null : lastResponse.getBody();
    }
}
