import React from "react";

import "./myActyvity.scss";
import Spinner from '../../../../components/spinner/spinner';
import CurrentOffers from '../../../homepage/сurrentOffers/CurrentOffers';

const MyActivity = () => {
  return (
    <>
    <CurrentOffers/>
    <Spinner/>
    </>
  );
};

export default MyActivity;
