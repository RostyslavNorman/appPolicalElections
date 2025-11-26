package ui;

import controllers.ElectionSystemController;

/**
 * Controllers that need access to the shared ElectionSystemController
 * must implement this interface.
 */
public interface UsesElectionController {

    /**
     * Passes the backend ElectionSystemController instance
     * to the child controller.
     */
    void setSystemController(ElectionSystemController controller);
}
