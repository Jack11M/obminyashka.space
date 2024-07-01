export interface ISelect {
  submit?: () => void;
  isLoading: boolean;
}

export interface ISelectOption {
  text: string;
  value?: string;
}

export interface IOnChangeValue {
  value: string;
  chosenOptions: ISelectOption[] | [];
}

export interface ICategories {
  id: number;
  name: string;
  subcategories: { id: number; name: string }[];
}
