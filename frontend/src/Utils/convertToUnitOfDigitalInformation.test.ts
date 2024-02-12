import {convertToUnitOfDigitalInformation, isImageExtensionSupported} from "./convertToUnitOfDigitalInformation.ts";

describe('Convert number to unit of digital information', () => {
    test('should return kilobytes', () => {
        const result = convertToUnitOfDigitalInformation(10.5 * 1024);
        expect(result).toMatchObject({value: 10.5, valueString: '10.50 KB'})
    })

    test('should 0 for 0', () => {
        const result = convertToUnitOfDigitalInformation(0);
        expect(result).toBe('0 bytes')
    })

    test.each`
    edgeValue | expectedResult
    ${Number.MIN_SAFE_INTEGER} | ${'0 bytes'}
    ${Number.MAX_SAFE_INTEGER} | ${'0 bytes'}
    `('should return 0 bytes for $edgeValue value', ({edgeValue, expectedResult}) => {
        const actual = convertToUnitOfDigitalInformation(edgeValue);
        expect(actual).toBe(expectedResult)
    })
})

describe('Is an image extension is supported by the service', () => {
    test.each([
        {imageType: 'image/jpeg', expected: true},
        {imageType: 'image/tiff', expected: false},
        {imageType: '', expected: false},
    ])(`should be "$expected" for "$imageType"`, ({imageType, expected}) => {
        const isIncorrectImageType = isImageExtensionSupported(imageType);
        expect(isIncorrectImageType).toBe(expected)
    })
})