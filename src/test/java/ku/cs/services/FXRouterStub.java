package ku.cs.services;

public class FXRouterStub {
    public static String lastRoute;

    public static void goTo(String route) {
        lastRoute = route;
    }
}
