package ku.cs.services;

import ku.cs.models.account.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
// ใช้ Mockito เพื่อให้ Mock fxRouter แทนเรียกตรง ๆ

public class SessionManagerTest {

    private User user;
    private Officer officer;
    private static MockedStatic<FXRouter> mockedRouter;

    @BeforeAll
    static void beforeAll() {
        mockedRouter = mockStatic(FXRouter.class);
    }

    @AfterAll
    static void afterAll() {
        mockedRouter.close();
    }

    @BeforeEach
    void setUp() {
        user = new User("u1", "User One", "pass",
                "u1@mail.com", "0812345678",
                Role.USER, LocalDateTime.now());

        officer = new Officer(2, "officerA", "Officer A", "pass",
                "o1@mail.com", "0811111111",
                Role.OFFICER, LocalDateTime.now());

        SessionManager.logoutTestHelper();
        mockedRouter.reset(); // reset calls ที่ verify
    }

    @Test
    void testLoginAndIsAuthenticated() {
        SessionManager.login(user);
        assertTrue(SessionManager.isAuthenticated());
        assertEquals(user, SessionManager.getCurrentAccount());

        mockedRouter.verify(() -> FXRouter.goTo("user-home"), atLeastOnce());
    }

    @Test
    void testLogout() {
        SessionManager.login(officer);
        assertTrue(SessionManager.isAuthenticated());

        SessionManager.logout();
        assertFalse(SessionManager.isAuthenticated());

        mockedRouter.verify(() -> FXRouter.goTo("officer-login"), atLeastOnce());
    }

    @Test
    void testHasRole() {
        SessionManager.login(officer);
        assertTrue(SessionManager.hasRole(Role.OFFICER));
        assertFalse(SessionManager.hasRole(Role.USER));
    }

    @Test
    void testGetOfficerAndUser() {
        SessionManager.login(officer);
        assertNotNull(SessionManager.getOfficer());
        assertNull(SessionManager.getUser());

        SessionManager.login(user);
        assertNotNull(SessionManager.getUser());
        assertNull(SessionManager.getOfficer());
    }

    @Test
    void testRequireRole() {
        SessionManager.login(user);
        SessionManager.requireRole(Role.USER, "user-login");
        assertEquals(user, SessionManager.getCurrentAccount());

        SessionManager.requireRole(Role.OFFICER, "officer-login");
        mockedRouter.verify(() -> FXRouter.goTo("officer-login"), atLeastOnce());
    }

    @Test
    void testRequireAdminOrOfficerLogin() {
        SessionManager.login(officer);
        SessionManager.requireAdminOrOfficerLogin();
        assertEquals(officer, SessionManager.getCurrentAccount());
    }
}
