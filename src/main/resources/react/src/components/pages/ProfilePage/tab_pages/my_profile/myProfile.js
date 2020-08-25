import React from "react";
import Title from "../tabs_UI/title";
import Input from "../tabs_UI/input";

const MyProfile = () => {
  return (
    <>
      <Title text={"О себе"} />
      <form>
        <Input text={"Имя:"} />
        <Input text={"Фамилия:"} />
        <Input text={"Город:"} />
        <Input text={"Телефон:"} />

        <Title text={"Дети"} />
        <Input text={"Имя:"} />
        <Input text={"Возраст:"} />
      </form>
    </>
  );
};

export default MyProfile;
