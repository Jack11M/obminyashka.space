import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { putOauthUserThunk } from "store/auth/thunk";
import { useHistory, useLocation } from "react-router-dom";

const OAuthSuccess = () => {
    const history = useHistory();
    const dispatch = useDispatch();
    const location = useLocation();
    const code = location.search.replace('?code=', '');

    const getOAuth2User = async () => {
        sessionStorage.setItem('code', code);
        try {
            await dispatch(putOauthUserThunk());
            history.push('/');
        } catch (e) {
            console.log(e.response);
        }
    }

    useEffect(() => {
        getOAuth2User().then(res => console.log(res));
    }, [])

    return null;
}

export default OAuthSuccess;