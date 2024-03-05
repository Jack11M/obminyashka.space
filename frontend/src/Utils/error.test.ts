import {getErrorMessage} from "./error.ts";
import {AxiosError, AxiosHeaders, AxiosResponse} from "axios";

const AXIOS_RESPONSE: AxiosResponse = {
    data: {error: new AxiosError("test response error")},
    status: 401,
    statusText: "string",
    headers: new AxiosHeaders(),
    config: {headers: new AxiosHeaders()}
}

describe('Get error message', () => {
    test.each`
    error | behaviorExpectations | expectedErrorMessage
    ${{message: "", name: "", isAxiosError: true, response: AXIOS_RESPONSE}} | ${"axios error's message text"} | ${"test response error"}
    ${new Error("expected error")} | ${"an error's text for general error"} | ${"expected error"}
    ${new Error()} | ${'"Something went wrong" for errors without text'} | ${"Something went wrong"}
    `('should return $behaviorExpectations', ({error, expectedErrorMessage}) => {
        const errorMessage = getErrorMessage(error);
        expect(errorMessage).toEqual(expectedErrorMessage)
    })
})