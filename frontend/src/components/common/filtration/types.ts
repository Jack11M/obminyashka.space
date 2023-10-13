type Types = "radio" | "checkbox" | "input";

// export interface ICategory {
//   type?: Types;
//   name: string;
//   title: string;
//   categoryId: string;
//   openCategory: string[];
//   categoryBlock?: boolean;
//   categoryActive?: string;
//   hiddenCheckbox?: boolean;
//   subCategories: ISubCategoryData[];
//   setOpenCategory: (str: string[]) => void;
//   getCities?: (id: string) => Promise<ISubCategoryData[]>;
// }
// export interface ISubCategory {
//   type?: Types;
//   name: string;
//   categoryId: string;
//   subCategoryId: string;
//   containerName?: string;
//   subCategoryName: string;
//   isOpenCategory: boolean;
//   hiddenCheckbox?: boolean;
//   getCities?: (id: string) => Promise<ISubCategoryData[]>;
// }

// export interface ICategoryFilterData {
//   title: string;
//   categoryBlock?: boolean;
//   categoryActive?: string;
//   categoryFilterData: ICategoryData[];
//   getCities?: (id: string) => Promise<ISubCategoryData[]>;
// }

export interface ICategoryData {
  id: string;
  name: string;
  type?: Types;
  title: string;
  hiddenCheckbox?: boolean;
  subCategories: ISubCategoryData[];
}

export interface ISubCategoryData {
  id: string;
  name: string;
  containerName?: string;
}

// export interface ISelectedData {
//   id: string;
//   name: string;
// }
