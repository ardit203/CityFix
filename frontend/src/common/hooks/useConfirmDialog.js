import { useContext } from "react";
import ConfirmDialogContext from "../contexts/confirmDialogContext.js";

const useConfirmDialog = () => {
    const context = useContext(ConfirmDialogContext);

    if (!context) {
        throw new Error("useConfirmDialog must be used inside ConfirmDialogProvider");
    }

    return context;
};

export default useConfirmDialog;