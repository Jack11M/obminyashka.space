import React from "react";
import Agreement from "../agreement/Agreement.jsx";
import { Link } from "react-router-dom";

const Extra = ({ whatIsPage, setChecked }) => {
  const { isChecking, setIsChecking } = setChecked;
  return (
    <div className="extra">
      <div className="checkbox">
        <input
          checked={isChecking}
          id="check"
          name="checkbox"
          type="checkbox"
          onChange={event => {
            event.target.checked ? setIsChecking(true) : setIsChecking(false);
          }}
        />
        <label className="check" htmlFor="check"></label>
        <label htmlFor="check">
          <Agreement whatIsPage={whatIsPage} />
        </label>
      </div>
      {whatIsPage && (
        <span>
          <Link to="/registration/register">Не можете войти?</Link>
        </span>
      )}
    </div>
  );
};

export default Extra;
