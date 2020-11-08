import React from "react";

const Avatar = ({whatIsClass, width, height, avatar} ) => {
  return (
    <div className={whatIsClass}>
      <svg
        width={width}
        height={height}
        viewBox="0 0 30 28"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M15 0C23.2529 0 30 6.30274 30 14C30 21.6972 23.2471 28 15 28C6.75294 28 0 21.6972 0 14C0 6.30274 6.74706 0 15 0ZM15 4.2C12.4471 4.2 10.5 6.02274 10.5 8.39999C10.5 10.7772 12.4471 12.6 15 12.6C17.5529 12.6 19.5 10.7827 19.5 8.39999C19.5 6.01725 17.5529 4.2 15 4.2ZM24 20.8353C24 18.0353 20.2941 15.12 15 15.12C10.2353 15.12 6 18.0353 6 20.8353C13.3529 20.8353 18.9412 20.8353 24 20.8353Z"
          fill="#CCCCCC"
        />
      </svg>
    </div>
  );
};

export default Avatar;
