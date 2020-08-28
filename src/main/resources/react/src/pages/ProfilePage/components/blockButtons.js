import React from "react";

function BlockButtons({ index }) {
  return (
    <div className={"block-buttons"}>
      <div className={"block-buttons__add-remove"}>
        <div className={"block-buttons__add-remove-item"} >
	        <span>Добавить поле</span> <span className={"add"} onClick={() => console.log('111111')}></span>
        </div>
        {Boolean(index) && (
          <div className={"block-buttons__add-remove-item"}>
            <span>Удалить поле</span> <span className={"remove"}></span>
          </div>
        )}
      </div>
    </div>
  );
}

export default BlockButtons;
