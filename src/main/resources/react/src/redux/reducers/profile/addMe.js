const initialState = {
  me: [
    { fieldId: "firstName", label: "Имя", value: "Andrey" },
    { fieldId: "lastName", label: "Фамилия", value: "Teslenko" },
    { fieldId: "city", label: "Город", value: "Kharkov" },
    { fieldId: "phone", label: "Телефон", value: "vodafone" },
  ],
  children: [
    [
      { fieldId: `child_${1}`, label: "Имя", value: "Kirill" },
      { fieldId: `ageChild_${1}`, label: "Возраст", value: 18 },
    ],
    [
      { fieldId: `child_${2}`, label: "Имя", value: "Kirill" },
      { fieldId: `ageChild_${2}`, label: "Возраст", value: 18 },
    ],
  ],
};

const addMe = (state = initialState, action) => {
  switch (action.type) {
    case "PROFILE_LOADED":
      return action.payload;
    case "ADD_ME_INPUT_VALUE":
      return {
        ...state,
        me: { [action.name]: action.payload },
      };
    case "ADD_CHILDREN_INPUT_VALUE":
      return {
        ...state,
        children: [...action.payload],
      };
    default:
      return state;
  }
};

export default addMe;
