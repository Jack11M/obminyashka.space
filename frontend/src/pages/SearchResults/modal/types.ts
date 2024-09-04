import { ReactNode } from "react";

export interface IModal {
  isModal: boolean;
  children: ReactNode;
  setIsModal: (bol: boolean) => void;
}
