export interface IProvider {
  search: string;
  isFetch: boolean;
  setSearch: React.Dispatch<React.SetStateAction<string>>;
  setIsFetch: React.Dispatch<React.SetStateAction<boolean>>;
}
