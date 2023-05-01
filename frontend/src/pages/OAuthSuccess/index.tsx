/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { showMessage } from 'obminyashka-components';
import { useNavigate, useLocation } from 'react-router-dom';

import { putOauthUserThunk } from 'src/store/auth/thunk';

const OAuthSuccess = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();
  const code = location.search.replace('?code=', '');

  const getOAuth2User = async () => {
    sessionStorage.setItem('code', code);
    try {
      await dispatch(putOauthUserThunk());
      navigate('/');
    } catch (e) {
      showMessage.error(e.response);
    }
  };

  useEffect(() => {
    getOAuth2User().then((res) => showMessage(res));
  }, []);

  return null;
};

export { OAuthSuccess };
