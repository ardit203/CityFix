export const cleanObject = (obj) => {
    if (!obj) return {};
    return Object.fromEntries(
        Object.entries(obj).filter(([key, value]) => value !== "")
    );
};
