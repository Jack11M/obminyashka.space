import "./renderError.scss";

const RenderError = (el) => {
  return {
    showSuccess() {
      el.parentElement.classList.remove("error");
      el.nextElementSibling.innerHTML = "";
    },
    showErrors(errors = "") {
      el.parentElement.classList.add("error");
      el.nextElementSibling.innerHTML =
        errors || el.nextElementSibling.dataset.error;
    },
  };
};

export default RenderError;
