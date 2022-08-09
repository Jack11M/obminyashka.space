import { useState, useEffect } from 'react';

export const useDelay = (delay) => {
  const [open, setOpen] = useState(false);
  const [a, setA] = useState(false);
  const [timer, setTimer] = useState(null);

  useEffect(() => {
    if (!open) {
      setTimer(
        setTimeout(() => {
          setA(false);
        }, delay)
      );
    }

    if (open) {
      setTimer(null);
      setA(true);
      clearTimeout(timer);
    }
  }, [open]);

  return [a, setOpen];
};
