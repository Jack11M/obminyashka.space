import React, { useEffect, useState } from "react";
import { Redirect } from "react-router-dom";
import Input from "./inputForForm/InputForForm.js";
import { dataLogin, dataRegister, errorsServer } from "./inputBase";
import { RegisterValidation } from "./validationInput";
import RenderError from "./renderError/RenderError";
import Button from "../../components/button/Button.js";
import Extra from "./extra/Extra.js";
import useFetch from "../hooks/doFetch";
import useLocalStorage from "../hooks/useLocalStorage";

import "./form.scss";

const Form = props => {
  const isLogin = props.match.path === "/logIn/";
  let arrayValueForm = isLogin ? dataLogin : dataRegister;
  const oneClass = isLogin ? "login" : "register";

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [readyToEnter, setReadyToEnter] = useState(false);

  const [isChecking, setIsChecking] = useState(false);

  const [loginInputs, setLoginInputs] = useState({});
  const [registerInputs, setRegisterInputs] = useState({});

  const [errorsInput, setErrorsInput] = useState({});

  const [{ isLoading, response, errorsResponse }, doFetch] = useFetch(
    "auth",
    oneClass
  );
  const [, setUser] = useLocalStorage("user", { isLogin, isChecking });
  const [isSuccess, setIsSuccess] = useState(false);
  const [isSingIn, setIsSingIn] = useState(false);

  const dataInputsState = isLogin ? loginInputs : registerInputs;
  const setDataInputsState = isLogin ? setLoginInputs : setRegisterInputs;

  useEffect(() => {
    setIsSuccess(false);
    setReadyToEnter(false);
    setIsChecking(false);
    setDataInputsState({});
    setErrorsInput({});
  }, [isLogin, setDataInputsState]);

  useEffect(() => {
    if (0 !== Object.keys(errorsInput).length) {
      setReadyToEnter(false);
      return;
    }
    const check = false;
    const checkTruth = () =>
      0 === Object.keys(errorsInput).length && checkAllInputs(check);

    if ((checkTruth() && isChecking) || isSubmitting) {
      setReadyToEnter(true);
    }
    if ((isLogin && checkTruth()) || isSubmitting) {
      setReadyToEnter(true);
    }
  }, [errorsInput, isChecking, isSubmitting]);

  useEffect(() => {
    if (!isSubmitting) {
      return;
    }
    doFetch(dataInputsState);
    setIsSubmitting(false);
    setReadyToEnter(true);
  }, [isSubmitting, dataInputsState]);

  useEffect(() => {
    if (!response) {
      setIsSingIn(false);
      return;
    }
    if (isLogin) {
      setUser(response);
      setIsSingIn(true);
    }

    if (response === "user registered") {
      setIsSuccess(true);
    }
  }, [response]);

  useEffect(() => {
    if (!errorsResponse) {
      return;
    }

    const inputs = findForm();

    const whatIsDataInput = arrayValueForm.find(item => {
      return item.message === errorsResponse.message;
    });

    const input = inputs.find(item => item.id === whatIsDataInput.id);
    const whatIsMessage = errorsServer.find(
      item => item.message === errorsResponse.message
    );
    const render = RenderError(input);
    render.showErrors(whatIsMessage.errorStatus);
  }, [errorsResponse]);

  if (isSuccess) {
    return <Redirect to="/logIn/" />;
  }
  if (isSingIn) {
    return <Redirect to="/" />;
  }

  const isValidInput = input => {
    const isThisValid = RegisterValidation(input, isLogin);
    const errorKey = oneClass + input.name;
    const render = RenderError(input);

    if (isThisValid) {
      render.showSuccess();
      setDataInputsState({
        ...dataInputsState,
        [input.name]: input.value
      });
      const errors = { ...errorsInput };
      delete errors[errorKey];
      setErrorsInput({ ...errors });
    } else {
      render.showErrors();
      setDataInputsState({
        ...dataInputsState,
        [input.name]: input.value
      });
      setErrorsInput({ ...errorsInput, [errorKey]: "" });
    }
  };

  const checkAllInputs = (check = true) => {
    const arr = findForm();
    if (!check) {
      return !arr.some(item => RegisterValidation(item, isLogin) === false);
    } else {
      arr.forEach(item => isValidInput(item));
    }
  };

  const findForm = () => {
    const formFields = document.forms[0].elements;
    let arr = [...formFields];
    return arr.filter(item => item.id !== "check" && item.tagName !== "BUTTON");
  };

  const handleSubmit = event => {
    event.preventDefault();
    checkAllInputs();
    if (!Object.keys(errorsInput).length && readyToEnter) {
      setIsSubmitting(true);
      setReadyToEnter(false);
    }
  };

  return (
    <form className={oneClass} onSubmit={handleSubmit}>
      {arrayValueForm.map(item => {
        return (
          <Input
            dataInput={item}
            key={oneClass + item.id}
            isValid={event => isValidInput(event.target)}
          />
        );
      })}
      <Extra whatIsPage={isLogin} setChecked={{ isChecking, setIsChecking }} />
      <Button
        text={isLogin ? "Войти" : "Регистрация"}
        disabling={!readyToEnter || isLoading}
        whatClass={"btn-auth"}
        width={'222px'}
        height={'48px'}
      />
    </form>
  );
};

export default Form;
