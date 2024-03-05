import axios, {AxiosError} from "axios";

export const getErrorMessage = (error: Error | AxiosError) => {
    if (axios.isAxiosError(error)) {
        return error?.response?.data?.error?.message
    }
    return error?.message || 'Something went wrong';
};
