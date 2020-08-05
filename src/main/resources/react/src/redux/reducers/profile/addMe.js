const initialState = {
  me: [
    { fieldId: "firstName", label: "Имя", value: "" },
    { fieldId: "lastName", label: "Фамилия", value: "" },
    { fieldId: "city", label: "Город", value: "" },
    { fieldId: "phone", label: "Телефон", value: "" },
  ],
  children: [
    {
      childName: { fieldId: `child_${0}`, label: "Имя", value: "" },
      childAge: { fieldId: `ageChild_${0}`, label: "Возраст", value: "" },
    },
    {
      childName: { fieldId: `child_${1}`, label: "Имя", value: "" },
      childAge: { fieldId: `ageChild_${1}`, label: "Возраст", value: "" },
    },
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
