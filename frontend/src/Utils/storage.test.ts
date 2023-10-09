import {getStorageUser, addDataToUserInStorage, getDefaultLang} from "./storage.ts";
import 'jest-localstorage-mock'; // needed for local/session storage mocking

const TEST_USER = {
    username: "test-user"
}

afterEach(() => localStorage.clear())

describe('[Mocks] Get user data from local | session storage', () => {
    test('should return user from local storage', () => {
        localStorage.setItem('user', JSON.stringify(TEST_USER))

        const user = getStorageUser();
        expect(user).toEqual(TEST_USER)
    })

    test('should return empty string for data absent', () => {
        const user = getStorageUser();
        expect(user).toEqual('')
    })
})

describe('[Mocks] Set user data to storage', () => {
    test('should add email to existing user data in local storage', () => {
        localStorage.setItem('user', JSON.stringify(TEST_USER))

        const email = "test-user@gmail.com";
        addDataToUserInStorage([`{"email": "${email}"}`])

        expect(Object.keys(localStorage.__STORE__).length).toBe(1);
        expect(localStorage.__STORE__['user']).toContain("username");
        expect(localStorage.__STORE__['user']).toContain(email);
    })
})

describe('[Mocks] Get default language from local storage', () => {
    test('should return value from storage when exists', () => {
        const expectedLanguage = 'ua';
        localStorage.setItem('lang', expectedLanguage)

        const defaultLang = getDefaultLang();
        expect(defaultLang).toBe(expectedLanguage)
    })
})