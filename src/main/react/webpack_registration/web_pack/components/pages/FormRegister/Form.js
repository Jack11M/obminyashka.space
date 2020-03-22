import React, { useState, useEffect } from "react";
import { Redirect } from "react-router-dom";
import Input from "./inputForForm/InputForForm";
import { dataLogin, dataRegister, errorsServer } from "./inputBase";
import { RegisterValidation } from "./validationInput";
import RenderError from "../renderError/RenderError";
import { Button } from "./button/Button";
import Extra from "./extra/Extra";
import useFetch from "../hooks/doFetch";
import useLocalStorage from "../hooks/useLocalStorage";

import "./form.scss";

const Form = props => {
  const isLogin = props.match.path === "/registration";
  let arrayValueForm = isLogin ? dataLogin : dataRegister;
  const oneClass = isLogin ? "login" : "register";

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [readyToEnter, setReadyToEnter] = useState(false);

  const [isChecking, setIsChecking] = useState(false);

  const [loginInputs, setLoginInputs] = useState({});
  const [registerInputs, setRegisterInputs] = useState({});

  const [errorsInput, setErrorsInput] = useState({});

  const [{ isLoading, response, errorsResponse }, doFetch] = useFetch(oneClass);
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
    if (
      (0 === Object.keys(errorsInput).length &&
        isChecking &&
        checkAllInputs(check)) ||
      isSubmitting
    ) {
      setReadyToEnter(true);
    }
    if (
      (isLogin &&
        0 === Object.keys(errorsInput).length &&
        checkAllInputs(check)) ||
      isSubmitting
    ) {
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
    if (isLogin && isChecking) {
      setUser(response);
      setIsSingIn(true);
    }
    if (isLogin && !isChecking) {
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
    return <Redirect to="/registration" />;
  }
  if (isSingIn) {
    return (window.location.href = "http://localhost:8080");
  }

  const isValidInput = input => {
    const isThisValid = RegisterValidation(input, isLogin);
    const errorKey = oneClass + input.name;

    if (isThisValid) {
      const render = RenderError(input);
      render.showSuccess();
      setDataInputsState({
        ...dataInputsState,
        [input.name]: input.value
      });
      const errors = { ...errorsInput };
      delete errors[errorKey];
      setErrorsInput({ ...errors });
    } else {
      const render = RenderError(input);
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
    <form
      className={oneClass}
      onSubmit={event => {
        handleSubmit(event);
      }}
    >
      {arrayValueForm.map(item => {
        return (
          <Input
            dataInput={item}
            key={oneClass + item.id}
            isValid={isValidInput}
          />
        );
      })}
      <Extra whatIsPage={isLogin} setChecked={{ isChecking, setIsChecking }} />
      <Button whatIsForm={isLogin} disabling={!readyToEnter || isLoading} />
    </form>
  );
};

export default Form;
