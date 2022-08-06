import { getTranslatedText } from 'components/local/localization';

import { ShowSelectItem } from '../../show-select-item';

const SelectItem = ({
  name,
  data,
  showImg,
  setItem,
  placeholder,
  valueCategory,
}) => (
  <ShowSelectItem
    categories
    name={name}
    data={data}
    showImg={showImg}
    onClick={setItem}
    placeholder={placeholder}
    value={valueCategory.name}
    typeError="popup.selectCategory"
    titleError="popup.errorTitleSubCategory"
    text={getTranslatedText(`categories.${valueCategory.name}`)}
  />
);

export { SelectItem };
