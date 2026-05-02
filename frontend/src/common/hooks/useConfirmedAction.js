import { useNavigate } from "react-router";
import useConfirmDialog from "./useConfirmDialog.js";
import useSnackbar from "./useSnackbar.js";

const useConfirmedAction = () => {
    const navigate = useNavigate();
    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();

    const runConfirmedAction = async ({
                                          confirmOptions,
                                          action,
                                          successMessage,
                                          errorMessage = "Something went wrong",
                                          navigateTo
                                      }) => {
        const confirmed = await confirm(confirmOptions);

        if (!confirmed) {
            return false;
        }

        try {
            if (action) {
                await action();
            }

            if (successMessage) {
                showSnackbar(successMessage, "success");
            }

            if (navigateTo) {
                navigate(navigateTo);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                errorMessage;

            showSnackbar(message, "error");

            return false;
        }
    };

    return {
        runConfirmedAction
    };
};

export default useConfirmedAction;