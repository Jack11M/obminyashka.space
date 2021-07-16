const ua = {
  auth: {
    login: 'Вхід',
    signUp: 'Реєстрація',
    logEmail: 'Введіть ваш E-mail або логін',
    logPassword: 'Введіть Ваш пароль',
    regEmail: 'Введіть ваш E-mail',
    regLogin: 'Введіть ваш логін',
    regPassword: 'Введіть Ваш пароль',
    regConfirm: 'Повторіть пароль',
    remember: "Запам'ятати мене",
    noLogin: 'Чи не можете увійти?',
    agreement:
      '* Я погоджуюсь з правилами використання сервісу, а також з передачею і обробкою моїх даних. Я підтверджую своє повноліття і відповідальність за розміщення оголошень.',
  },
  popup: {
    serverResponse: 'Відповідь від сервера',
    errorTitle: 'Ошибка',
    notEmptyInput: 'Потрібно хоча б одне поле заповнене.',
  },
  errors: {
    requireField: "Поле обов'язково",
    invalidEmailFormat: 'Невірний формат пошти',
    max129: 'Повинно бути не більше 129 символів',
    min2: 'Повинно бути не менше 2 символів',
    max50: 'Повинно бути не більше 50 символів',
    altCodeMatch: 'Не повинно бути Alt code',
    min8: 'Повинно бути не менше 8 символів',
    max30: 'Повинно бути не більше 30 символів',
    passwordMatch: 'Великі і малі латинські букви і цифри',
    passwordMismatch: 'Паролі не співпадають',
    noSpace: 'Не повинно бути пробілів',
    nameMatch: 'Будь-які літери',
    phoneMatch: 'Невірний формат телефону',
    passwordIdentical: 'Пароль збігається з поточним',
    emailIdentical: 'Email збігається зі старим',
    emailNotIdentical: 'Email не збігається',
  },
  panel: {
    myActivity: 'Моя активність',
    myProfile: 'Мій профіль',
    myFavorite: 'Вибране',
    mySettings: 'Налаштування',
    myExit: 'Вихід',
  },
  ownInfo: {
    aboutMe: 'Про себе',
    firstName: "Iм'я:",
    lastName: 'Прізвище:',
    phone: 'Телефон:',
    children: 'Діти',
    dateOfBirth: 'Дата народження:',
    gender: 'Стать:',
    boy: 'Хлопчик',
    girl: 'Дівчинка',
    unselected: 'Не вибрано',
  },
  settings: {
    changePassword: 'Змінити пароль',
    currentPassword: 'Поточний пароль:',
    newPassword: 'Новий пароль:',
    confirmPassword: 'Повторіть пароль:',
    changeEmail: 'Змінити E-MaiL',
    oldEmail: 'Старий E-Mail:',
    newEmail: 'Новий E-Mail:',
    confirmEmail: 'Повторіть E-Mail:',
    remove: 'Видалити акаунт',
    describe:
      'Увага! Після видалення облікового запису, ваш профіль буде повністю видалений з каталогу сайту і багато функцій перестануть бути доступними. Ви можете поміняти контактні дані та номери телефонів в розділі меню',
    profile: 'Профіль',
  },
  exit: {
    question: 'Вийти?',
    text: 'Ви не зможете залишати повідомлення і додавати оголошення!',
    exit: 'вихід',
  },
  button: {
    addAdv: 'Додати оголошення',
    addField: 'Додати поле',
    removeField: 'Видалити поле',
    saveChanges: 'Зберегти зміни',
    enter: 'Увійти',
    look: 'Дивитись',
    save: 'ЗБЕРЕГТИ',
    saveEmail: 'ЗБЕРЕГТИ EMAIL',
    remove: 'ВИДАЛИТИ АКАУНТ',
  },
  header: {
    about: 'Про проект',
    goodness: 'Добра справа',
    myOffice: 'Мій кабінет',
    categories: 'Категорії',
    iSearch: 'Я шукаю',
    clothes: 'Одяг',
    shoes: 'Взуття',
    toys: 'іграшки',
    vehicles: 'Транспорт для дітей',
    furniture: 'Дитячі меблі',
    babies: 'Малюки до року',
    books: 'Книги',
    another: 'інше',
  },
  footer: {
    rules: 'Правила безпечної угоди',
    charity: 'Благодійні організації',
    questions: 'Часті питання',
    protect: 'Всі права захищені',
  },
  fourOhFour: {
    noPage: 'Сторінку не знайдено!',
    mainPage: 'На головну',
    backPage: 'Назад',
  },
  somethingBad: {
    error: 'Помилка! Що будемо робити?',
  },
  product: {
    categories: 'Категорії',
    blueTitle: 'Вас також можуть зацікавити',
    dateOfAdv: 'Дата публікації',
    cityOfAdv: 'Місто',
    phoneOfAdv: 'Телефон',
    changesTo: 'Обмінює на',
    description: 'Опис',
    size: 'Розмір',
    age: 'Вік',
    season: 'Сезон',
    sex: 'Стать',
    button: 'Запропонувати обмін',
    checkInUl: 'ваші пропозиції',
  },
  mainPage: {
    question: 'Накопичилося багато ',
    questionBold: 'дитячих речей?',
    answerFirstPart: 'Просто',
    answerBold: 'зареєструйся',
    answerSecondPart: 'і розмісти',
    answerThirdPart: 'оголошення!',
    changeBold: 'Обміняйтесь',
    changeFirstPart: 'з іншими користувачами',
    changeSecondPart: 'на щось кльове і',
    changeThirdPart: 'корисне!',
    blueText: 'Поточні пропозиції',
    blueSearch: 'Розширений пошук',
    blueSlideSubtitle: 'Величезний вибір',
    blueSlideTitle: 'Іграшок',
    greenSlideSubtitle: 'Різноманітність дитячого та підліткового',
    greenSlideTitle: 'Одягу',
    yellowSlideSubtitle: 'Все для',
    yellowSlideTitle: 'Малюків',
    pinkSlideSubtitle: 'Безліч дитячих',
    pinkSlideTitle: 'Меблів',
    lilacSlideSubtitle: 'Різноманітність дитячого та підліткового',
    lilacSlideTitle: 'Взуття',
    orangeSlideSubtitle: 'Дитячий',
    orangeSlideTitle: 'Транспорт',
    helpTitle: 'Чужих дітей не буває!',
    helpName: 'Обміняшка',
    helpText:
      'співпрацює з волонтерськими організаціями по\n' +
      '         всій Україні! Ти теж можеш допомогти! Віддай свої непотрібні речі, вони\n' +
      '         потраплять до дитячих будинків і притулків!',
    helpButton: 'я хочу допомогти дітям!',
  },
};

export default ua;
