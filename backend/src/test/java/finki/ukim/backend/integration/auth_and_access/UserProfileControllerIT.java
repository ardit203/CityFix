package finki.ukim.backend.integration.auth_and_access;

import com.fasterxml.jackson.databind.ObjectMapper;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserProfileControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_profile_it")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    private static final String PROFILE_URL = "/api/user-profile";

    /** Helper – register a user and return its JWT. */
    private Map<String, Object> registerAndLoginWithData(String username, String password, String email, Role role) throws Exception {
        Map<String, Object> profile = Map.of(
                "name", "TestName",
                "surname", "TestSurname",
                "gender", "MALE",
                "dateOfBirth", LocalDate.of(1995, 1, 1).toString(),
                "phoneNumber", "1234567890"
        );
        Map<String, Object> regBody = Map.of(
                "username", username,
                "password", password,
                "confirmPassword", password,
                "email", email,
                "role", role.name(),
                "userProfile", profile
        );
        MvcResult regResult = mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regBody)))
                .andExpect(status().isOk())
                .andReturn();

        Map<?, ?> userResponse = mapper.readValue(regResult.getResponse().getContentAsString(), Map.class);
        Long userId = ((Number) userResponse.get("id")).longValue();

        MvcResult loginResult = mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andReturn();

        Map<?, ?> loginResponse = mapper.readValue(loginResult.getResponse().getContentAsString(), Map.class);
        String token = (String) loginResponse.get("token");

        return Map.of("userId", userId, "token", token, "username", username);
    }

    @Test
    void getProfile_shouldReturn200_whenAuthenticated() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("p_user_" + uniqueSuffix, "TestPass11!", "p_user_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");

        mockMvc.perform(get(PROFILE_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("p_user_" + uniqueSuffix))
                .andExpect(jsonPath("$.userProfile.name").value("TestName"));
    }

    @Test
    void getProfile_shouldReturn401_whenUnauthenticated() throws Exception {
        mockMvc.perform(get(PROFILE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getProfileById_shouldReturn200_whenFound() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("id_user_" + uniqueSuffix, "TestPass11!", "id_user_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");
        Long userId = (Long) data.get("userId");

        mockMvc.perform(get(PROFILE_URL + "/id/" + userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userProfile.name").value("TestName"));
    }

    @Test
    void getProfileById_shouldReturn404_whenNotFound() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("id_user2_" + uniqueSuffix, "TestPass11!", "id_user2_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");

        mockMvc.perform(get(PROFILE_URL + "/id/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProfileByUsername_shouldReturn200_whenFound() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "uname_" + uniqueSuffix;
        Map<String, Object> data = registerAndLoginWithData(username, "TestPass11!", username + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");

        mockMvc.perform(get(PROFILE_URL + "/username/" + username)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void getProfileByUsername_shouldReturn404_whenNotFound() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("uname2_" + uniqueSuffix, "TestPass11!", "uname2_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");

        mockMvc.perform(get(PROFILE_URL + "/username/nonexistent_xyz")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProfile_shouldReturn200_whenDataIsValid() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("upd_" + uniqueSuffix, "TestPass11!", "upd_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");
        Long userId = (Long) data.get("userId");

        Map<String, Object> updatedProfile = Map.of(
                "name", "UpdatedName",
                "surname", "UpdatedSurname",
                "gender", "FEMALE",
                "dateOfBirth", "1992-02-02",
                "phoneNumber", "0987654321"
        );

        mockMvc.perform(put(PROFILE_URL + "/update/" + userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userProfile.name").value("UpdatedName"))
                .andExpect(jsonPath("$.userProfile.surname").value("UpdatedSurname"));
    }

    @Test
    void updateProfile_shouldReturn400_whenUserIdIsInvalid() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("upd2_" + uniqueSuffix, "TestPass11!", "upd2_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");

        Map<String, Object> updatedProfile = Map.of(
                "name", "UpdatedName",
                "surname", "UpdatedSurname"
        );

        mockMvc.perform(put(PROFILE_URL + "/update/999999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfile_shouldReturn401_whenUnauthenticated() throws Exception {
        Map<String, Object> updatedProfile = Map.of(
                "name", "UpdatedName",
                "surname", "UpdatedSurname"
        );

        mockMvc.perform(put(PROFILE_URL + "/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProfilePicture_shouldReturn200_whenValidFile() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("pic_" + uniqueSuffix, "TestPass11!", "pic_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");
        Long userId = (Long) data.get("userId");

        MockMultipartFile file = new MockMultipartFile(
                "profilePicture",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        mockMvc.perform(multipart(PROFILE_URL + "/update-profile-picture/" + userId)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userProfile.profilePicture").exists());
    }

    @Test
    void updateProfilePicture_shouldReturn401_whenUnauthenticated() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "profilePicture",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        mockMvc.perform(multipart(PROFILE_URL + "/update-profile-picture/1")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProfilePicture_shouldReturn400_whenFileIsEmpty() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("pic_emp_" + uniqueSuffix, "TestPass11!", "pic_emp_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");
        Long userId = (Long) data.get("userId");

        MockMultipartFile emptyFile = new MockMultipartFile(
                "profilePicture",
                "empty.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        mockMvc.perform(multipart(PROFILE_URL + "/update-profile-picture/" + userId)
                        .file(emptyFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfilePicture_shouldReturn400_whenInvalidContentType() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("pic_inv_" + uniqueSuffix, "TestPass11!", "pic_inv_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");
        Long userId = (Long) data.get("userId");

        MockMultipartFile invalidFile = new MockMultipartFile(
                "profilePicture",
                "test.csv",
                "text/csv",
                "a,b,c".getBytes()
        );

        mockMvc.perform(multipart(PROFILE_URL + "/update-profile-picture/" + userId)
                        .file(invalidFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfilePicture_shouldReplaceExistingPicture_whenUpdatedTwice() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("pic_rep_" + uniqueSuffix, "TestPass11!", "pic_rep_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");
        Long userId = (Long) data.get("userId");

        MockMultipartFile file1 = new MockMultipartFile(
                "profilePicture",
                "first.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "first-image-content".getBytes()
        );

        mockMvc.perform(multipart(PROFILE_URL + "/update-profile-picture/" + userId)
                        .file(file1)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        MockMultipartFile file2 = new MockMultipartFile(
                "profilePicture",
                "second.png",
                MediaType.IMAGE_PNG_VALUE,
                "second-image-content".getBytes()
        );

        mockMvc.perform(multipart(PROFILE_URL + "/update-profile-picture/" + userId)
                        .file(file2)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userProfile.profilePicture.fileName").value(org.hamcrest.Matchers.endsWith(".png")));
    }

    @Test
    void updateProfilePicture_shouldReturn400_whenProfileDoesNotExist() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> data = registerAndLoginWithData("pic_nf_" + uniqueSuffix, "TestPass11!", "pic_nf_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        String token = (String) data.get("token");

        MockMultipartFile file = new MockMultipartFile(
                "profilePicture",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "content".getBytes()
        );

        mockMvc.perform(multipart(PROFILE_URL + "/update-profile-picture/999999")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}
