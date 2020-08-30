import { useEffect, useState } from "react";
import axios from "axios";

export default (controller, whatPage) => {
  const [isLoading, setIsLoading] = useState(false);
  const [response, setResponse] = useState(null);
  const [errorsResponse, setError] = useState(null);
  const [data, setData] = useState({});

  let baseUrl = `http://54.37.125.180:8080/${controller}/${whatPage}`;
  const headers = { "Content-Type": "application/json" };

  useEffect(() => {
    if (!isLoading) {
      return;
    }

    axios({
      method: "post",
      url: baseUrl,
      data,
      headers
    })
      .then(res => {
        console.log(res.data);
        setIsLoading(false);
        setResponse(res.data);
      })
      .catch(errors => {
        console.log("ErrorFromBase", errors.response.data);
        setIsLoading(false);
        setError(errors.response.data);
      });
  });

  const doFetch = data => {
    setData(data);
    setIsLoading(true);
  };
  return [{ isLoading, response, errorsResponse }, doFetch];
};
