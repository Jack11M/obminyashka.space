import { useEffect, useState } from 'react';

import api from 'REST/Resources';
import { showMessage } from 'hooks';

import * as Icon from 'assets/icons';

import * as Styles from './styles';

const Select = () => {
  const [openSelect, setOpenSelect] = useState(false);

  const [receivedCategories, setReceivedCategories] = useState([]);

  useEffect(() => {
    (async () => {
      try {
        const categories = await api.fetchAddGood.getCategoryAll();
        if (Array.isArray(categories)) {
          setReceivedCategories(categories);
          console.log(...categories);
        } else {
          // eslint-disable-next-line no-throw-literal
          throw { message: 'OOps, I didn’t get the category' };
        }
      } catch (err) {
        showMessage(err.response?.data ?? err.message);
      }
    })();
  }, []);

  const handleSelected = (id) => {
    const data = receivedCategories.map((item) => {
      if (item.id === id) {
        return {
          ...item,
          isSelected: !item.isSelected,
        };
      }
      return item;
    });
    setReceivedCategories(data);
  };

  return (
    <>
      {receivedCategories.map((item) => (
        <Styles.TitleBlock
          key={item.id}
          onClick={() => setOpenSelect(!openSelect)}
        >
          <Styles.DisplayFlex>
            <Styles.Title>{item.name}</Styles.Title>

            <Styles.RotateRectangle openSelect={openSelect}>
              <Icon.Rectangle />
            </Styles.RotateRectangle>
          </Styles.DisplayFlex>
        </Styles.TitleBlock>
      ))}

      <Styles.OptionWrapper hideSelect={openSelect}>
        {openSelect &&
          receivedCategories.map((item) => (
            <Styles.SubTitleBlock
              key={item.id}
              isSelected={item.isSelected}
              onClick={() => handleSelected(item.id)}
            >
              <span>{item.name}</span>

              <Styles.Close isSelected={item.isSelected}>
                <Icon.CloseSvg />
              </Styles.Close>
            </Styles.SubTitleBlock>
          ))}
      </Styles.OptionWrapper>
    </>
  );
};

export { Select };
