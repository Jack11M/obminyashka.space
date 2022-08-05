import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';

import { showMessage } from 'hooks';
import { putOauthUserThunk } from 'store/auth/thunk';

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
      showMessage(e.response);
    }
  };

  useEffect(() => {
    getOAuth2User().then((res) => showMessage(res));
  }, []);

  return null;
};

export default OAuthSuccess;
