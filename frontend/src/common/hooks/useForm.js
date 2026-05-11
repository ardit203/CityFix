import {useState, useCallback} from "react";

const useForm = (initialState = {}) => {
    const [formData, setFormData] = useState(initialState);

    // 1. The Universal Change Handler
    const handleChange = useCallback((event) => {
        const {name, value, type, checked} = event.target;
        const finalValue = type === 'checkbox' ? checked : value;

        setFormData((prev) => {
            const newState = {...prev};
            const keys = name.split('.');

            let current = newState;
            for (let i = 0; i < keys.length - 1; i++) {
                // Ensure nested objects exist before trying to copy them
                current[keys[i]] = {...(current[keys[i]] || {})};
                current = current[keys[i]];
            }

            current[keys[keys.length - 1]] = finalValue;
            return newState;

            // // Optional: Clear the error for this specific field the moment the user starts typing
            // if (errors[name]) {
            //     setErrors((prev) => ({ ...prev, [name]: "" }));
            // }
        });
    }, []);

    // 2. A helper to manually update the whole form (used in useEffects)
    const resetForm = useCallback((newState) => {
        setFormData(newState);
    }, []);

    const handleSubmit = useCallback(async (event, validate, submitFun) => {
        event.preventDefault();
        console.log("Form Data")
        if (validate()) await submitFun(formData);
    }, [formData])

    return {
        formData,
        handleChange,
        resetForm,
        handleSubmit
    };
};

export default useForm;
