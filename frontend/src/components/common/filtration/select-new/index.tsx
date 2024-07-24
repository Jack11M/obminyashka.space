/* eslint-disable react-hooks/exhaustive-deps */
import { ChangeEvent, useEffect, useRef, useState } from "react";

import { CheckBox } from "obminyashka-components";

import { includes, useOutsideClick } from "./helpers";
import { ISelectOption, ISelectProps } from "./types";

import * as Styles from "./styles";

export const Select = ({
  value,
  title,
  options,
  multiple,
  isActive,
  disabled,
  onChange,
  isLoading,
  filtration,
  setIsActive,
  notCheckbox,
  deleteOnClose,
  filteredParameterOptions,
}: ISelectProps) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [chosenOptions, setChosenOptions] = useState<ISelectOption[] | []>(
    filteredParameterOptions ?? []
  );
  const [filtrationValue, setFiltrationValue] = useState<string>("");

  const isDisabled = isLoading || disabled || !options?.length;
  const isOpenOptions = !isDisabled && (isActive || isOpen);
  const isEmptyFiltrationValue = !filtrationValue.trim().length;
  const filtered = options?.filter(
    (el) =>
      isEmptyFiltrationValue ||
      el.text.toLowerCase().startsWith(filtrationValue.toLowerCase())
  );

  const matchesOption =
    filtration &&
    chosenOptions.filter(({ text }) => text === filtrationValue)[0];

  const ref = useRef<HTMLDivElement | null>(null);

  const setOpen = () => {
    if (isDisabled) return;
    if (setIsActive) setIsActive();
    else setIsOpen(!isOpen);
  };

  const setOptions = (option: ISelectOption) => {
    const include = includes({ arr: chosenOptions, objectCheck: option });

    if (filtration) {
      setChosenOptions([option]);
      setFiltrationValue(option.text);
      setIsOpen(false);
      return;
    }

    if (!multiple) setChosenOptions([option]);
    else
      setChosenOptions(
        include
          ? chosenOptions.filter((el) => el.text !== option.text)
          : [...chosenOptions, option]
      );
  };

  const onChangeFiltration = (e: ChangeEvent<HTMLInputElement>) =>
    setFiltrationValue(e.target.value);

  const onBlur = () => {
    if (filtration) {
      if (!matchesOption && chosenOptions.length) {
        setFiltrationValue("");
        setChosenOptions([]);
        onChange({
          value: "",
          chosenOptions: [],
        });
      }

      setIsOpen(false);
    }

    if (isActive || isOpen) return;
    if (deleteOnClose && chosenOptions.length && (!isActive || !isOpen))
      setChosenOptions([]);
  };

  useOutsideClick(onBlur, ref);

  useEffect(() => {
    if (
      filteredParameterOptions &&
      filteredParameterOptions.length > 0 &&
      !chosenOptions.length
    ) {
      if (!filtration) {
        setOpen();
      }

      setChosenOptions(filteredParameterOptions);

      if (filtration) {
        setFiltrationValue(filteredParameterOptions?.[0].text);
      }
    }
  }, [filteredParameterOptions]);

  useEffect(() => {
    if (filtration) {
      if (matchesOption) {
        setFiltrationValue(matchesOption.text);
        setChosenOptions([matchesOption]);
        onChange({
          value: value ?? "",
          chosenOptions: chosenOptions,
        });
      }

      if (!filtrationValue) {
        setChosenOptions([]);
        onChange({
          value: "",
          chosenOptions: [],
        });
      }
    }
  }, [filtrationValue]);

  useEffect(() => {
    if ((isOpen || isActive) && !disabled)
      onChange({ value: value ?? "", chosenOptions });
  }, [chosenOptions, isActive, isOpen]);

  useEffect(() => {
    if (disabled && chosenOptions.length > 0) {
      setChosenOptions([]);
      setIsOpen(false);
    }
  }, [disabled, chosenOptions]);

  return (
    <Styles.Wrapper ref={ref} isOpen={isOpenOptions} filtration={filtration}>
      <Styles.TitleContainer>
        <Styles.Title
          type="text"
          placeholder={title}
          disabled={isDisabled}
          isOpen={isOpenOptions}
          readOnly={!filtration}
          filtration={filtration}
          value={filtration ? filtrationValue : title}
          onClick={() => (!filtration ? setOpen() : null)}
          onFocus={() => (!filtration ? null : setIsOpen(true))}
          onChange={(e) => (filtration ? onChangeFiltration(e) : null)}
        />

        {filtration && isLoading && (
          <Styles.LoaderContainer>
            <Styles.LoaderCircle />
          </Styles.LoaderContainer>
        )}

        {!filtration && <Styles.Triangle isOpen={isOpenOptions} />}
      </Styles.TitleContainer>

      <Styles.ScrollWrapper isOpen={isOpenOptions} filtration={filtration}>
        <Styles.SubCategories isOpen={isOpenOptions} filtration={filtration}>
          {filtered?.map((option, idx) => (
            <Styles.SubCategory
              filtration={filtration}
              notCheckbox={notCheckbox}
              key={`select-${idx}-${option.value}`}
              isCheck={includes({
                arr: chosenOptions,
                objectCheck: option,
              })}
            >
              {!filtration && !notCheckbox && (
                <CheckBox
                  name={option.text}
                  text={option.text}
                  onChange={() => setOptions(option)}
                  type={multiple ? "checkbox" : "radio"}
                  checked={includes({
                    arr: chosenOptions,
                    objectCheck: option,
                  })}
                />
              )}

              {!filtration && notCheckbox && (
                <Styles.OptionText onClick={() => setOptions(option)}>
                  {option.text}

                  <Styles.Cross />
                </Styles.OptionText>
              )}

              {filtration && (
                <Styles.OptionText onClick={() => setOptions(option)}>
                  {option.text}
                </Styles.OptionText>
              )}
            </Styles.SubCategory>
          ))}
        </Styles.SubCategories>
      </Styles.ScrollWrapper>
    </Styles.Wrapper>
  );
};
