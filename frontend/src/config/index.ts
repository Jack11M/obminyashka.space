const NUMBER_OF_CHARACTERS = /^.{1,129}@/;
const EMAIL_REG_EXP =
  /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/iu;

const PASSWORD_REG_EXP = /(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])/;

const PASSWORD_ALT_CODE_EXP =
  /^[A-Za-z0-9 «!»№;%:?*()_+\-,@#$^&[\]{}’".<>`~₴\\|/=]*[A-Za-z0-9][A-Za-z0-9 «!»№;%:?*()_+\-,@#$^&[\]{}’".<>`~₴\\|/=]*$/;

const USERNAME_ALT_CODE_EXP =
  /^[А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9 «!»№;%:?*()_+\-,@#$^&[\]{}’".<>`~₴\\|/=]*[А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9][А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9 «!»№;%:?*()_+\-,@#$^&[\]{}'".<>`~₴\\|/=]*$/;

const NAME_REG_EXP = /^[a-zA-Zа-яА-Я-'`ЁёҐЄІЇієїґ]+$/iu;

const USERNAME_REG_EXP =
  /^(?=.*[A-Za-zА-Яа-я-'`ЁёҐЄІЇієїґ])(?=.*\d*)(?=.[«!»№;%:?*()_+\-,@#$^&[\]{}’".<>`~₴|/=]*)[A-Za-zА-Яа-я-'`ЁёҐЄІЇієїґ\d«!»№;%:?*()_+\-,@#$^&[\]{}’".<>~₴|/=]$/;

// const PHONE_REG_EXP =
// /^\s*(?<country>\+?\d{2})[-.|0 (]*(?<area>\d{2})[-. )]*(?<number>\d{3}[-. ]*\d{2}[-. ]*\d{2})\s*$/;

const PHONE_REG_EXP = /^\+?[0-9() -]+$/;

const NO_SPACE = /^\S+$/;

export {
  NO_SPACE,
  NAME_REG_EXP,
  EMAIL_REG_EXP,
  PHONE_REG_EXP,
  USERNAME_REG_EXP,
  PASSWORD_REG_EXP,
  NUMBER_OF_CHARACTERS,
  PASSWORD_ALT_CODE_EXP,
  USERNAME_ALT_CODE_EXP,
};
