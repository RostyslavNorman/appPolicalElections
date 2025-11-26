package ui;

public class UIContext {

    private static MainLayoutController mainLayoutController;

    public static void setMainLayoutController(MainLayoutController controller) {
        mainLayoutController = controller;
    }

    public static MainLayoutController getMainLayoutController() {
        return mainLayoutController;
    }
}
