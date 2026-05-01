import { useContext } from "react";
import SnackbarContext from "../contexts/snackbarContext.js";

const useSnackbar = () => {
    const context = useContext(SnackbarContext);

    if (!context) {
        throw new Error("useSnackbar must be used inside SnackbarProvider");
    }

    return context;
};

export default useSnackbar;